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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

import org.bridgedb.bio.Organism;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.wikipathways.webservice.WSHistoryRow;
import org.pathvisio.wikipathways.webservice.WSOntologyTerm;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayHistory;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.wikipathways.client.WikiPathwaysClient;

public class WikipathwaysManager implements IBioclipseManager {

	private WikiPathwaysClient wpClient = null;

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

    private WikiPathwaysClient getClient() throws MalformedURLException {
    	if (wpClient == null) {
    		URL wsURL = new URL(getWebServiceAddress());
    		wpClient = new WikiPathwaysClient(wsURL);
    	}
    	return wpClient;
    }
    
    public String login(String name, String pass, IProgressMonitor monitor) throws BioclipseException {
    	if (monitor == null) monitor = new NullProgressMonitor();

		monitor.beginTask(
			"Getting an authorization key.", 2
		);
		try {
	    	WikiPathwaysClient client = getClient();
			client.login(name, pass);
		} catch (Throwable exception) {
			throw new BioclipseException(
				"Error while login in on the WikiPathways website: " + exception.getMessage(),
				exception
			);
		}
		return "";
    }

    public List<String> listOrganisms(IProgressMonitor monitor) throws IOException {
    	if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		monitor.beginTask(
			"Downloading list of organisms covered in WikiPathways.", 2
		);
    	WikiPathwaysClient client = getClient();
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
    	WikiPathwaysClient client = getClient();
		Organism organismObj = Organism.fromLatinName(organism);
		WSPathwayInfo[] pathwayObjs = client.listPathways(organismObj);
		List<String> pathways = new ArrayList<String>();
		for (int i=0; i<pathwayObjs.length; i++) {
			pathways.add(pathwayObjs[i].getId());
		}
		monitor.worked(2);
    	return pathways;
    }
    
    public String getPathway(String pwId, Integer revision, IProgressMonitor monitor)
    		throws IOException, ConverterException {
    	if (monitor == null) {
			monitor = new NullProgressMonitor();
		}    	
		monitor.beginTask(
			"Returns a pathway as GPML.", 2
		);
    	WikiPathwaysClient client = getClient();
		WSPathway wsPathway = client.getPathway(pwId, revision.intValue());
		Pathway pathway = WikiPathwaysClient.toPathway(wsPathway);
		monitor.worked(2);
    	return pathway.toString();
    }

    public List<String> getPathwayHistory(String pwId, IProgressMonitor monitor)
    		throws IOException, ParseException {
    	if (monitor == null) {
			monitor = new NullProgressMonitor();
		}    	
		monitor.beginTask(
			"Returns the history of a pathway.", 2
		);
    	WikiPathwaysClient client = getClient();
		WSPathwayHistory history = client.getPathwayHistory(pwId, new Date(0));
		List<String> revisions = new ArrayList<String>();
		WSHistoryRow[] revs = history.getHistory();
		for (WSHistoryRow revision : revs) {
			revisions.add(revision.getRevision());
		}
		monitor.worked(2);
    	return revisions;
    }

    public List<String> getOntologyTermsByPathway(String pwId, IProgressMonitor monitor)
    		throws IOException, ParseException {
    	if (monitor == null) {
			monitor = new NullProgressMonitor();
		}    	
		monitor.beginTask(
			"Look up the ontology terms of a pathway.", 2
		);
    	WikiPathwaysClient client = getClient();
		WSOntologyTerm[] terms = client.getOntologyTermsByPathway(pwId);
		List<String> revisions = new ArrayList<String>();
		for (WSOntologyTerm term : terms) {
			revisions.add(term.getName() + " (" + term.getId() + ", " + term.getOntology() + ")");
		}
		monitor.worked(2);
    	return revisions;
    }

    public boolean saveOntologyTag(String pwId, String term, String termId, IProgressMonitor monitor)
    	    throws IOException, BioclipseException {
    	if (monitor == null) {
    		monitor = new NullProgressMonitor();
    	}    	
    	monitor.beginTask(
    		"Save an ontology term for a pathway.", 2
    	);
    	WikiPathwaysClient client = getClient();
    	boolean success = client.saveOntologyTag(pwId, term, termId);
    	monitor.worked(2);
    	return success;
    }

    public boolean removeOntologyTag(String pwId, String termId, IProgressMonitor monitor)
    	    throws IOException, BioclipseException {
    	if (monitor == null) {
    		monitor = new NullProgressMonitor();
    	}    	
    	monitor.beginTask(
    		"Removes an ontology term for a pathway.", 2
    	);
    	WikiPathwaysClient client = getClient();
    	boolean success = client.removeOntologyTag(pwId, termId);
    	monitor.worked(2);
    	return success;
    }

}
