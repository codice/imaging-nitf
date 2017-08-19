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
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Tests for SecurityMetadataBuilder
 */
public class SecurityMetadataBuilderTest {

    public SecurityMetadataBuilderTest() {
    }

    @Test
    public void checkSecurityMetadataBuilder21() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE);
        builder.securityClassification(SecurityClassification.UNCLASSIFIED);
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        checkNitf21SecurityMetadataUnclasAndEmpty(securityMetadata);
    }

    @Test
    public void checkSecurityMetadataBuilder21Copy() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE);
        builder.securityClassification(SecurityClassification.UNCLASSIFIED);
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        SecurityMetadataBuilder21 builderCopy = new SecurityMetadataBuilder21(securityMetadata);
        SecurityMetadata securityMetadata2 = builderCopy.get();
        checkNitf21SecurityMetadataUnclasAndEmpty(securityMetadata2);
    }

    @Test
    public void checkSecurityMetadataBuilder21CopyAll() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE);
        builder.securityClassification(SecurityClassification.RESTRICTED)
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
                .securitySourceDate("20081230")
                .classificationReason("C")
                .classificationAuthority("Some Person");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        SecurityMetadataBuilder21 builderCopy = new SecurityMetadataBuilder21(securityMetadata);
        SecurityMetadata securityMetadata2 = builderCopy.get();
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
    }

    @Test
    public void checkSecurityMetadataBuilder20Copy() {
        SecurityMetadataBuilder20 builder = new SecurityMetadataBuilder20();
        builder.securityClassification(SecurityClassification.RESTRICTED)
                .codewords("AB CD")
                .controlAndHandling("WITH CARE")
                .downgradeDateOrSpecialCase("999998")
                .releaseInstructions("Great Height")
                .downgradeEvent("XyzA")
                .classificationAuthority("SOME AUTH")
                .securityControlNumber("BOGUS");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        SecurityMetadataBuilder20 builderCopy = new SecurityMetadataBuilder20(securityMetadata);
        SecurityMetadata securityMetadata2 = builderCopy.get();
        assertEquals("AB CD", securityMetadata2.getCodewords());
        assertEquals("WITH CARE", securityMetadata2.getControlAndHandling());
        assertEquals("Great Height", securityMetadata2.getReleaseInstructions());
        assertEquals("SOME AUTH", securityMetadata2.getClassificationAuthority());
        assertEquals("999998", securityMetadata2.getDowngradeDateOrSpecialCase());
        assertEquals("XyzA", securityMetadata2.getDowngradeEvent());

        builderCopy.downgradeEvent("EVENT 2");
        builderCopy.releaseInstructions("Other instruct.");
        SecurityMetadata securityMetadata3 = builderCopy.get();

        // Check original is unchanged (immutable)
        assertEquals("AB CD", securityMetadata2.getCodewords());
        assertEquals("WITH CARE", securityMetadata2.getControlAndHandling());
        assertEquals("Great Height", securityMetadata2.getReleaseInstructions());
        assertEquals("SOME AUTH", securityMetadata2.getClassificationAuthority());
        assertEquals("999998", securityMetadata2.getDowngradeDateOrSpecialCase());
        assertEquals("XyzA", securityMetadata2.getDowngradeEvent());

        assertEquals("AB CD", securityMetadata3.getCodewords());
        assertEquals("WITH CARE", securityMetadata3.getControlAndHandling());
        assertEquals("Other instruct.", securityMetadata3.getReleaseInstructions());
        assertEquals("SOME AUTH", securityMetadata3.getClassificationAuthority());
        assertEquals("999998", securityMetadata3.getDowngradeDateOrSpecialCase());
        assertEquals("EVENT 2", securityMetadata3.getDowngradeEvent());
    }

    @Test
    public void checkSecurityMetadataBuilderClassificationSystem() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .securityClassificationSystem("AS");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals(FileType.NITF_TWO_ONE, securityMetadata.getFileType());
        assertEquals("AS", securityMetadata.getSecurityClassificationSystem());
    }

    @Test
    public void checkSecurityMetadataBuilderCodewords() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .codewords("abc123");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("abc123", securityMetadata.getCodewords());
    }

    @Test
    public void checkSecurityMetadataBuilderControlAndHandling() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .controlAndHandling("AZ,BY");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("AZ,BY", securityMetadata.getControlAndHandling());
    }

    @Test
    public void checkSecurityMetadataBuilderReleaseInstructions() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .releaseInstructions("AS CA NZ");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("AS CA NZ", securityMetadata.getReleaseInstructions());
    }

    @Test
    public void checkSecurityMetadataBuilderDeclassificationDate() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .declassificationType("DD")
                .declassificationDate("20201020");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("DD", securityMetadata.getDeclassificationType());
        assertEquals("20201020", securityMetadata.getDeclassificationDate());
    }

    @Test
    public void checkSecurityMetadataBuilderDeclassificationExemption() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .declassificationType("X")
                .declassificationExemption("X251");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("X", securityMetadata.getDeclassificationType());
        assertEquals("X251", securityMetadata.getDeclassificationExemption());
    }

    @Test
    public void checkSecurityMetadataBuilderDowngrade() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .declassificationType("GE")
                .downgrade("R")
                .downgradeDate("20191231")
                .classificationText("Seems OK to me.");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("GE", securityMetadata.getDeclassificationType());
        assertEquals("R", securityMetadata.getDowngrade());
        assertEquals("20191231", securityMetadata.getDowngradeDate());
        assertEquals("Seems OK to me.", securityMetadata.getClassificationText());
    }

    @Test
    public void checkSecurityMetadataBuilderClassificationAuthority() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .classificationAuthorityType("M")
                .classificationAuthority("Derive-Multiple")
                .securitySourceDate("20151001")
                .classificationReason("G");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("M", securityMetadata.getClassificationAuthorityType());
        assertEquals("Derive-Multiple", securityMetadata.getClassificationAuthority());
        assertEquals("20151001", securityMetadata.getSecuritySourceDate());
        assertEquals("G", securityMetadata.getClassificationReason());
    }

    @Test
    public void checkSecurityMetadataBuilderControlNumber() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NITF_TWO_ONE)
                .securityClassification(SecurityClassification.UNCLASSIFIED)
                .securityControlNumber("Obfuscation");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("Obfuscation", securityMetadata.getSecurityControlNumber());
    }

    @Test
    public void checkSecurityMetadataBuilderNSIF() {
        SecurityMetadataBuilder21 builder = new SecurityMetadataBuilder21(FileType.NSIF_ONE_ZERO);
        builder.securityClassification(SecurityClassification.UNCLASSIFIED);
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals(FileType.NSIF_ONE_ZERO, securityMetadata.getFileType());
        checkNitf21SecurityMetadataUnclasAndEmpty(securityMetadata);
    }

    @Test
    public void checkSecurityMetadataBuilder20() {
        SecurityMetadataBuilder20 builder = new SecurityMetadataBuilder20();
        builder.securityClassification(SecurityClassification.UNCLASSIFIED);
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals(FileType.NITF_TWO_ZERO, securityMetadata.getFileType());
        checkNitf20SecurityMetadataUnclasAndEmpty(securityMetadata);
    }

    @Test
    public void checkSecurityMetadataBuilder20Downgrade() {
        SecurityMetadataBuilder20 builder = new SecurityMetadataBuilder20();
        builder.securityClassification(SecurityClassification.UNCLASSIFIED)
                .downgradeDateOrSpecialCase("999998")
                .downgradeEvent("When its good.");
        SecurityMetadata securityMetadata = builder.get();
        assertNotNull(securityMetadata);
        assertEquals("999998", securityMetadata.getDowngradeDateOrSpecialCase());
        assertEquals("When its good.", securityMetadata.getDowngradeEvent());
        checkNitf20SecurityMetadataUnclasAndEmpty(securityMetadata);
    }
}
