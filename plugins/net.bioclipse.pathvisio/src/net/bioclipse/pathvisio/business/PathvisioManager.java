/*******************************************************************************
 * Copyright (c) 2012  Ola Spjuth <ola.spjuth@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.pathvisio.business;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.bioclipse.business.IBioclipsePlatformManager;
import net.bioclipse.core.ResourcePathTransformer;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IStringMatrix;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.rdf.Activator;
import net.bioclipse.rdf.business.IRDFManager;

import org.apache.log4j.Logger;
import org.bridgedb.IDMapperException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.xmlrpc.PathwayGpml;

/**
 * 
 * @author ola
 *
 */
public class PathvisioManager implements IBioclipseManager {

	public static final String WIKIPATHWAYS_BASE_URL = 
			"http://www.wikipathways.org//wpi/wpi.php?action=downloadFile&type=gpml&pwTitle=Pathway:";

	private static final Logger logger = Logger.getLogger(PathvisioManager.class);

	
	/**
	 * Plugin for Pathvisio (www.pathvisio.org).
	 */
	public String getManagerName() {
		return "pathvisio";
	}

	public String exportPNG(String pathwayFile) throws BioclipseException{

		IFile p = ResourcePathTransformer.getInstance().transform(pathwayFile);
		System.out.println(p.getLocation());

		String inputFile=p.getLocation().toFile().getPath();
		if (inputFile.endsWith(".gpml"))
			inputFile = inputFile.replace(".gpml", "");

		PathwayGpml path = new PathwayGpml();
		String r;
		try {
			r = path.exportPathway(inputFile, "png");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BioclipseException(e.getMessage());
		}

		try {
			p.getParent().refreshLocal(1, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			throw new BioclipseException(e.getMessage());
		}

		return inputFile+"."+"png";
	}

	public Set<String> queryWikipathways(String label) throws BioclipseException{

		String endpoint = "http://sparql.wikipathways.org/";
		
		String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
			+"PREFIX wp: <http://vocabularies.wikipathways.org/wp#>"
			+"PREFIX dcterms: <http://purl.org/dc/terms/>"
			+"select distinct ?pathway ?label where {"
			+" ?geneProduct a wp:GeneProduct . "
			+" ?geneProduct rdfs:label ?label . "
			+" ?geneProduct dcterms:isPartOf ?pathway ."
			+" FILTER regex(str(?label), \"" + label + "\"). "
			+" FILTER regex(str(?pathway), \"^http\")."
			+"}";		

		IRDFManager rdf = Activator.getDefault().getJavaManager();
		IStringMatrix result = rdf.sparqlRemote(endpoint, query);
		
		List<String> col = result.getColumn("pathway");
		Set<String> res = new HashSet<String>();
		for (String str : col){
			res.add(str);
		}

		return res;
	}
	
	public String getGPML(String pathwayID) 
			throws BioclipseException{
		
		//Extract the WP ID
		if (!pathwayID.startsWith("WP")) pathwayID = "WP" + pathwayID;
		if (pathwayID.contains("_")) {
			String pattern = ".*\\/(WP\\d+)_.*";
			pathwayID = pathwayID.replaceAll(pattern, "$1");
		}
		
		IBioclipsePlatformManager bc = net.bioclipse.business.Activator.getDefault().getJavaManager();

		String res = bc.downloadAsFile(WIKIPATHWAYS_BASE_URL+pathwayID, "/Virtual/"+pathwayID+".gpml");
		return res;
	}



}
