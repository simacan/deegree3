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
package org.deegree.filter.function.se;

import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static org.deegree.rendering.r2d.se.parser.SymbologyParser.updateOrContinue;
import static org.deegree.rendering.r2d.se.unevaluated.Continuation.SBUPDATER;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.deegree.filter.MatchableObject;
import org.deegree.filter.expression.Function;
import org.deegree.rendering.r2d.se.unevaluated.Continuation;

/**
 * <code>Substring</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class Substring extends Function {

    private StringBuffer value, position, length;

    private Continuation<StringBuffer> valueContn, positionContn, lengthContn;

    /***/
    public Substring() {
        super( "Substring", null );
    }

    @Override
    public Object[] evaluate( MatchableObject f ) {
        StringBuffer sb = new StringBuffer();
        sb.append( value.toString().trim() );
        if ( valueContn != null ) {
            valueContn.evaluate( sb, f );
        }
        String val = sb.toString().trim();

        int pos;
        if ( positionContn != null ) {
            StringBuffer s = new StringBuffer();
            s.append( position );
            positionContn.evaluate( s, f );
            pos = parseInt( s.toString() );
        } else {
            pos = parseInt( position.toString() );
        }
        pos = max( pos - 1, 0 );

        if ( length == null ) {
            return new Object[] { val.substring( pos ) };
        }

        int len;
        if ( lengthContn != null ) {
            StringBuffer s = new StringBuffer();
            s.append( length );
            lengthContn.evaluate( s, f );
            len = parseInt( s.toString() );
        } else {
            len = parseInt( length.toString() );
        }
        int end = pos + len;
        end = min( val.length(), end );

        return new Object[] { val.substring( pos, end ) };
    }

    /**
     * @param in
     * @throws XMLStreamException
     */
    public void parse( XMLStreamReader in )
                            throws XMLStreamException {
        in.require( START_ELEMENT, null, "Substring" );

        position = new StringBuffer( "1" );

        while ( !( in.isEndElement() && in.getLocalName().equals( "Substring" ) ) ) {
            in.nextTag();

            if ( in.getLocalName().equals( "StringValue" ) ) {
                value = new StringBuffer();
                valueContn = updateOrContinue( in, "StringValue", value, SBUPDATER, null );
            }

            if ( in.getLocalName().equals( "Position" ) ) {
                position = new StringBuffer();
                positionContn = updateOrContinue( in, "Position", position, SBUPDATER, null );
            }

            if ( in.getLocalName().equals( "Length" ) ) {
                length = new StringBuffer();
                lengthContn = updateOrContinue( in, "Length", length, SBUPDATER, null );
            }
        }

        in.require( END_ELEMENT, null, "Substring" );
    }

}
