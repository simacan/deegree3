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
package org.deegree.rendering.r3d.persistence;

import static java.io.File.separator;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.deegree.commons.config.AbstractBasicResourceManager;
import org.deegree.commons.config.DeegreeWorkspace;
import org.deegree.commons.config.ResourceManager;
import org.deegree.commons.config.ResourceManagerMetadata;
import org.deegree.commons.config.ResourceProvider;
import org.deegree.commons.config.ResourceState;
import org.deegree.commons.jdbc.ConnectionManager;
import org.deegree.commons.utils.FileUtils;
import org.deegree.commons.utils.ProxyUtils;
import org.deegree.commons.xml.stax.XMLStreamUtils;
import org.deegree.coverage.persistence.CoverageBuilderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code></code> class TODO add class documentation here.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class RenderableStoreManager extends AbstractBasicResourceManager {

    private static final Logger LOG = LoggerFactory.getLogger( RenderableStoreManager.class );

    Map<String, RenderableStoreProvider> nsToProvider = new ConcurrentHashMap<String, RenderableStoreProvider>();

    private static Map<String, RenderableStore> idToStore = Collections.synchronizedMap( new HashMap<String, RenderableStore>() );

    /**
     * Initializes the {@link RenderableStoreManager} by loading all {@link RenderableStore} configurations from the
     * given directory.
     * 
     * @param configLocation
     *            containing renderable manager configurations
     */
    public void init( File configLocation, DeegreeWorkspace workspace ) {
        if ( !configLocation.exists() ) {
            LOG.info( "No 'datasources/renderable' directory -- skipping initialization of renderable stores." );
            return;
        }
        LOG.info( "--------------------------------------------------------------------------------" );
        LOG.info( "Setting up renderable stores." );
        LOG.info( "--------------------------------------------------------------------------------" );
        File[] rsConfigFiles = null;
        if ( configLocation.isFile() ) {
            String ext = FileUtils.getFileExtension( configLocation );
            if ( "xml".equalsIgnoreCase( ext ) ) {
                rsConfigFiles = new File[] { configLocation };
            }
        } else {
            rsConfigFiles = configLocation.listFiles( new FilenameFilter() {
                @Override
                public boolean accept( File dir, String name ) {
                    return name.toLowerCase().endsWith( ".xml" );
                }
            } );
        }
        if ( rsConfigFiles == null ) {
            LOG.warn( "Did not find any RenderableStore configuration files in directory: "
                      + configLocation.getAbsolutePath() + " no global renderable manager will be available." );
            return;
        }
        for ( File rsConfigFile : rsConfigFiles ) {
            if ( rsConfigFile != null ) {
                String storeId = FileUtils.getFilename( rsConfigFile );
                LOG.info( "Setting up RenderableStore '" + storeId + "' from file '" + rsConfigFile.getName() + "'..."
                          + "" );
                if ( idToStore.containsKey( storeId ) ) {
                    String msg = "Duplicate definition of RenderableStore with id '" + storeId + "'.";
                    LOG.warn( msg );
                } else {
                    try {
                        RenderableStore rs = create( rsConfigFile.toURI().toURL(), workspace );
                        if ( rs != null ) {
                            LOG.info( "Registering global RenderableStore with id '" + storeId + "', type: '"
                                      + rs.getClass().getCanonicalName() + "'" );
                            idToStore.put( storeId, rs );
                        } else {
                            LOG.info( "RenderableStore with id '" + storeId + "', could not be loaded (null)." );
                        }
                    } catch ( Exception e ) {
                        LOG.error( "Error initializing RenderableStore: " + e.getMessage(), e );
                    }
                }
            }
        }
        LOG.info( "" );
    }

    /**
     * Returns the {@link RenderableStore} instance with the specified identifier.
     * 
     * @param id
     *            identifier of the renderable store instance
     * @return the corresponding {@link RenderableStore} instance or null if no such instance has been created
     */
    public static RenderableStore get( String id ) {
        return idToStore.get( id );
    }

    /**
     * Returns all active {@link RenderableStore}s.
     * 
     * @return the {@link RenderableStore}s instance, may be empty but never <code>null</code>
     */
    public static Collection<RenderableStore> getAll() {
        return idToStore.values();
    }

    /**
     * Returns an uninitialized {@link RenderableStore} instance created from the given configuration document.
     * 
     * @param configURL
     *            URL of the configuration document, must not be <code>null</code>
     * @return corresponding {@link RenderableStore} instance, initialized and ready to be used
     * @throws IllegalArgumentException
     *             if the creation fails, e.g. due to a configuration error
     */
    private synchronized RenderableStore create( URL configURL, DeegreeWorkspace workspace )
                            throws IllegalArgumentException {

        String namespace = null;
        try {
            XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader( configURL.openStream() );
            XMLStreamUtils.nextElement( xmlReader );
            namespace = xmlReader.getNamespaceURI();
        } catch ( Exception e ) {
            String msg = "Error determining configuration namespace for file '" + configURL + "'";
            LOG.error( msg );
            throw new IllegalArgumentException( msg );
        }
        LOG.debug( "Config namespace: '" + namespace + "'" );
        RenderableStoreProvider builder = nsToProvider.get( namespace );
        if ( builder == null ) {
            String msg = "No RenderableStore provider for namespace '" + namespace + "' (file: '" + configURL
                         + "') registered. Skipping it.";
            LOG.error( msg );
            throw new IllegalArgumentException( msg );
        }
        return builder.build( configURL, workspace );
    }

    @SuppressWarnings("unchecked")
    public Class<? extends ResourceManager>[] getDependencies() {
        // TODO verify deps
        return new Class[] { ProxyUtils.class, ConnectionManager.class, CoverageBuilderManager.class };
    }

    public void shutdown() {
        // no cleanup needed?
    }

    public void startup( DeegreeWorkspace workspace ) {
        ServiceLoader<RenderableStoreProvider> providerLoader = ServiceLoader.load( RenderableStoreProvider.class,
                                                                                    workspace.getModuleClassLoader() );
        try {
            for ( RenderableStoreProvider builder : providerLoader ) {
                if ( builder != null ) {
                    LOG.debug( "Service loader found RenderableStoreProvider: " + builder + ", namespace: "
                               + builder.getConfigNamespace() );
                    if ( nsToProvider.containsKey( builder.getConfigNamespace() ) ) {
                        LOG.error( "Multiple RenderableStoreProviders for config namespace: '"
                                   + builder.getConfigNamespace() + "' on classpath -- omitting provider '"
                                   + builder.getClass().getName() + "'." );
                        continue;
                    }
                    nsToProvider.put( builder.getConfigNamespace(), builder );
                }
            }
        } catch ( Exception e ) {
            LOG.error( e.getMessage(), e );
        }

        init( new File( workspace.getLocation(), "datasources" + separator + "renderable" ), workspace );
    }

    public ResourceManagerMetadata<RenderableStore> getMetadata() {
        return new ResourceManagerMetadata<RenderableStore>() {
            public String getName() {
                return "renderable stores";
            }

            public String getPath() {
                return "datasources/renderable/";
            }

            public List<ResourceProvider> getResourceProviders() {
                return new LinkedList<ResourceProvider>( nsToProvider.values() );
            }
        };
    }

    @Override
    public ResourceState<RenderableStore> activate( String id ) {
        throw new UnsupportedOperationException( "Needs implementation." );

    }

    @Override
    public ResourceState<RenderableStore> deactivate( String id ) {
        throw new UnsupportedOperationException( "Needs implementation." );

    }

    @Override
    protected ResourceProvider getProvider( URL file ) {
        throw new UnsupportedOperationException( "Needs implementation." );
    }

    @Override
    protected void remove( String id ) {
        throw new UnsupportedOperationException( "Needs implementation." );
    }
}