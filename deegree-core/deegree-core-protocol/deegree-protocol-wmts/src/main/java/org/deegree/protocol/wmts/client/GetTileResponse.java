//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2012 by:
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
package org.deegree.protocol.wmts.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.deegree.commons.utils.net.HttpUtils;
import org.deegree.protocol.ows.client.OWSResponse;
import org.deegree.protocol.ows.exception.OWSExceptionReport;

/**
 * The server response to a WMTS <code>GetTile</code> request.
 * <p>
 * NOTE: The receiver <b>must</b> call {@link #getAsImage()} or {@link #close()} eventually, otherwise the HTTP
 * connection will stay open.
 * </p>
 * 
 * @author <a href="mailto:schneider@occamlabs.de">Markus Schneider</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$
 */
public class GetTileResponse {

    private final OWSResponse rawResponse;

    GetTileResponse( OWSResponse rawResponse ) {
        this.rawResponse = rawResponse;
    }

    /**
     * Returns the tile as an image.
     * 
     * @return image, never <code>null</code>
     * @throws IOException
     * @throws XMLStreamException
     * @throws OWSExceptionReport
     */
    public BufferedImage getAsImage()
                            throws IOException, OWSExceptionReport, XMLStreamException {

        checkForXmlContentTypeAndExceptionReport();

        BufferedImage image = null;
        try {
            InputStream is = rawResponse.getAsBinaryStream();
            image = HttpUtils.IMAGE.work( is );
        } finally {
            rawResponse.close();
        }
        return image;
    }

    private void checkForXmlContentTypeAndExceptionReport()
                            throws OWSExceptionReport, XMLStreamException {

        HttpResponse httpResponse = rawResponse.getAsHttpResponse();
        Header contentType = httpResponse.getFirstHeader( "Content-Type" );
        if ( contentType != null && contentType.getValue().startsWith( "text/xml" ) ) {
            rawResponse.getAsXMLStream();
        }
    }

    /**
     * Provides access to the raw server response.
     * 
     * @return the raw server response, never <code>null</code>
     */
    public OWSResponse getAsRawResponse() {
        return rawResponse;
    }

    /**
     * 
     * @throws IOException
     */
    public void close()
                            throws IOException {
        rawResponse.close();
    }
}
