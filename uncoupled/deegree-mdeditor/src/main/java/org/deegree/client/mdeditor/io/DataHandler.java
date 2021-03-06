//$HeadURL: svn+ssh://lbuesching@svn.wald.intevation.de/deegree/base/trunk/resources/eclipse/files_template.xml $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.client.mdeditor.io;

import java.util.List;
import java.util.Map;

import org.deegree.client.mdeditor.configuration.ConfigurationException;
import org.deegree.client.mdeditor.io.xml.XMLDataHandler;
import org.deegree.client.mdeditor.model.Dataset;
import org.deegree.client.mdeditor.model.FormGroup;
import org.deegree.client.mdeditor.model.DataGroup;
import org.deegree.commons.utils.StringPair;

/**
 * handles all jobs concerning reading and writing form groups
 * 
 * 
 * @author <a href="mailto:buesching@lat-lon.de">Lyn Buesching</a>
 * @author last edited by: $Author: lyn $
 * 
 * @version $Revision: $, $Date: $
 */
public abstract class DataHandler {

    private static XMLDataHandler handler;

    static {
        handler = new XMLDataHandler();
    }

    /**
     * creates a list containing value and label out of the data groups of the form group with the given id
     * 
     * @param grpId
     *            the id of the form group
     * @param referenceLabel
     *            the pattern describing the label
     * @return a list of all available data groups with the given id
     */
    public abstract List<StringPair> getItems( String grpId, String referenceLabel )
                            throws ConfigurationException;

    /**
     * @return a list of all datasets
     */
    public abstract List<Dataset> getDatasets()
                            throws ConfigurationException, DataIOException;

    /**
     * @param grpId
     *            the id of the group
     * @return a list of all data groups of the form group with the given id
     */
    public abstract List<DataGroup> getDataGroups( String grpId )
                            throws ConfigurationException;

    /**
     * deletes the data group with the given id assigned to the form group with the given id
     * 
     * @param grpId
     *            the id of the form group
     * @param id
     *            the id of the data group
     */
    public abstract void deleteDataGroup( String grpId, String id )
                            throws ConfigurationException;

    /**
     * @param grpId
     *            the id of the form group
     * @param id
     *            the id of the data group
     * @return the data group with the given id assigned to the form group with the given id; null, if the data group
     *         does not exist or could not be read
     */
    public abstract DataGroup getDataGroup( String grpId, String id )
                            throws ConfigurationException;

    /**
     * 
     * Writes a data group. If a data group with the given id exist, the existing data group will be overwritten.
     * 
     * @param formGroup
     *            the elements of the data group to write
     * @param id
     *            the id of the data group
     * @return the id of the data group
     * @throws DataIOException
     *             if the data group could not be written
     */
    public abstract String writeDataGroup( String id, FormGroup formGroup )
                            throws DataIOException, ConfigurationException;

    /**
     * Writes a dataset. If a dataset with the given id exist, the existing dataset will be overwritten.
     * 
     * @param id
     *            the id of the dataset
     * @param formGroups
     *            the elements of the dataset to write
     * @param dataGroups
     *            the data groups assigne dto this dataset
     * @return the id of the dataset
     * @throws DataIOException
     *             if the dataset could not be written
     */
    public abstract String writeDataset( String id, List<FormGroup> formGroups, Map<String, List<DataGroup>> dataGroups )
                            throws DataIOException, ConfigurationException;

    /**
     * 
     * Reads the dataset with the given id.
     * 
     * @param id
     *            the id of the dataset to read
     * @return the datset
     * @throws DataIOException
     */
    public abstract Dataset getDataset( String id )
                            throws DataIOException, ConfigurationException;

    /**
     * deletes the id with the given dataset
     * 
     * @param id
     *            the id of the dataset to delete
     */
    public abstract void deleteDataset( String id )
                            throws ConfigurationException;

    /**
     * @return an instance of the used FormGroupHandler
     */
    public static DataHandler getInstance() {
        // TODO!!!
        return handler;
    }
}
