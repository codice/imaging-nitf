/*
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 */
package org.codice.imaging.nitf.extract;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.codice.imaging.nitf.core.common.CommonSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;

public class GraphicSegmentHandler extends SegmentHandler<GraphicSegment> {

  private static final Map<String, Function<GraphicSegment, String>> MAPPING =
      new ImmutableMap.Builder<String, Function<GraphicSegment, String>>()
          .put("file-part-type", segment -> "SY")
          .put("SID", CommonSegment::getIdentifier)
          .put("SNAME", GraphicSegment::getGraphicName)
          .put(
              "SSCLAS", segment -> segment.getSecurityMetadata().getSecurityClassification().name())
          .put("SSCLSY", segment -> segment.getSecurityMetadata().getSecurityClassificationSystem())
          .put("SSCODE", segment -> segment.getSecurityMetadata().getCodewords())
          .put("SSCTLH", segment -> segment.getSecurityMetadata().getControlAndHandling())
          .put("SSREL", segment -> segment.getSecurityMetadata().getReleaseInstructions())
          .put("SSDCTP", segment -> segment.getSecurityMetadata().getDeclassificationType())
          .put("SSDCDT", segment -> segment.getSecurityMetadata().getDeclassificationDate())
          .put("SSDCXM", segment -> segment.getSecurityMetadata().getDeclassificationExemption())
          .put("SSDG", segment -> segment.getSecurityMetadata().getDowngrade())
          .put("SSDGDT", segment -> segment.getSecurityMetadata().getDowngradeDate())
          .put("SSCLTX", segment -> segment.getSecurityMetadata().getClassificationText())
          .put("SSCATP", segment -> segment.getSecurityMetadata().getClassificationAuthorityType())
          .put("SSCAUT", segment -> segment.getSecurityMetadata().getClassificationAuthority())
          .put("SSCRSN", segment -> segment.getSecurityMetadata().getClassificationReason())
          .put("SSSRDT", segment -> segment.getSecurityMetadata().getSecuritySourceDate())
          .put("SSCTLN", segment -> segment.getSecurityMetadata().getSecurityControlNumber())
          .put("SDLVL", segment -> Integer.toString(segment.getGraphicDisplayLevel()))
          .put("SALVL", segment -> Integer.toString(segment.getAttachmentLevel()))
          .put(
              "SLOC",
              segment -> segment.getGraphicLocationRow() + "," + segment.getGraphicLocationColumn())
          .put("SCOLOR", segment -> segment.getGraphicColour().toString())
          .put("SXSHDL", segment -> Integer.toString(segment.getExtendedHeaderDataOverflow()))
          .build();

  GraphicSegmentHandler(BiConsumer<GraphicSegment, Map<String, String>> finalProcessing) {
    super("graphic", MAPPING, finalProcessing);
  }

  @Override
  protected void extraProcessing(GraphicSegment graphicSegment, Map<String, String> metadata) {
    // empty
  }
}
