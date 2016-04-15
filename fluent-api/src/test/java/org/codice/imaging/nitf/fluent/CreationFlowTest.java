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
package org.codice.imaging.nitf.fluent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegmentFactory;
import org.codice.imaging.nitf.core.header.NitfHeaderFactory;
import org.codice.imaging.nitf.core.image.ImageBand;
import org.codice.imaging.nitf.core.image.ImageRepresentation;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.ImageSegmentFactory;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.text.TextFormat;
import org.codice.imaging.nitf.core.text.TextSegment;
import org.codice.imaging.nitf.core.text.TextSegmentFactory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for NitfCreationFlow.
 */
public class CreationFlowTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public CreationFlowTest() {
    }

    @Test
    public void createEmptyNitf() throws FileNotFoundException, NitfFormatException {
        final String filename = "empty.ntf";
        new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE))
                .write(filename);
        new NitfParserInputFlow()
                .file(new File(filename))
                .allData()
                .fileHeader(
                        header -> {
                            assertThat(header.getFileTitle(), is(""));
                            assertThat(header.getFileType(), is(FileType.NITF_TWO_ONE));
                            assertThat(header.getOriginatingStationId(), is("Codice"));
                            assertThat(header.getFileSecurityMetadata().getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
                        }
                );
        new File(filename).deleteOnExit();
    }

    @Test
    public void createImageNitf() throws FileNotFoundException, NitfFormatException {
        final String filename = "image.ntf";
        NitfCreationFlow flow = new NitfCreationFlow();
        flow.fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE))
                .imageSegment(() -> {
                    ImageSegment imageSegment = ImageSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
                    return imageSegment;
                })
                .write(filename);

        assertThat(flow.build().getImageSegments().size(), is(1));
        new NitfParserInputFlow()
                .file(new File(filename))
                .allData()
                .fileHeader(header -> {
                    assertThat(header.getFileType(), is(FileType.NITF_TWO_ONE));
                })
                .forEachImageSegment(imageSegment -> {
                    assertThat(imageSegment.getFileType(), is(FileType.NITF_TWO_ONE));
                });

        new File(filename).deleteOnExit();
    }

    @Test
    public void createTwoTextNitf() throws FileNotFoundException, NitfFormatException {
        final String filename = "text.ntf";
        NitfCreationFlow flow = new NitfCreationFlow();
        flow.fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE))
            .textSegment(() -> {
                TextSegment segment = TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
                segment.setTextTitle("Better title");
                segment.setTextFormat(TextFormat.BASICCHARACTERSET);
                return segment;
            })
            .textSegment(() -> {
                TextSegment segment = TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
                segment.setTextTitle("Better title");
                segment.setTextFormat(TextFormat.BASICCHARACTERSET);
                return segment;
            })
            .write(filename);

        assertThat(flow.build().getTextSegments().size(), is(2));

        new NitfParserInputFlow()
                .file(new File(filename))
                .allData()
                .fileHeader(
                        header -> {
                            assertThat(header.getFileTitle(), is(""));
                            assertThat(header.getFileType(), is(FileType.NITF_TWO_ONE));
                            assertThat(header.getOriginatingStationId(), is("Codice"));
                            assertThat(header.getFileSecurityMetadata().getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
                        }
                )
                .forEachTextSegment(
                        textSegment -> {
                            assertThat(textSegment.getSecurityMetadata().getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
                            assertThat(textSegment.getTextTitle(), is("Better title"));
                            assertThat(textSegment.getTextFormat(), is(TextFormat.BASICCHARACTERSET));
                            assertThat(textSegment.getData(), is(""));
                });
        new File(filename).deleteOnExit();
    }

    @Test
    public void createSimpleDESNitf() throws FileNotFoundException, NitfFormatException {
        final String filename = "des.ntf";
        new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE))
                .dataExtensionSegment(() -> DataExtensionSegmentFactory.getDefault(FileType.NITF_TWO_ONE))
                .write(filename);
        new NitfParserInputFlow()
                .file(new File(filename))
                .allData()
                .fileHeader(
                        header -> {
                            assertThat(header.getFileTitle(), is(""));
                            assertThat(header.getFileType(), is(FileType.NITF_TWO_ONE));
                            assertThat(header.getOriginatingStationId(), is("Codice"));
                            assertThat(header.getFileSecurityMetadata().getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
                        }
                )
                .forEachDataExtensionSegment(
                        des -> {
                            assertThat(des.getSecurityMetadata().getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
                            assertThat(des.getDESVersion(), is(0));
                            assertThat(des.getIdentifier().trim(), is(""));
                            assertThat(des.getDataLength(), is(0L));
                            assertThat(des.getOverflowedHeaderType(), is(nullValue()));
                        });
        new File(filename).deleteOnExit();
    }

    @Test
    public void createOverflowDESNitf() throws FileNotFoundException, NitfFormatException {
        final String filename = "overflow.ntf";
        new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE))
                .dataExtensionSegment(() -> DataExtensionSegmentFactory.getOverflow(FileType.NITF_TWO_ONE, "UDHD", 0))
                .write(filename);
        new NitfParserInputFlow()
                .file(new File(filename))
                .allData()
                .fileHeader(
                        header -> {
                            assertThat(header.getFileTitle(), is(""));
                            assertThat(header.getFileType(), is(FileType.NITF_TWO_ONE));
                            assertThat(header.getOriginatingStationId(), is("Codice"));
                            assertThat(header.getFileSecurityMetadata().getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
                            assertThat(header.getUserDefinedHeaderOverflow(), is(1));
                        }
                )
                .forEachDataExtensionSegment(
                        des -> {
                            assertThat(des.getSecurityMetadata().getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
                            assertThat(des.getDESVersion(), is(1));
                            assertThat(des.getIdentifier().trim(), is("TRE_OVERFLOW"));
                            assertThat(des.getDataLength(), is(0L));
                            assertThat(des.getOverflowedHeaderType(), is("UDHD"));
                            assertThat(des.getItemOverflowed(), is(0));
                        });
        new File(filename).deleteOnExit();
    }

    @Test
    public void createOverflowDESNitf20() throws FileNotFoundException, NitfFormatException {
        final String filename = "overflow.ntf";
        new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NITF_TWO_ZERO))
                .dataExtensionSegment(() -> DataExtensionSegmentFactory.getOverflow(FileType.NITF_TWO_ZERO, "XHD", 0))
                .write(filename);
        new NitfParserInputFlow()
                .file(new File(filename))
                .allData()
                .fileHeader(
                        header -> {
                            assertThat(header.getFileTitle(), is(""));
                            assertThat(header.getFileType(), is(FileType.NITF_TWO_ZERO));
                            assertThat(header.getOriginatingStationId(), is("Codice"));
                            assertThat(header.getFileSecurityMetadata().getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
                            assertThat(header.getExtendedHeaderDataOverflow(), is(1));
                        }
                )
                .forEachDataExtensionSegment(
                        des -> {
                            assertThat(des.getSecurityMetadata().getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
                            assertThat(des.getIdentifier().trim(), is("Controlled Extensions"));
                            assertThat(des.getDataLength(), is(0L));
                            assertThat(des.getOverflowedHeaderType(), is("XHD"));
                            assertThat(des.getItemOverflowed(), is(0));
                        });
        new File(filename).deleteOnExit();
    }

    @Test
    public void createTextOverflowDESNitf() throws FileNotFoundException, NitfFormatException {
        final String filename = "overflowText.ntf";
        new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE))
                .textSegment(() -> {
                    TextSegment segment = TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
                    segment.setTextTitle("Better title");
                    segment.setTextFormat(TextFormat.BASICCHARACTERSET);
                    return segment;
                })
                .textSegment(() -> TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE))
                .dataExtensionSegment(() -> DataExtensionSegmentFactory.getOverflow(FileType.NITF_TWO_ONE, "TXSHD", 2))
                .write(filename);

        List<TextSegment> textSegments = new ArrayList<>();
        new NitfParserInputFlow()
                .file(new File(filename))
                .allData()
                .forEachTextSegment(textSegment -> textSegments.add(textSegment))
                .forEachDataExtensionSegment(
                        des -> {
                            assertThat(des.getSecurityMetadata().getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
                            assertThat(des.getDESVersion(), is(1));
                            assertThat(des.getIdentifier().trim(), is("TRE_OVERFLOW"));
                            assertThat(des.getOverflowedHeaderType(), is("TXSHD"));
                            assertThat(des.getItemOverflowed(), is(2));
                        });
        TextSegment firstSegment = textSegments.get(0);
        assertThat(firstSegment, is(notNullValue()));
        assertThat(firstSegment.getExtendedHeaderDataOverflow(), is(0));

        TextSegment secondSegment = textSegments.get(1);
        assertThat(secondSegment, is(notNullValue()));
        assertThat(secondSegment.getExtendedHeaderDataOverflow(), is(1));

        new File(filename).deleteOnExit();
    }

    @Test
    public void createImageOverflowDESNitf() throws IOException, FileNotFoundException, NitfFormatException {
        final String filename = "overflowImage.ntf";
        byte[] bytes = new byte[]{0};
        InputStream is1 = new ByteArrayInputStream(bytes);
        InputStream is2 = new ByteArrayInputStream(bytes);
        ImageBand band = new ImageBand();
        band.setImageRepresentation("M");
        ImageInputStream iis1 = ImageIO.createImageInputStream(is1);
        ImageInputStream iis2 = ImageIO.createImageInputStream(is2);
        new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE))
                .imageSegment(() -> {
                    ImageSegment imageSegment1 = ImageSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
                    imageSegment1.setImageRepresentation(ImageRepresentation.MONOCHROME);
                    imageSegment1.addImageBand(band);
                    imageSegment1.setNumberOfBitsPerPixelPerBand(8);
                    imageSegment1.setNumberOfRows(1);
                    imageSegment1.setNumberOfBlocksPerRow(1);
                    imageSegment1.setNumberOfColumns(1);
                    imageSegment1.setNumberOfBlocksPerColumn(1);
                    imageSegment1.setData(iis1);
                    imageSegment1.setDataLength(1L);
                    return imageSegment1;
                })
                .imageSegment(() -> {
                    ImageSegment imageSegment2 = ImageSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
                    imageSegment2.setImageRepresentation(ImageRepresentation.MONOCHROME);
                    imageSegment2.addImageBand(band);
                    imageSegment2.setNumberOfBitsPerPixelPerBand(8);
                    imageSegment2.setNumberOfColumns(1);
                    imageSegment2.setNumberOfRows(1);
                    imageSegment2.setNumberOfBlocksPerColumn(1);
                    imageSegment2.setNumberOfBlocksPerRow(1);
                    imageSegment2.setData(iis2);
                    imageSegment2.setDataLength(1L);
                    return imageSegment2;
                })
                .dataExtensionSegment(() -> DataExtensionSegmentFactory.getOverflow(FileType.NITF_TWO_ONE, "IXSHD", 2))
                .dataExtensionSegment(() -> DataExtensionSegmentFactory.getOverflow(FileType.NITF_TWO_ONE, "UDID", 2))
                .write(filename);

        List<ImageSegment> imageSegments = new ArrayList<>();
        List<DataExtensionSegment> desList = new ArrayList<>();
        new NitfParserInputFlow()
                .file(new File(filename))
                .allData()
                .forEachImageSegment(imageSegment -> imageSegments.add(imageSegment))
                .forEachDataExtensionSegment(des -> {
                    desList.add(des);
                    assertThat(des.getSecurityMetadata().getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
                    assertThat(des.getDESVersion(), is(1));
                    assertThat(des.getIdentifier().trim(), is("TRE_OVERFLOW"));
                });
        ImageSegment firstSegment = imageSegments.get(0);
        assertThat(firstSegment, is(notNullValue()));

        ImageSegment secondSegment = imageSegments.get(1);
        assertThat(secondSegment, is(notNullValue()));
        assertThat(secondSegment.getUserDefinedHeaderOverflow(), is(2));
        assertThat(secondSegment.getExtendedHeaderDataOverflow(), is(1));

        DataExtensionSegment firstDES = desList.get(0);
        assertThat(firstDES.getOverflowedHeaderType(), is("IXSHD"));
        assertThat(firstDES.getItemOverflowed(), is(2));

        DataExtensionSegment secondDES = desList.get(1);
        assertThat(secondDES.getOverflowedHeaderType(), is("UDID"));
        assertThat(secondDES.getItemOverflowed(), is(2));

        new File(filename).deleteOnExit();
    }

    @Test
    public void buildWithoutHeader() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("NitfCreationFlow.build(): method cannot be called before fileHeader().");
        new NitfCreationFlow().build();
    }

    @Test
    public void addDESNullSupplier() {
        NitfCreationFlow flow = new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NSIF_ONE_ZERO))
                .dataExtensionSegment(null);
        assertThat(flow, is(notNullValue()));
        assertThat(flow.build().getDataExtensionSegments().size(), is(0));
    }

    @Test
    public void addTextSegmentNullSupplier() {
        NitfCreationFlow flow = new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NITF_TWO_ZERO))
                .textSegment(null);
        assertThat(flow, is(notNullValue()));
        assertThat(flow.build().getTextSegments().size(), is(0));
    }

    @Test
    public void addNullDES() {
        NitfCreationFlow flow = new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NSIF_ONE_ZERO))
                .dataExtensionSegment(() -> null);
        assertThat(flow, is(notNullValue()));
    }

    @Test
    public void addMismatchedSegmentTypes() {
        NitfCreationFlow flow = new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NSIF_ONE_ZERO));
        exception.expect(IllegalStateException.class);
        exception.expectMessage("NitfCreationFlow.addSegment(): segment FileType must match header FileType.");
        flow.textSegment(() -> TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE));
    }

    @Test
    public void addUnknownSegmentType() {
        NitfCreationFlow flow = new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NSIF_ONE_ZERO));
        exception.expect(IllegalStateException.class);
        exception.expectMessage("NitfCreationFlow.addSegment(): segment FileType must match header FileType.");
        flow.imageSegment(() -> ImageSegmentFactory.getDefault(FileType.UNKNOWN));
    }

    @Test
    public void addMismatchedDES() {
        NitfCreationFlow flow = new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NITF_TWO_ZERO));
        exception.expect(IllegalStateException.class);
        exception.expectMessage("NitfCreationFlow.addSegment(): segment FileType must match header FileType.");
        flow.dataExtensionSegment(() -> DataExtensionSegmentFactory.getDefault(FileType.NITF_TWO_ONE));
    }
}
