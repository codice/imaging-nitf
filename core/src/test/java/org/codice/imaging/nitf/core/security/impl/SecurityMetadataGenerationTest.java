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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.codice.imaging.nitf.core.security.FileSecurityMetadata;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import org.junit.Test;

/**
 * Check NifSecurityMetadata generation
 */
public class SecurityMetadataGenerationTest {

    public SecurityMetadataGenerationTest() {
    }

    private void checkNITF21andNSIF10(SecurityMetadata defaultSecurityMetadata) {
        assertNotNull(defaultSecurityMetadata);

        checkNitf21SecurityMetadataUnclasAndEmpty(defaultSecurityMetadata);
    }

    private void checkNITF20(SecurityMetadata defaultSecurityMetadata) {
        assertNotNull(defaultSecurityMetadata);
        checkNitf20SecurityMetadataUnclasAndEmpty(defaultSecurityMetadata);
        assertNull(defaultSecurityMetadata.getDowngradeEvent());
    }

    @Test
    public void checkDefaultGeneration() {
        SecurityMetadata defaultSecurityMetadata = SecurityMetadataFactory.getDefaultMetadata(FileType.NITF_TWO_ONE);
        checkNITF21andNSIF10(defaultSecurityMetadata);
    }

    @Test
    public void checkDefaultGenerationNSIF() {
        SecurityMetadata defaultSecurityMetadata = SecurityMetadataFactory.getDefaultMetadata(FileType.NSIF_ONE_ZERO);
        checkNITF21andNSIF10(defaultSecurityMetadata);
    }

    @Test
    public void checkDefaultGenerationNITF20() {
        SecurityMetadata defaultSecurityMetadata = SecurityMetadataFactory.getDefaultMetadata(FileType.NITF_TWO_ZERO);
        checkNITF20(defaultSecurityMetadata);
    }

    @Test
    public void checkFileSecurityMetadataGeneration() {
        FileSecurityMetadata fsm = SecurityMetadataFactory.getDefaultFileSecurityMetadata(FileType.NITF_TWO_ONE);
        assertNotNull(fsm);
        checkNITF21andNSIF10(fsm);
        assertEquals("", fsm.getFileCopyNumber());
        assertEquals("", fsm.getFileNumberOfCopies());
    }

    @Test
    public void checkFileSecurityMetadataGenerationNSIF() {
        FileSecurityMetadata fsm = SecurityMetadataFactory.getDefaultFileSecurityMetadata(FileType.NSIF_ONE_ZERO);
        assertNotNull(fsm);
        checkNITF21andNSIF10(fsm);
        assertEquals("", fsm.getFileCopyNumber());
        assertEquals("", fsm.getFileNumberOfCopies());
    }

    @Test
    public void checkFileSecurityMetadataGenerationNITF20() {
        FileSecurityMetadata fsm = SecurityMetadataFactory.getDefaultFileSecurityMetadata(FileType.NITF_TWO_ZERO);
        assertNotNull(fsm);
        checkNITF20(fsm);
        assertEquals("", fsm.getFileCopyNumber());
        assertEquals("", fsm.getFileNumberOfCopies());
    }
}
