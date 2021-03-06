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
package org.deegree.client.mdeditor.gui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.ScopeContext;

import org.deegree.client.mdeditor.configuration.ConfigurationException;
import org.deegree.client.mdeditor.configuration.ConfigurationManager;
import org.deegree.client.mdeditor.model.FormConfigurationDescription;

/**
 * TODO add class documentation here
 * 
 * @author <a href="mailto:buesching@lat-lon.de">Lyn Buesching</a>
 * @author last edited by: $Author: lyn $
 * 
 * @version $Revision: $, $Date: $
 */
@ManagedBean
@SessionScoped
public class SelectConfigurationBean implements Serializable {

    private static final long serialVersionUID = 6450261738349024830L;

    private String selectedConfiguration;

    private boolean global = false;

    private Map<String, Object> configBeans = new HashMap<String, Object>();

    private static final String SCOPE_NAME = "editorBean";

    public List<FormConfigurationDescription> getDescriptions()
                            throws ConfigurationException {
        List<FormConfigurationDescription> describtions = ConfigurationManager.getConfiguration().getDescribtions(
                                                                                                                   global );
        return describtions;
    }

    public String getDescription()
                            throws ConfigurationException {
        FormConfigurationDescription desc = ConfigurationManager.getConfiguration().getDescribtion(
                                                                                                    selectedConfiguration,
                                                                                                    global );
        return desc != null ? desc.getDescription() : null;
    }

    public void setSelectedConfiguration( String selectedConfiguration ) {
        this.selectedConfiguration = selectedConfiguration;
    }

    public String getSelectedConfiguration()
                            throws ConfigurationException {
        return selectedConfiguration;
    }

    public void setGlobal( boolean global ) {
        this.global = global;
    }

    public boolean isGlobal() {
        return global;
    }

    public Map<String, Object> getMap() {
        return configBeans;
    }

    private void removeFromScope() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ScopeContext context = new ScopeContext( SCOPE_NAME, configBeans );
        fc.getApplication().publishEvent( fc, PreDestroyCustomScopeEvent.class, context );
        configBeans.clear();
    }

    private void addToScope()
                            throws ConfigurationException {
        configBeans.put( SCOPE_NAME, new EditorBean( selectedConfiguration ) );
        FacesContext fc = FacesContext.getCurrentInstance();
        ScopeContext context = new ScopeContext( SCOPE_NAME, configBeans );
        fc.getApplication().publishEvent( fc, PostConstructCustomScopeEvent.class, context );
    }

    public Object load()
                            throws ConfigurationException {
        if ( selectedConfiguration == null ) {
            FacesContext fc = FacesContext.getCurrentInstance();
            FacesMessage msg = GuiUtils.getFacesMessage( fc, FacesMessage.SEVERITY_ERROR, "ERROR.LOAD_CONF_NOID" );
            fc.addMessage( "LOAD_CONFIGURATION_NO_ID", msg );
            return "error";
        }
        if ( configBeans.containsKey( SCOPE_NAME )
             && !selectedConfiguration.equals( ( (EditorBean) configBeans.get( SCOPE_NAME ) ).getConfId() ) ) {
            removeFromScope();
        }
        if ( !configBeans.containsKey( SCOPE_NAME ) ) {
            addToScope();
        }
        return "forms";
    }
}
