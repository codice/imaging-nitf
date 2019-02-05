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
import org.codice.imaging.nitf.core.text.TextSegment;

public class TextSegmentHandler extends SegmentHandler<TextSegment> {

  private static final Map<String, Function<TextSegment, String>> MAPPING =
      new ImmutableMap.Builder<String, Function<TextSegment, String>>()
          .put("file-part-type", segment -> "TE")
          .put("TXTALVL", segment -> Integer.toString(segment.getAttachmentLevel()))
          .put("TXTDT", segment -> Long.toString(nitfDate(segment.getTextDateTime()).getTime()))
          .put("TXTITL", TextSegment::getTextTitle)
          .put("TSCLAS", segment -> segment.getSecurityMetadata().getSecurityClassificationSystem())
          .put("TSCLSY", segment -> segment.getSecurityMetadata().getSecurityClassificationSystem())
          .put("TSCODE", segment -> segment.getSecurityMetadata().getCodewords())
          .put("TSCTLH", segment -> segment.getSecurityMetadata().getControlAndHandling())
          .put("TSREL", segment -> segment.getSecurityMetadata().getReleaseInstructions())
          .put("TSDCTP", segment -> segment.getSecurityMetadata().getDeclassificationType())
          .put("TSDCDT", segment -> segment.getSecurityMetadata().getDeclassificationDate())
          .put("TSDCXM", segment -> segment.getSecurityMetadata().getDeclassificationExemption())
          .put("TSDG", segment -> segment.getSecurityMetadata().getDowngrade())
          .put("TSDGDT", segment -> segment.getSecurityMetadata().getDowngradeDate())
          .put("TSCLTX", segment -> segment.getSecurityMetadata().getClassificationText())
          .put("TSCATP", segment -> segment.getSecurityMetadata().getClassificationAuthorityType())
          .put("TSCAUT", segment -> segment.getSecurityMetadata().getClassificationAuthority())
          .put("TSCRSN", segment -> segment.getSecurityMetadata().getClassificationReason())
          .put("TSSRDT", segment -> segment.getSecurityMetadata().getSecuritySourceDate())
          .put("TSCTLN", segment -> segment.getSecurityMetadata().getSecurityControlNumber())
          .put("TXTFMT", segment -> segment.getTextFormat().name())
          .put("TXSHDL", segment -> Integer.toString(segment.getExtendedHeaderDataOverflow()))
          .build();

  TextSegmentHandler(BiConsumer<TextSegment, Map<String, String>> finalProcessing) {
    super("text", MAPPING, finalProcessing);
  }

  @Override
  protected void extraProcessing(TextSegment textSegment, Map<String, String> metadata) {
    // empty
  }
}
