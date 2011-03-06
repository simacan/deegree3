//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
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
package org.deegree.metadata.persistence;

import org.deegree.commons.annotations.ConsoleManaged;
import org.deegree.commons.config.AbstractResourceManager;
import org.deegree.commons.config.DeegreeWorkspace;
import org.deegree.commons.config.DefaultResourceManagerMetadata;
import org.deegree.commons.config.ResourceManager;
import org.deegree.commons.config.ResourceManagerMetadata;
import org.deegree.commons.config.WorkspaceInitializationException;
import org.deegree.commons.jdbc.ConnectionManager;
import org.deegree.commons.utils.ProxyUtils;

/**
 * Entry point for creating {@link MetadataStore} providers and instances.
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class MetadataStoreManager extends AbstractResourceManager<MetadataStore> {

    private MetadataStoreManagerMetadata metadata;

    @SuppressWarnings("unchecked")
    public Class<? extends ResourceManager>[] getDependencies() {
        return new Class[] { ProxyUtils.class, ConnectionManager.class };
    }

    @Override
    public void startup( DeegreeWorkspace workspace )
                            throws WorkspaceInitializationException {
        metadata = new MetadataStoreManagerMetadata( workspace );
        super.startup( workspace );
    }

    @ConsoleManaged(startPage = "/console/metadatastore/buttons")
    static class MetadataStoreManagerMetadata extends DefaultResourceManagerMetadata<MetadataStore> {
        public MetadataStoreManagerMetadata( DeegreeWorkspace workspace ) {
            super( "metadata stores", "datasources/metadata/", MetadataStoreProvider.class, workspace );
        }
    }

    public ResourceManagerMetadata<MetadataStore> getMetadata() {
        return metadata;
    }
}