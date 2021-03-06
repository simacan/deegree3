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
package org.deegree.client.mdeditor.configuration;

import java.util.List;

import org.deegree.client.mdeditor.configuration.ConfigurationException;
import org.deegree.client.mdeditor.configuration.ConfigurationManager;
import org.deegree.client.mdeditor.model.FormConfigurationDescription;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * TODO add class documentation here
 * 
 * @author <a href="mailto:buesching@lat-lon.de">Lyn Buesching</a>
 * @author last edited by: $Author: lyn $
 * 
 * @version $Revision: $, $Date: $
 */
public class MDEditorConfigurationParserTest extends TestCase {

    @Test
    public void testParseConfiguration()
                            throws ConfigurationException {
        Configuration conf = ConfigurationManager.getConfiguration();
        assertNotNull( conf );

        assertNotNull( conf.getDataDirURL() );
        assertTrue( conf.getDataDirURL().getPath().endsWith( "data" ) );

        assertNotNull( conf.getExportDirURL() );
        assertTrue( conf.getExportDirURL().getPath().endsWith( "export" ) );

        assertNotNull( conf.getDescribtions( false ) );
        assertEquals( 2, conf.getDescribtions( false ).size() );

        FormConfigurationDescription desc = conf.getDescribtions( false ).get( 0 );
        assertNotNull( desc );
        assertEquals( "simple", desc.getId() );
        assertEquals( "Datensätze/Serien", desc.getTitle() );
        assertNotNull( desc.getDescription() );
        assertTrue( desc.getDescription().contains( "Erstellen " ) );
        assertTrue( desc.getConfUrl().toExternalForm().endsWith( "simpleTestConfig.xml" ) );

        List<FormConfigurationDescription> globalDescriptions = conf.getDescribtions( true );
        assertNotNull( globalDescriptions );
        assertEquals( 1, globalDescriptions.size() );
        assertEquals( "global", globalDescriptions.get( 0 ).getId() );

    }
}
