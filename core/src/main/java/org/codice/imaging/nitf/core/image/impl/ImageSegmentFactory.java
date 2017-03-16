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
package org.codice.imaging.nitf.core.image.impl;

import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.impl.DateTimeImpl;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageCoordinatesRepresentation;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.security.impl.SecurityMetadataFactory;

/**
 * Factory class for creating new ImageSegment instances.
 */
public final class ImageSegmentFactory {

    private ImageSegmentFactory() {
    }

    /**
     * Create a default NITF image segment, without data.
     *
     * Note that this will have 0 size image, which is not valid in a NITF file. You need to set the image
     * characteristics (including size) when you set the data.
     *
     * Note that this will not set an identifier - it will be empty (space filled on write). That may or may not be
     * valid - it is application dependent.
     *
     * @param fileType the type (version) of NITF file this image segment is for
     * @return default image segment, containing no image data.
     */
    public static ImageSegment getDefault(final FileType fileType) {
        ImageSegment imageSegment = new ImageSegmentImpl();
        imageSegment.setFileType(fileType);
        imageSegment.setIdentifier("");
        imageSegment.setAttachmentLevel(0);
        imageSegment.setImageDateTime(DateTimeImpl.getNitfDateTimeForNow());
        imageSegment.setImageTargetId(new TargetIdImpl());
        imageSegment.setSecurityMetadata(SecurityMetadataFactory.getDefaultMetadata(fileType));
        imageSegment.setImageCoordinatesRepresentation(ImageCoordinatesRepresentation.NONE);
        imageSegment.setImageCompression(ImageCompression.NOTCOMPRESSED);
        return imageSegment;
    }
}
