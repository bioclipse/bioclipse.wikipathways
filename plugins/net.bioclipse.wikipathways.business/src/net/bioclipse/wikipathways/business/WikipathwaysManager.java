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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.bioclipse.managers.business.IBioclipseManager;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WikipathwaysManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(WikipathwaysManager.class);

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "wikipathways";
    }
    public String getWebServiceAddress() {
        return "http://www.wikipathways.org/wpi/webservice/webservice.php/";
    }
    
    public String listOrganisms(IProgressMonitor monitor) throws ParserConfigurationException, SAXException, IOException {
    	if (monitor == null) {
			monitor = new NullProgressMonitor();
		}    	
		monitor.beginTask(
				"Downloading list of organisms covered in WikiPathways.", 2
		);
    	URL url = new URL(this.getWebServiceAddress()+"listOrganisms");
    	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(url.openStream());
        NodeList wpOrganismsList = doc.getElementsByTagName("ns1:organisms");
        String wpOrganisms = "";
        if (wpOrganismsList.getLength() > 0){
        
        for (int i=0; i<wpOrganismsList.getLength(); i++){
        	wpOrganisms = wpOrganisms + wpOrganismsList.item(i).getTextContent()+"\n";
        }
        }
    	
    	return wpOrganisms;
    }
    
    public String listPathways(String organism, IProgressMonitor monitor) throws ParserConfigurationException, SAXException, IOException, TransformerException {
    	if (monitor == null) {
			monitor = new NullProgressMonitor();
		}    	
		monitor.beginTask(
				"Returns list of pathways for a given organism covered in WikiPathways.", 2
		);
		 URL url = new URL(this.getWebServiceAddress()+"listPathways?organism="+organism.replace(" ", "%20")); // TODO Fix this temporary ugly hack
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							url.openConnection().getInputStream()
					)
			);
			String line = reader.readLine();
			String pathwayString = ""; 
			while (line != null) {
				pathwayString = pathwayString + line+'\n';
				line = reader.readLine();
				
			}
    	return pathwayString;
    }
    
    
}
