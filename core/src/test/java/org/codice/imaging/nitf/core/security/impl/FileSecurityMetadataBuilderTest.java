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
package org.codice.imaging.nitf.core.security.impl;

import static org.codice.imaging.nitf.core.TestUtils.checkNitf20SecurityMetadataUnclasAndEmpty;
import static org.codice.imaging.nitf.core.TestUtils.checkNitf21SecurityMetadataUnclasAndEmpty;

import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.security.FileSecurityMetadata;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Tests for SecurityMetadataBuilder
 */
public class FileSecurityMetadataBuilderTest {

    public FileSecurityMetadataBuilderTest() {
    }

    @Test
    public void checkSecurityMetadataBuilder21() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE);
        builder.securityClassification(SecurityClassification.UNCLASSIFIED);
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        checkNitf21SecurityMetadataUnclasAndEmpty(securityMetadata);
        assertEquals("00000", securityMetadata.getFileCopyNumber());
        assertEquals("00000", securityMetadata.getFileNumberOfCopies());
    }


    @Test
    public void checkSecurityMetadataBuilder21Copy() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE);
        builder.securityClassification(SecurityClassification.UNCLASSIFIED);
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        FileSecurityMetadataBuilder21 builderCopy = FileSecurityMetadataBuilder21.newInstance(securityMetadata);
        FileSecurityMetadata securityMetadata2 = builderCopy.get();
        checkNitf21SecurityMetadataUnclasAndEmpty(securityMetadata2);
        assertEquals("00000", securityMetadata2.getFileCopyNumber());
        assertEquals("00000", securityMetadata2.getFileNumberOfCopies());
    }

    @Test
    public void checkSecurityMetadataBuilder21CopyAll() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE);
        builder.securityClassification(SecurityClassification.RESTRICTED)
                .fileCopyNumber("00001")
                .securityClassificationSystem("AS")
                .downgrade("F")
                .codewords("AB CD EF")
                .releaseInstructions("Only to friends.")
                .controlAndHandling("GH")
                .downgradeDate("20170812")
                .declassificationType("GE")
                .declassificationExemption("X1")
                .securityControlNumber("Bogus2")
                .declassificationDate("20191008")
                .classificationText("Classification text3")
                .classificationAuthorityType("O")
                .fileNumberOfCopies("00002")
                .securitySourceDate("20081230")
                .classificationReason("C")
                .classificationAuthority("Some Person");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        FileSecurityMetadataBuilder21 builderCopy = FileSecurityMetadataBuilder21.newInstance(securityMetadata);
        FileSecurityMetadata securityMetadata2 = builderCopy.get();
        assertEquals(SecurityClassification.RESTRICTED, securityMetadata2.getSecurityClassification());
        assertEquals("AS", securityMetadata2.getSecurityClassificationSystem());
        assertEquals("GH", securityMetadata2.getControlAndHandling());
        assertEquals("Only to friends.", securityMetadata2.getReleaseInstructions());
        assertEquals("GE", securityMetadata2.getDeclassificationType());
        assertEquals("20191008", securityMetadata2.getDeclassificationDate());
        assertEquals("X1", securityMetadata2.getDeclassificationExemption());
        assertEquals("F", securityMetadata2.getDowngrade());
        assertEquals("20170812", securityMetadata2.getDowngradeDate());
        assertEquals("Bogus2", securityMetadata2.getSecurityControlNumber());
        assertEquals("Classification text3", securityMetadata2.getClassificationText());
        assertEquals("O", securityMetadata2.getClassificationAuthorityType());
        assertEquals("Some Person", securityMetadata2.getClassificationAuthority());
        assertEquals("C", securityMetadata2.getClassificationReason());
        assertEquals("20081230", securityMetadata2.getSecuritySourceDate());
        assertEquals("AB CD EF", securityMetadata2.getCodewords());
        assertEquals("00001", securityMetadata2.getFileCopyNumber());
        assertEquals("00002", securityMetadata2.getFileNumberOfCopies());
    }

    @Test
    public void checkSecurityMetadataBuilder20Copy() {
        FileSecurityMetadataBuilder20 builder = FileSecurityMetadataBuilder20.newInstance();
        builder.securityClassification(SecurityClassification.RESTRICTED)
                .fileCopyNumber("00001")
                .codewords("AB CD")
                .controlAndHandling("WITH CARE")
                .downgradeDateOrSpecialCase("999998")
                .releaseInstructions("Great Height")
                .downgradeEvent("XyzA")
                .fileNumberOfCopies("00003")
                .classificationAuthority("SOME AUTH")
                .securityControlNumber("BOGUS");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        FileSecurityMetadataBuilder20 builderCopy = FileSecurityMetadataBuilder20.newInstance(securityMetadata);
        FileSecurityMetadata securityMetadata2 = builderCopy.get();
        assertEquals("AB CD", securityMetadata2.getCodewords());
        assertEquals("WITH CARE", securityMetadata2.getControlAndHandling());
        assertEquals("Great Height", securityMetadata2.getReleaseInstructions());
        assertEquals("SOME AUTH", securityMetadata2.getClassificationAuthority());
        assertEquals("999998", securityMetadata2.getDowngradeDateOrSpecialCase());
        assertEquals("XyzA", securityMetadata2.getDowngradeEvent());

        builderCopy.downgradeEvent("EVENT 2");
        builderCopy.releaseInstructions("Other instruct.");
        FileSecurityMetadata securityMetadata3 = builderCopy.get();

        // Check original is unchanged (immutable)
        assertEquals("AB CD", securityMetadata2.getCodewords());
        assertEquals("WITH CARE", securityMetadata2.getControlAndHandling());
        assertEquals("Great Height", securityMetadata2.getReleaseInstructions());
        assertEquals("SOME AUTH", securityMetadata2.getClassificationAuthority());
        assertEquals("999998", securityMetadata2.getDowngradeDateOrSpecialCase());
        assertEquals("XyzA", securityMetadata2.getDowngradeEvent());
        assertEquals("00001", securityMetadata2.getFileCopyNumber());
        assertEquals("00003", securityMetadata2.getFileNumberOfCopies());

        assertEquals("AB CD", securityMetadata3.getCodewords());
        assertEquals("WITH CARE", securityMetadata3.getControlAndHandling());
        assertEquals("Other instruct.", securityMetadata3.getReleaseInstructions());
        assertEquals("SOME AUTH", securityMetadata3.getClassificationAuthority());
        assertEquals("999998", securityMetadata3.getDowngradeDateOrSpecialCase());
        assertEquals("EVENT 2", securityMetadata3.getDowngradeEvent());
        assertEquals("00001", securityMetadata3.getFileCopyNumber());
        assertEquals("00003", securityMetadata3.getFileNumberOfCopies());
    }

    @Test
    public void checkSecurityMetadataBuilderClassificationSystem() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .securityClassificationSystem("AS");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals(FileType.NITF_TWO_ONE, securityMetadata.getFileType());
        assertEquals("AS", securityMetadata.getSecurityClassificationSystem());
    }

    @Test
    public void checkSecurityMetadataBuilderCodewords() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .codewords("abc123");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("abc123", securityMetadata.getCodewords());
    }

    @Test
    public void checkSecurityMetadataBuilderControlAndHandling() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .controlAndHandling("AZ,BY");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("AZ,BY", securityMetadata.getControlAndHandling());
    }

    @Test
    public void checkSecurityMetadataBuilderReleaseInstructions() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .releaseInstructions("AS CA NZ");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("AS CA NZ", securityMetadata.getReleaseInstructions());
    }

    @Test
    public void checkSecurityMetadataBuilderDeclassificationDate() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .declassificationType("DD")
                .declassificationDate("20201020");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("DD", securityMetadata.getDeclassificationType());
        assertEquals("20201020", securityMetadata.getDeclassificationDate());
    }

    @Test
    public void checkSecurityMetadataBuilderDeclassificationExemption() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .declassificationType("X")
                .declassificationExemption("X251");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("X", securityMetadata.getDeclassificationType());
        assertEquals("X251", securityMetadata.getDeclassificationExemption());
    }

    @Test
    public void checkSecurityMetadataBuilderDowngrade() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .declassificationType("GE")
                .downgrade("R")
                .downgradeDate("20191231")
                .classificationText("Seems OK to me.");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("GE", securityMetadata.getDeclassificationType());
        assertEquals("R", securityMetadata.getDowngrade());
        assertEquals("20191231", securityMetadata.getDowngradeDate());
        assertEquals("Seems OK to me.", securityMetadata.getClassificationText());
    }

    @Test
    public void checkSecurityMetadataBuilderClassificationAuthority() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .classificationAuthorityType("M")
                .classificationAuthority("Derive-Multiple")
                .securitySourceDate("20151001")
                .classificationReason("G");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("M", securityMetadata.getClassificationAuthorityType());
        assertEquals("Derive-Multiple", securityMetadata.getClassificationAuthority());
        assertEquals("20151001", securityMetadata.getSecuritySourceDate());
        assertEquals("G", securityMetadata.getClassificationReason());
    }

    @Test
    public void checkSecurityMetadataBuilderControlNumber() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .securityControlNumber("Obfuscation");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("Obfuscation", securityMetadata.getSecurityControlNumber());
    }

    @Test
    public void checkSecurityMetadataBuilderNSIF() {
        FileSecurityMetadataBuilder21 builder = FileSecurityMetadataBuilder21.newInstance(FileType.NSIF_ONE_ZERO);
        builder.securityClassification(SecurityClassification.UNCLASSIFIED);
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals(FileType.NSIF_ONE_ZERO, securityMetadata.getFileType());
        checkNitf21SecurityMetadataUnclasAndEmpty(securityMetadata);
    }
 
    @Test
    public void checkSecurityMetadataBuilder20() {
        FileSecurityMetadataBuilder20 builder = FileSecurityMetadataBuilder20.newInstance();
        builder.securityClassification(SecurityClassification.UNCLASSIFIED);
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals(FileType.NITF_TWO_ZERO, securityMetadata.getFileType());
        checkNitf20SecurityMetadataUnclasAndEmpty(securityMetadata);
        assertEquals("00000", securityMetadata.getFileCopyNumber());
        assertEquals("00000", securityMetadata.getFileNumberOfCopies());
    }

    @Test
    public void checkSecurityMetadataBuilder20Downgrade() {
        FileSecurityMetadataBuilder20 builder = FileSecurityMetadataBuilder20.newInstance();
        builder.securityClassification(SecurityClassification.UNCLASSIFIED)
                .downgradeDateOrSpecialCase("999998")
                .downgradeEvent("When its good.")
                .fileCopyNumber("3")
                .fileNumberOfCopies("45");
        FileSecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("999998", securityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("When its good.", securityMetadata.getDowngradeEvent());
        checkNitf20SecurityMetadataUnclasAndEmpty(securityMetadata);
        assertEquals("3", securityMetadata.getFileCopyNumber());
        assertEquals("45", securityMetadata.getFileNumberOfCopies());
    }
}
