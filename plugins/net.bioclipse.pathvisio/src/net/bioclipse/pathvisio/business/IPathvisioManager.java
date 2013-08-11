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
import java.util.Set;

import org.bridgedb.IDMapperException;
import org.pathvisio.core.model.ConverterException;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="Pathvisio manager for biological pathways.",
    doi="10.1186/1471-2105-9-399"
)
public interface IPathvisioManager extends IBioclipseManager {

    @PublishedMethod(
        methodSummary = "Export an image from a pathway file in GPML format",
        params="String pathwayFile"
    )
    public String exportPNG(String pathwayFile) throws ConverterException, 
    IOException, IDMapperException;
    
    @PublishedMethod(
    	methodSummary = "Query wikipathways for a protein label",
    	params="String label"
    )
	public Set<String> queryWikipathways(String label) throws BioclipseException;
    
    @PublishedMethod(
        methodSummary = "Gets the GPML for the given WikiPathway",
        params="String pathwayID"
    )
	public String getGPML(String pathwayID) 
			throws BioclipseException;
}
