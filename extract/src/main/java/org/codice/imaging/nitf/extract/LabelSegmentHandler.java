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
import org.codice.imaging.nitf.core.label.LabelSegment;

public class LabelSegmentHandler extends SegmentHandler<LabelSegment> {

  private static final Map<String, Function<LabelSegment, String>> MAPPING =
      new ImmutableMap.Builder<String, Function<LabelSegment, String>>()
          .put("file-part-type", segment -> "LA")
          .put("LID", LabelSegment::getIdentifier)
          .put(
              "LSCLAS", segment -> segment.getSecurityMetadata().getSecurityClassification().name())
          .put("LSCODE", segment -> segment.getSecurityMetadata().getCodewords())
          .put("LSCTLH", segment -> segment.getSecurityMetadata().getControlAndHandling())
          .put("LSREL", segment -> segment.getSecurityMetadata().getReleaseInstructions())
          .put("LSCAUT", segment -> segment.getSecurityMetadata().getClassificationAuthority())
          .put("LSCTLN", segment -> segment.getSecurityMetadata().getSecurityControlNumber())
          .put("LSDWNG", segment -> segment.getSecurityMetadata().getDowngrade())
          .put("LSDEVT", segment -> segment.getSecurityMetadata().getDowngradeEvent())
          .put("LCW", segment -> Integer.toString(segment.getLabelCellWidth()))
          .put("LCH", segment -> Integer.toString(segment.getLabelCellHeight()))
          .put("LDLVL", segment -> Integer.toString(segment.getLabelDisplayLevel()))
          .put("LALVL", segment -> Integer.toString(segment.getAttachmentLevel()))
          .put(
              "LLOC",
              segment ->
                  String.format(
                      "%s,%s", segment.getLabelLocationRow(), segment.getLabelLocationColumn()))
          .put("LTC", segment -> segment.getLabelTextColour().toString())
          .put("LBC", segment -> segment.getLabelBackgroundColour().toString())
          .put("LXSHDL", segment -> Integer.toString(segment.getExtendedHeaderDataOverflow()))
          .build();

  LabelSegmentHandler(BiConsumer<LabelSegment, Map<String, String>> finalProcessing) {
    super("label", MAPPING, finalProcessing);
  }

  @Override
  protected void extraProcessing(LabelSegment labelSegment, Map<String, String> metadata) {
    // empty
  }
}
