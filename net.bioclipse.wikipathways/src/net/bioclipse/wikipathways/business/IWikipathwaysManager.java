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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.core.runtime.CoreException;
import org.xml.sax.SAXException;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="TODO: Describe the manager here."
)
public interface IWikipathwaysManager extends IBioclipseManager {

	  @Recorded
	  @PublishedMethod(
	        params = "", 
	        methodSummary = "Returns list of organisms covered in WikiPathways."
	        	
	    )
    public String listOrganisms()
	throws ParserConfigurationException, SAXException, IOException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "String organism", 
	        methodSummary = "Returns list of pathways for a given organism covered in WikiPathways."
	        	
	    )
    public String listPathways(String organism)
	throws ParserConfigurationException, SAXException, IOException, TransformerException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "String pwId, Integer revision", 
	        methodSummary = "Download the pathway from WikiPathways."
	        	
	    )
    public String getPathways(String pwId, Integer revision)
	throws ParserConfigurationException, SAXException, IOException, TransformerException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "String pwId", 
	        methodSummary = "Get some general info about the pathway, such as the name, species, without downloading the GPML."
	        	
	    )
    public String getPathwayInfo(String pwId)
	throws ParserConfigurationException, SAXException, IOException, TransformerException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "String pwId, String timestamp", 
	        methodSummary = "Get the revision history of a pathway."
	        	
	    )
    public String getPathwayHistory(String pwId)
	throws ParserConfigurationException, SAXException, IOException, TransformerException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "String timestamp", 
	        methodSummary = "Get the recently changed pathways."
	        	
	    )
    public String getRecentChanges(String timestamp)
	throws ParserConfigurationException, SAXException, IOException, TransformerException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "Find pathways using a textual search on the description and text labels of the pathway objects.", 
	        methodSummary = "Get the recently changed pathways."
	        	
	    )
    public String findPathwaysByText(String query, String species)
	throws ParserConfigurationException, SAXException, IOException, TransformerException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "Find pathways using a textual search on the description and text labels of the pathway objects.", 
	        methodSummary = "Get the recently changed pathways."
	        	
	    )
    public String findPathwaysByXref(String[] ids, String codes)
	throws ParserConfigurationException, SAXException, IOException, TransformerException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "Find pathways using a textual search on the description and text labels of the pathway objects.", 
	        methodSummary = "Get the recently changed pathways."
	        	
	    )
    public String findInteractions(String query)
	throws ParserConfigurationException, SAXException, IOException, TransformerException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "Find pathways using a textual search on the description and text labels of the pathway objects.", 
	        methodSummary = "Get the recently changed pathways."
	        	
	    )
    public String findPathwaysByLiterature(String query)
	throws ParserConfigurationException, SAXException, IOException, TransformerException;
}
