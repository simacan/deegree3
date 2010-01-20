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
package org.deegree.record.persistence.sqltransform.postgres;

import static org.deegree.record.persistence.MappingInfo.ColumnType.DATE;
import static org.deegree.record.persistence.MappingInfo.ColumnType.STRING;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.deegree.filter.Expression;
import org.deegree.filter.Operator;
import org.deegree.filter.expression.Literal;
import org.deegree.filter.expression.PropertyName;
import org.deegree.record.persistence.MappingInfo;

/**
 * TODO add class documentation here
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author: thomas $
 * 
 * @version $Revision: $, $Date: $
 */
public class ExpressionFilterHandling {
    
    private Set<String> table;

    private Set<String> column;
    
    private String expression;
    
    private QName propName;
    
    private static Map<String, MappingInfo> propToTableAndCol = new HashMap<String, MappingInfo>();

    static {
        //----------------------------------------------------------------------------------------
        //----------------------<common queryable properties>-------------------------------------
        propToTableAndCol.put( "apiso:title", new MappingInfo( "isoqp_title", "title", STRING ) );
        propToTableAndCol.put( "dc:title", new MappingInfo( "isoqp_title", "title", STRING ) );
        propToTableAndCol.put( "Title", new MappingInfo( "isoqp_title", "title", STRING ) );
        
        propToTableAndCol.put( "apiso:abstract", new MappingInfo( "isoqp_abstract", "abstract", STRING ) );
        propToTableAndCol.put( "dct:abstract", new MappingInfo( "isoqp_abstract", "abstract", STRING ) );
        propToTableAndCol.put( "Abstract", new MappingInfo( "isoqp_abstract", "abstract", STRING ) );
        
        propToTableAndCol.put( "apiso:BoundingBox", new MappingInfo( "isoqp_BoundingBox", "bbox", STRING ) );
        propToTableAndCol.put( "dc:coverage", new MappingInfo( "isoqp_BoundingBox", "bbox", STRING ) );
        propToTableAndCol.put( "ows:BoundingBox", new MappingInfo( "isoqp_BoundingBox", "bbox", STRING ) );
        
        propToTableAndCol.put( "apiso:type", new MappingInfo( "isoqp_type", "type", STRING ) );
        propToTableAndCol.put( "dc:type", new MappingInfo( "isoqp_type", "type", STRING ) );
        propToTableAndCol.put( "Type", new MappingInfo( "isoqp_type", "type", STRING ) );
        
        propToTableAndCol.put( "apiso:format", new MappingInfo( "isoqp_format", "format", STRING ) );
        propToTableAndCol.put( "dc:format", new MappingInfo( "isoqp_format", "format", STRING ) );
        propToTableAndCol.put( "Format", new MappingInfo( "isoqp_format", "format", STRING ) );
        
        propToTableAndCol.put( "apiso:subject", new MappingInfo( "isoqp_keyword", "keyword", STRING ) );
        propToTableAndCol.put( "dc:subject", new MappingInfo( "isoqp_keyword", "keyword", STRING ) );
        propToTableAndCol.put( "Subject", new MappingInfo( "isoqp_keyword", "keyword", STRING ) );
        
        propToTableAndCol.put( "apiso:anyText", new MappingInfo( "datasets", "anytext", STRING ) );
        propToTableAndCol.put( "AnyText", new MappingInfo( "datasets", "anytext", STRING ) );
        
        propToTableAndCol.put( "apiso:identifier", new MappingInfo( "datasets", "identifier", STRING ) );
        propToTableAndCol.put( "dc:identifier", new MappingInfo( "datasets", "identifier", STRING ) );
        propToTableAndCol.put( "Identifier", new MappingInfo( "datasets", "identifier", STRING ) );
        
        propToTableAndCol.put( "apiso:modified", new MappingInfo( "datasets", "modified", DATE ) );
        propToTableAndCol.put( "dct:modified", new MappingInfo( "datasets", "modified", DATE ) );
        propToTableAndCol.put( "Modified", new MappingInfo( "datasets", "modified", DATE ) );
        
        propToTableAndCol.put( "apiso:CRS", new MappingInfo( "isoqp_crs", "crs", STRING ) );
        propToTableAndCol.put( "CRS", new MappingInfo( "isoqp_crs", "crs", STRING ) );
        propToTableAndCol.put( "dc:CRS", new MappingInfo( "isoqp_crs", "crs", STRING ) );
        
        propToTableAndCol.put( "apiso:association", new MappingInfo( "isoqp_association", "relation", STRING ) );
        propToTableAndCol.put( "dc:relation", new MappingInfo( "isoqp_association", "relation", STRING ) );
        propToTableAndCol.put( "Association", new MappingInfo( "isoqp_association", "relation", STRING ) );
        //----------------------</common queryable properties>------------------------------------
        //----------------------------------------------------------------------------------------
        
        
        //----------------------------------------------------------------------------------------
        //----------------------<additional common queryable properties>--------------------------
        propToTableAndCol.put( "apiso:language", new MappingInfo( "datasets", "language", STRING ) );
        
        propToTableAndCol.put( "apiso:revisiondate", new MappingInfo( "isoqp_revisiondate", "revisiondate", STRING ) );
        
        propToTableAndCol.put( "apiso:alternatetitle", new MappingInfo( "isoqp_alternatetitle", "alternatetitle", STRING ) );
        
        propToTableAndCol.put( "apiso:creationdate", new MappingInfo( "isoqp_creationdate", "creationdate", STRING ) );

        propToTableAndCol.put( "apiso:publicationdate", new MappingInfo( "isoqp_publicationdate", "publicationdate", STRING ) );

        propToTableAndCol.put( "apiso:organisationname", new MappingInfo( "isoqp_organisationname", "organisationname", STRING ) );

        propToTableAndCol.put( "apiso:hassecurityconstraint", new MappingInfo( "datasets", "hassecurityconstraint", STRING ) );

        propToTableAndCol.put( "apiso:resourceidentifier", new MappingInfo( "isoqp_resourceidentifier", "resourceidentifier", STRING ) );

        propToTableAndCol.put( "apiso:parentidentifier", new MappingInfo( "datasets", "parentidentifier", STRING ) );

        propToTableAndCol.put( "apiso:keywordtype", new MappingInfo( "isoqp_keyword", "keywordType", STRING ) );

        propToTableAndCol.put( "apiso:topiccategory", new MappingInfo( "isoqp_topiccategory", "topiccategory", STRING ) );
        
        propToTableAndCol.put( "apiso:resourcelanguage", new MappingInfo( "datasets", "resourcelanguage", STRING ) );

        propToTableAndCol.put( "apiso:geographicdescriptioncode", new MappingInfo( "isoqp_geographicdescriptioncode", "geographicdescriptioncode", STRING ) );

        propToTableAndCol.put( "apiso:spatialresolution", new MappingInfo( "isoqp_spatialresolution", "id", STRING ) );

        propToTableAndCol.put( "apiso:temporalextent", new MappingInfo( "isoqp_temporalextent", "id", STRING ) );
        
        propToTableAndCol.put( "apiso:servicetype", new MappingInfo( "isoqp_servicetype", "servicetype", STRING ) );

        propToTableAndCol.put( "apiso:servicetypeversion", new MappingInfo( "isoqp_servicetypeversion", "servicetypeversion", STRING ) );

        propToTableAndCol.put( "apiso:operation", new MappingInfo( "isoqp_operation", "operation", STRING ) );

        propToTableAndCol.put( "apiso:operatesondata", new MappingInfo( "isoqp_operatesondata", "id", STRING ) );

        propToTableAndCol.put( "apiso:couplingtype", new MappingInfo( "isoqp_couplingtype", "couplingtype", STRING ) );

        //----------------------</additional common queryable properties>-------------------------
        //----------------------------------------------------------------------------------------
        
        

    }
    
    
    
    /**
     * Handles the {@link Expression} that is identified during the parsing of the {@link Operator}s
     * 
     * @param typeExpression
     * @param exp
     */
    public ExpressionFilterObject expressionFilterHandling( org.deegree.filter.Expression.Type typeExpression, Expression exp ) {
        
        table = new HashSet<String>();

        column = new HashSet<String>();
        
        switch ( typeExpression ) {

        case ADD:
            // TODO
            break;

        case SUB:
            // TODO
            break;

        case MUL:
            // TODO
            break;

        case DIV:
            // TODO
            break;

        case PROPERTY_NAME:
            PropertyName propertyName = (PropertyName) exp;

            for ( String s : propToTableAndCol.keySet() ) {
                if ( propertyName.getPropertyName().equals( s ) ) {
                    MappingInfo m = propToTableAndCol.get( s );
                    propName = propertyName.getAsQName();
                    table.add( m.getTable() );
                    column.add( m.getColumn() );
                    expression = m.getTable() + "." + m.getColumn();

                }
            }
            
            break;

        case LITERAL:
            Literal<?> literal = (Literal<?>) exp;
            String value = "'" + literal.getValue().toString() + "'";
            expression = value;
            break;
        case FUNCTION:
            // TODO
            break;

        }
        return new ExpressionFilterObject(expression, table, column, propName);
        
    }
    
    
    

}
