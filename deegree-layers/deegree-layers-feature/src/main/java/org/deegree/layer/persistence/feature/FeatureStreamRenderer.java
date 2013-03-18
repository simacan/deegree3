//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2012 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -
 and
 - Occam Labs UG (haftungsbeschränkt) -

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

 Occam Labs UG (haftungsbeschränkt)
 Godesberger Allee 139, 53175 Bonn
 Germany

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.layer.persistence.feature;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.LinkedList;
import java.util.List;

import org.deegree.commons.utils.Triple;
import org.deegree.feature.Feature;
import org.deegree.feature.stream.FeatureInputStream;
import org.deegree.filter.XPathEvaluator;
import org.deegree.geometry.Geometry;
import org.deegree.rendering.r2d.Renderer;
import org.deegree.rendering.r2d.TextRenderer;
import org.deegree.rendering.r2d.context.RenderContext;
import org.deegree.style.se.unevaluated.Style;
import org.deegree.style.styling.Styling;
import org.deegree.style.styling.TextStyling;
import org.slf4j.Logger;

/**
 * Responsible for using a renderer to evaluate and render a feature stream.
 * 
 * @author <a href="mailto:schmitz@occamlabs.de">Andreas Schmitz</a>
 * @author last edited by: $Author: stranger $
 * 
 * @version $Revision: $, $Date: $
 */
class FeatureStreamRenderer {

    private static final Logger LOG = getLogger( FeatureStreamRenderer.class );

    private RenderContext context;

    private int maxFeatures;

    private XPathEvaluator<?> evaluator;

    FeatureStreamRenderer( RenderContext context, int maxFeatures, XPathEvaluator<?> evaluator ) {
        this.context = context;
        this.maxFeatures = maxFeatures;
        this.evaluator = evaluator;
    }

    void renderFeatureStream( FeatureInputStream features, Style style ) {
        Renderer renderer = context.getVectorRenderer();
        TextRenderer textRenderer = context.getTextRenderer();

        Style s = style;
        if (s == null) {
            s = new Style();
        }
        List<Feature> featureList = new LinkedList<Feature>();
        boolean firstPass = true;
        for (Style ss : s.toStylesByRule()) { //.filter(gm.getScale(s)))
            Iterable<Feature> it = firstPass ? features : featureList;
            int cnt = 0;
            for (Feature f : it) {
                if (firstPass) {
                    featureList.add(f);
                }
                try {
                    LinkedList<Triple<Styling, LinkedList<Geometry>, String>> evalds = ss.evaluate( f,
                                                                                                       (XPathEvaluator<Feature>) evaluator );
                    for ( Triple<Styling, LinkedList<Geometry>, String> evald : evalds ) {
                        if ( evald.first instanceof TextStyling ) {
                            textRenderer.render( (TextStyling) evald.first, evald.third, evald.second );
                        } else {
                            renderer.render( evald.first, evald.second );
                        }
                    }
                    //render(f, evaluator, ss, renderer, textRenderer, gm.getScale(), resolution);
                } catch (IllegalArgumentException e) {
                    LOG.warn("Unable to render feature, probably a curve had multiple/non-linear segments.");
                    LOG.warn("Error message was: {}", e.getLocalizedMessage());
                    LOG.trace("Stack trace:", e);
                }
                if (maxFeatures > 0 && ++cnt == maxFeatures) {
                    LOG.debug("Reached max features of {} for layer '{}', stopping.", maxFeatures, this);
                    break;
                }
            }
            firstPass = false;
        }
    }

}
