/*******************************************************************************
 * Copyright (c) 2012  Andra Waagmeester <andra.waagmeester@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.wikipathways.business;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.bioclipse.managers.business.IBioclipseManager;

import org.bridgedb.bio.Organism;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.wikipathways.client.WikiPathwaysClient;

public class WikipathwaysManager implements IBioclipseManager {

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "wikipathways";
    }
    public String getWebServiceAddress() {
        return "http://webservice.wikipathways.org";
    }
    
    public List<String> listOrganisms(IProgressMonitor monitor) throws IOException {
    	if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		monitor.beginTask(
			"Downloading list of organisms covered in WikiPathways.", 2
		);
    	URL wsURL = new URL(getWebServiceAddress());
		WikiPathwaysClient client = new WikiPathwaysClient(wsURL);
		String[] organisms = client.listOrganisms();
		List<String> organismList = new ArrayList<String>();
		for (String organism : organisms) organismList.add(organism);
		monitor.worked(2);
    	return organismList;
    }
    
    public List<String> listPathways(String organism, IProgressMonitor monitor) throws IOException {
    	if (monitor == null) {
			monitor = new NullProgressMonitor();
		}    	
		monitor.beginTask(
			"Returns list of pathways for a given organism covered in WikiPathways.", 2
		);
    	URL wsURL = new URL(getWebServiceAddress());
		WikiPathwaysClient client = new WikiPathwaysClient(wsURL);
		Organism organismObj = Organism.fromLatinName(organism);
		WSPathwayInfo[] pathwayObjs = client.listPathways(organismObj);
		List<String> pathways = new ArrayList<String>();
		for (int i=0; i<pathwayObjs.length; i++) {
			pathways.add(pathwayObjs[i].getId());
		}
		monitor.worked(2);
    	return pathways;
    }
    
    
}
