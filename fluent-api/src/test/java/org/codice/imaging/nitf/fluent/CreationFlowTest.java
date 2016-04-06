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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegmentFactory;
import org.codice.imaging.nitf.core.header.NitfHeaderFactory;
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
        assertThat(new File(filename).delete(), is(true));
    }

    @Test
    public void createTwoTextNitf() throws FileNotFoundException, NitfFormatException {
        final String filename = "text.ntf";
        new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE))
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
        assertThat(new File(filename).delete(), is(true));
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
        assertThat(new File(filename).delete(), is(true));
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
        assertThat(new File(filename).delete(), is(true));
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
        assertThat(new File(filename).delete(), is(true));
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

        assertThat(new File(filename).delete(), is(true));
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
    }

    @Test
    public void addNullDES() {
        NitfCreationFlow flow = new NitfCreationFlow()
                .fileHeader(() -> NitfHeaderFactory.getDefault(FileType.NSIF_ONE_ZERO))
                .dataExtensionSegment(() -> null);
        assertThat(flow, is(notNullValue()));
    }
}
