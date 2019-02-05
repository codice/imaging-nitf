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
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTWriter;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.image.ImageCoordinates;
import org.codice.imaging.nitf.core.image.ImageCoordinatesRepresentation;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.TargetId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageSegmentHandler extends SegmentHandler<ImageSegment> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImageSegmentHandler.class);

  private static final GeometryFactory GEOMETRY_FACTORY =
      new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);

  private static final Map<String, Function<ImageSegment, String>> MAPPING =
      new ImmutableMap.Builder<String, Function<ImageSegment, String>>()
          .put("file-part-type", segment -> "IM")
          .put("IID2", ImageSegment::getImageIdentifier2)
          .put("ISORCE", ImageSegment::getImageSource)
          .put("NROWS", segment -> Long.toString(segment.getNumberOfRows()))
          .put("NCOLS", segment -> Long.toString(segment.getNumberOfColumns()))
          .put("IREP", segment -> segment.getImageRepresentation().getTextEquivalent())
          .put("ICAT", segment -> segment.getImageCategory().getTextEquivalent())
          .put("IC", segment -> segment.getImageCompression().getTextEquivalent())
          .put("TGTID", segment -> StringUtils.trimToNull(getTargetId(segment)))
          .put("COUNTRY", ImageSegmentHandler::getTargetIdCountryCode)
          .put(
              "IDATIM",
              segment ->
                  Long.toString(SegmentHandler.nitfDate(segment.getImageDateTime()).getTime()))
          .put("IID1", ImageSegment::getIdentifier)
          .put(
              "ISCLAS",
              segment ->
                  segment.getSecurityMetadata().getSecurityClassification().getTextEquivalent())
          .put("ISCLSY", segment -> segment.getSecurityMetadata().getSecurityClassificationSystem())
          .put("ISCODE", segment -> segment.getSecurityMetadata().getCodewords())
          .put("ISCTLH", segment -> segment.getSecurityMetadata().getControlAndHandling())
          .put("ISREL", segment -> segment.getSecurityMetadata().getReleaseInstructions())
          .put("ISDCTP", segment -> segment.getSecurityMetadata().getDeclassificationType())
          .put("ISDCDT", segment -> segment.getSecurityMetadata().getDeclassificationDate())
          .put("ISDCXM", segment -> segment.getSecurityMetadata().getDeclassificationExemption())
          .put("ISDG", segment -> segment.getSecurityMetadata().getDowngrade())
          .put("ISDGDT", segment -> segment.getSecurityMetadata().getDowngradeDate())
          .put("ISCLTX", segment -> segment.getSecurityMetadata().getClassificationText())
          .put("ISCATP", segment -> segment.getSecurityMetadata().getClassificationAuthorityType())
          .put("ISCAUT", segment -> segment.getSecurityMetadata().getClassificationAuthority())
          .put("ISCRSN", segment -> segment.getSecurityMetadata().getClassificationReason())
          .put("ISSRDT", segment -> segment.getSecurityMetadata().getSecuritySourceDate())
          .put("ISCTLN", segment -> segment.getSecurityMetadata().getSecurityControlNumber())
          .put("PVTYPE", segment -> segment.getPixelValueType().getTextEquivalent())
          .put("ABPP", segment -> Integer.toString(segment.getActualBitsPerPixelPerBand()))
          .put("PJUST", segment -> segment.getPixelJustification().getTextEquivalent())
          .put(
              "ICORDS",
              segment ->
                  segment
                      .getImageCoordinatesRepresentation()
                      .getTextEquivalent(segment.getFileType()))
          .put("NICOM", segment -> Integer.toString(segment.getImageComments().size()))
          .put(
              "ICOM1",
              segment ->
                  !segment.getImageComments().isEmpty() ? segment.getImageComments().get(0) : "")
          .put(
              "ICOM2",
              segment ->
                  segment.getImageComments().size() > 1 ? segment.getImageComments().get(1) : "")
          .put(
              "ICOM3",
              segment ->
                  segment.getImageComments().size() > 2 ? segment.getImageComments().get(2) : "")
          .put(
              "ICOM4",
              segment ->
                  segment.getImageComments().size() > 3 ? segment.getImageComments().get(3) : "")
          .put(
              "ICOM5",
              segment ->
                  segment.getImageComments().size() > 4 ? segment.getImageComments().get(4) : "")
          .put(
              "ICOM6",
              segment ->
                  segment.getImageComments().size() > 5 ? segment.getImageComments().get(5) : "")
          .put(
              "ICOM7",
              segment ->
                  segment.getImageComments().size() > 6 ? segment.getImageComments().get(6) : "")
          .put(
              "ICOM8",
              segment ->
                  segment.getImageComments().size() > 7 ? segment.getImageComments().get(7) : "")
          .put(
              "ICOM9",
              segment ->
                  segment.getImageComments().size() > 8 ? segment.getImageComments().get(8) : "")
          .put("NBANDS", segment -> Integer.toString(segment.getNumBands()))
          .put("IMODE", segment -> segment.getImageMode().getTextEquivalent())
          .put("NBPR", segment -> Integer.toString(segment.getNumberOfBlocksPerRow()))
          .put("NBPC", segment -> Integer.toString(segment.getNumberOfBlocksPerColumn()))
          .put("NPPBH", segment -> Long.toString(segment.getNumberOfPixelsPerBlockHorizontal()))
          .put("NPPBV", segment -> Long.toString(segment.getNumberOfPixelsPerBlockVertical()))
          .put("NBPP", segment -> Integer.toString(segment.getNumberOfBitsPerPixelPerBand()))
          .put("IDLVL", segment -> Integer.toString(segment.getImageDisplayLevel()))
          .put("IALVL", segment -> Integer.toString(segment.getAttachmentLevel()))
          .put(
              "ILOC",
              segment -> segment.getImageLocationRow() + "," + segment.getImageLocationColumn())
          .put("IMAG", segment -> Double.toString(segment.getImageMagnificationAsDouble()))
          .build();

  ImageSegmentHandler(BiConsumer<ImageSegment, Map<String, String>> finalProcessing) {
    super("image", MAPPING, finalProcessing);
  }

  private static String getTargetId(ImageSegment imageSegment) {
    try {
      final TargetId targetId = imageSegment.getImageTargetId();
      if (targetId != null) {
        final String targetIdValue = targetId.textValue();
        if (StringUtils.isNotBlank(targetIdValue)) {
          return targetIdValue;
        }
      }
    } catch (NitfFormatException e) {
      LOGGER.debug(
          "Unable to get valid target id from image segment id={}", imageSegment.getIdentifier());
    }

    return null;
  }

  private static String getTargetIdCountryCode(ImageSegment imageSegment) {
    final TargetId imageTargetId = imageSegment.getImageTargetId();

    if (imageTargetId != null) {
      final String countryCode = imageTargetId.getCountryCode();

      if (StringUtils.isNotBlank(countryCode)) {
        return countryCode;
      }
    }

    return null;
  }

  @Override
  protected void extraProcessing(ImageSegment segment, Map<String, String> metadata) {
    handleGeometry(segment).ifPresent(wkt -> metadata.put(getPrefix() + ".image-coordinates", wkt));
  }

  private Optional<String> handleGeometry(ImageSegment imageSegmentHeader) {
    ImageCoordinatesRepresentation imageCoordinatesRepresentation =
        imageSegmentHeader.getImageCoordinatesRepresentation();

    switch (imageCoordinatesRepresentation) {
      case MGRS:
      case UTMNORTH:
      case UTMSOUTH:
      case GEOGRAPHIC:
      case DECIMALDEGREES:
        return Optional.of(new WKTWriter().write(getPolygonForSegment(imageSegmentHeader)));
      default:
        LOGGER.debug(
            "Unsupported representation: {}. The NITF will be ingested, but image"
                + " coordinates will not be available.",
            imageCoordinatesRepresentation);
        break;
    }
    return Optional.empty();
  }

  private Polygon getPolygonForSegment(ImageSegment segment) {
    Coordinate[] coords = new Coordinate[5];
    ImageCoordinates imageCoordinates = segment.getImageCoordinates();
    coords[0] =
        new Coordinate(
            imageCoordinates.getCoordinate00().getLongitude(),
            imageCoordinates.getCoordinate00().getLatitude());
    coords[4] = new Coordinate(coords[0]);
    coords[1] =
        new Coordinate(
            imageCoordinates.getCoordinate0MaxCol().getLongitude(),
            imageCoordinates.getCoordinate0MaxCol().getLatitude());
    coords[2] =
        new Coordinate(
            imageCoordinates.getCoordinateMaxRowMaxCol().getLongitude(),
            imageCoordinates.getCoordinateMaxRowMaxCol().getLatitude());
    coords[3] =
        new Coordinate(
            imageCoordinates.getCoordinateMaxRow0().getLongitude(),
            imageCoordinates.getCoordinateMaxRow0().getLatitude());
    LinearRing externalRing = GEOMETRY_FACTORY.createLinearRing(coords);
    return GEOMETRY_FACTORY.createPolygon(externalRing, null);
  }
}
