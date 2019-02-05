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
import org.codice.imaging.nitf.core.symbol.SymbolSegment;

public class SymbolSegmentHandler extends SegmentHandler<SymbolSegment> {

  private static final Map<String, Function<SymbolSegment, String>> MAPPING =
      new ImmutableMap.Builder<String, Function<SymbolSegment, String>>()
          .put("file-part-type", segment -> "SY")
          .put("SID", SymbolSegment::getIdentifier)
          .put("SNAME", SymbolSegment::getSymbolName)
          .put(
              "SSCLAS", segment -> segment.getSecurityMetadata().getSecurityClassification().name())
          .put("SSCODE", segment -> segment.getSecurityMetadata().getCodewords())
          .put("SSCTLH", segment -> segment.getSecurityMetadata().getControlAndHandling())
          .put("SSREL", segment -> segment.getSecurityMetadata().getReleaseInstructions())
          .put("SSCAUT", segment -> segment.getSecurityMetadata().getClassificationAuthority())
          .put("SSCTLN", segment -> segment.getSecurityMetadata().getSecurityControlNumber())
          .put("SSDWNG", segment -> segment.getSecurityMetadata().getDowngrade())
          .put("SSDEVT", segment -> segment.getSecurityMetadata().getDowngradeEvent())
          .put("STYPE", segment -> segment.getSymbolType().name())
          .put("NLIPS", segemnt -> Integer.toString(segemnt.getNumberOfLinesPerSymbol()))
          .put("NPIXPL", segment -> Integer.toString(segment.getNumberOfPixelsPerLine()))
          .put("NWDTH", segment -> Integer.toString(segment.getLineWidth()))
          .put("NBPP", segment -> Integer.toString(segment.getNumberOfBitsPerPixel()))
          .put("SDLVL", segment -> Integer.toString(segment.getSymbolDisplayLevel()))
          .put("SALVL", segment -> Integer.toString(segment.getAttachmentLevel()))
          .put(
              "SLOC",
              segment ->
                  String.format(
                      "%s,%s", segment.getSymbolLocationRow(), segment.getSymbolLocationColumn()))
          .put(
              "SLOC2",
              segment ->
                  String.format(
                      "%s,%s", segment.getSymbolLocation2Row(), segment.getSymbolLocation2Column()))
          .put("SCOLOR", segment -> segment.getSymbolColour().toString())
          .put("SNUM", SymbolSegment::getSymbolNumber)
          .put("SROT", segment -> Integer.toString(segment.getSymbolRotation()))
          .put("SXSHDL", segment -> Integer.toString(segment.getExtendedHeaderDataOverflow()))
          .build();

  SymbolSegmentHandler(BiConsumer<SymbolSegment, Map<String, String>> finalProcessing) {
    super("symbol", MAPPING, finalProcessing);
  }

  @Override
  protected void extraProcessing(SymbolSegment symbolSegment, Map<String, String> metadata) {
    // empty
  }
}
