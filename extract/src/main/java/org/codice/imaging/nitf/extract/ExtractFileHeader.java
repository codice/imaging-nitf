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
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.header.NitfHeader;

class ExtractFileHeader extends SegmentHandler<NitfHeader> {

  private static final String NITF = "NITF";

  private static final String NSIF = "NSIF";

  private static final String TWO_ONE = "2.1";

  private static final String TWO_ZERO = "2.0";

  private static final String ONE_ZERO = "1.0";

  private static final Map<String, Function<NitfHeader, String>> MAPPING =
      new ImmutableMap.Builder<String, Function<NitfHeader, String>>()
          .put("FHDR", segment -> convertFormat(segment.getFileType()))
          .put("FVER", segment -> convertFormatVersion(segment.getFileType()))
          .put("OSTAID", NitfHeader::getOriginatingStationId)
          .put("FTITLE", NitfHeader::getFileTitle)
          .put(
              "FDT",
              segment ->
                  Long.toString(SegmentHandler.nitfDate(segment.getFileDateTime()).getTime()))
          .put(
              "FSCLAS",
              segment ->
                  segment.getFileSecurityMetadata().getSecurityClassification().getTextEquivalent())
          .put(
              "FSCLSY",
              segment -> segment.getFileSecurityMetadata().getSecurityClassificationSystem())
          .put("FSCODE", segment -> segment.getFileSecurityMetadata().getCodewords())
          .put("FSCTLH", segment -> segment.getFileSecurityMetadata().getControlAndHandling())
          .put("FSREL", segment -> segment.getFileSecurityMetadata().getReleaseInstructions())
          .put(
              "FSDCXM", segment -> segment.getFileSecurityMetadata().getDeclassificationExemption())
          .put("FSDCTP", segment -> segment.getFileSecurityMetadata().getDeclassificationType())
          .put("FSDCDT", segment -> segment.getFileSecurityMetadata().getDeclassificationDate())
          .put("FSDG", segment -> segment.getFileSecurityMetadata().getDowngrade())
          .put("FSDGDT", segment -> segment.getFileSecurityMetadata().getDowngradeDate())
          .put("FSCLTX", segment -> segment.getFileSecurityMetadata().getClassificationText())
          .put(
              "FSCATP",
              segment -> segment.getFileSecurityMetadata().getClassificationAuthorityType())
          .put("FSCAUT", segment -> segment.getFileSecurityMetadata().getClassificationAuthority())
          .put("FSCRSN", segment -> segment.getFileSecurityMetadata().getClassificationReason())
          .put("FSSRDT", segment -> segment.getFileSecurityMetadata().getSecuritySourceDate())
          .put("FSCTLN", segment -> segment.getFileSecurityMetadata().getSecurityControlNumber())
          .put("FSCOP", segment -> segment.getFileSecurityMetadata().getFileCopyNumber())
          .put("FSCPYS", segment -> segment.getFileSecurityMetadata().getFileNumberOfCopies())
          .put("FBKGC", segment -> segment.getFileBackgroundColour().toString())
          .put("ONAME", NitfHeader::getOriginatorsName)
          .put("OPHONE", NitfHeader::getOriginatorsPhoneNumber)
          .put("CLEVEL", segment -> Integer.toString(segment.getComplexityLevel()))
          .put("STYPE", NitfHeader::getStandardType)
          .build();

  ExtractFileHeader(BiConsumer<NitfHeader, Map<String, String>> finalProcessing) {
    super("file", MAPPING, finalProcessing);
  }

  @Override
  protected void extraProcessing(NitfHeader nitfHeader, Map<String, String> metadata) {
    // empty
  }

  private static String convertFormat(final FileType format) {
    if (format == FileType.NITF_TWO_ONE || format == FileType.NITF_TWO_ZERO) {
      return NITF;
    } else if (format == FileType.NSIF_ONE_ZERO) {
      return NSIF;
    }

    return "";
  }

  private static String convertFormatVersion(final FileType format) {
    if (format == FileType.NITF_TWO_ONE) {
      return TWO_ONE;
    } else if (format == FileType.NITF_TWO_ZERO) {
      return TWO_ZERO;
    } else if (format == FileType.NSIF_ONE_ZERO) {
      return ONE_ZERO;
    }

    return "";
  }
}
