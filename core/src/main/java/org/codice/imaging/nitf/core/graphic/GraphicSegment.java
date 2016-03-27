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
package org.codice.imaging.nitf.core.graphic;

import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.CommonBasicSegment;

/**
 * Common graphic segment parsing functionality.
 */
public interface GraphicSegment extends CommonBasicSegment {
    /**
     Return the graphic name (SNAME).
     <p>
     This field shall contain an alphanumeric name for the graphic.

     @return the graphic name
     */
    String getGraphicName();


    /**
     Return the graphic display level (SDLVL).
     <p>
     From MIL-STD-2500C: "This field shall contain a valid
     value that indicates the graphic display level of the
     graphic relative to other displayed file components in a
     composite display. The valid values are 001 to 999.
     The display level of each displayable file component
     (image or graphic) within a file shall be unique; that is,
     each number from 001 to 999 is the display level of, at
     most, one item. The meaning of display level is
     discussed in paragraph 5.3.3. The graphic or image
     component in the file having the minimum display level
     shall have attachment level 0 (ALVL000) (BCS zeros
     (0x30))."
     <p>
     Note that explanation mixes display level and attachment level.

     @return the display level (integer format).
     */
    int getGraphicDisplayLevel();

    /**
     Return the row part of the graphic location (SLOC).
     <p>
     From MIL-STD-2500C: "The graphics location is specified by
     providing the location of the graphic’s origin point
     relative to the position (location of the CCS, image, or
     graphic to which it is attached. This field shall contain
     the graphic location offset from the ILOC or SLOC value
     of the CCS, image, or graphic to which the graphic is
     attached or from the origin of the CCS when the graphic
     is unattached (SALVL000). A row and column value of
     000 indicates no offset. Positive row and column values
     indicate offsets down and to the right, while negative
     row and column values indicate offsets up and to the left."

     @return the row number for the graphic location.
     */
    int getGraphicLocationRow();

    /**
     Return the column part of the graphic location (SLOC).
     <p>
     From MIL-STD-2500C: "The graphics location is specified by
     providing the location of the graphic’s origin point
     relative to the position (location of the CCS, image, or
     graphic to which it is attached. This field shall contain
     the graphic location offset from the ILOC or SLOC value
     of the CCS, image, or graphic to which the graphic is
     attached or from the origin of the CCS when the graphic
     is unattached (SALVL000). A row and column value of
     000 indicates no offset. Positive row and column values
     indicate offsets down and to the right, while negative
     row and column values indicate offsets up and to the left."

     @return the column number for the graphic location.
     */
    int getGraphicLocationColumn();

    /**
     Return the row part of the first bounding box location (SBND1).
     <p>
     From MIL-STD-2500C: "This field shall contain
     an ordered pair of integers defining a location in
     Cartesian coordinates for use with CGM graphics. It is
     the upper left corner of the bounding box for the CGM
     graphic. See paragraph 5.5.2.1 for a description. The
     format is rrrrrccccc, where rrrrr is the row and ccccc is
     the column offset from ILOC or SLOC value of the item
     to which the graphic is attached. If the graphic is
     unattached (value of the SALVL field is equal to BCS
     zeros (0x30)), rrrrr and ccccc represent offsets from the
     origin of the coordinate system that is common to all
     images and graphics in the file having the value of BCS
     zeros (0x30) in the SALVL field. The range for rrrrr
     and ccccc shall be -9999 to 99999."

     @return the row number for the first bounding box location.
     */
    int getBoundingBox1Row();

    /**
     Return the column part of the first bounding box location (SBND1).
     <p>
     From MIL-STD-2500C: "This field shall contain
     an ordered pair of integers defining a location in
     Cartesian coordinates for use with CGM graphics. It is
     the upper left corner of the bounding box for the CGM
     graphic. See paragraph 5.5.2.1 for a description. The
     format is rrrrrccccc, where rrrrr is the row and ccccc is
     the column offset from ILOC or SLOC value of the item
     to which the graphic is attached. If the graphic is
     unattached (value of the SALVL field is equal to BCS
     zeros (0x30)), rrrrr and ccccc represent offsets from the
     origin of the coordinate system that is common to all
     images and graphics in the file having the value of BCS
     zeros (0x30) in the SALVL field. The range for rrrrr
     and ccccc shall be -9999 to 99999."

     @return the column number for the first bounding box location.
     */
    int getBoundingBox1Column();

    /**
     Return the graphic colour (SCOLOR).

     @return the graphic colour.
     */
    GraphicColour getGraphicColour();

    /**
     Return the row part of the second bounding box location (SBND2).
     <p>
     From MIL-STD-2500C: "This field shall
     contain an ordered pair of integers defining a location in
     Cartesian coordinates for use with CGM graphics. It is
     the lower right corner of the bounding box for the CGM
     graphic. See paragraph 5.5.2.1 for a description. The
     format is rrrrrccccc, where rrrrr is the row and ccccc is
     the column offset from ILOC or SLOC value of the item
     to which the graphic is attached. If the graphic is
     unattached (SALVL field value is BCS zeros(0x30)),
     rrrrr and ccccc represent offsets from the origin of the
     coordinate system that is common to all images and
     graphics in the file having the value of BCS zeros (0x30)
     in the SALVL field. The range for rrrrr and ccccc shall
     be -9999 to 99999."

     @return the row number for the second bounding box location.
     */
    int getBoundingBox2Row();

    /**
     Return the column part of the second bounding box location (SBND2).
     <p>
     From MIL-STD-2500C: "This field shall
     contain an ordered pair of integers defining a location in
     Cartesian coordinates for use with CGM graphics. It is
     the lower right corner of the bounding box for the CGM
     graphic. See paragraph 5.5.2.1 for a description. The
     format is rrrrrccccc, where rrrrr is the row and ccccc is
     the column offset from ILOC or SLOC value of the item
     to which the graphic is attached. If the graphic is
     unattached (SALVL field value is BCS zeros(0x30)),
     rrrrr and ccccc represent offsets from the origin of the
     coordinate system that is common to all images and
     graphics in the file having the value of BCS zeros (0x30)
     in the SALVL field. The range for rrrrr and ccccc shall
     be -9999 to 99999."

     @return the column number for the second bounding box location.
     */
    int getBoundingBox2Column();

    /**
     * Get the data (CGM stream) associated with this segment.
     *
     * You may need to rewind this stream if it has been previously read.
     *
     * @return stream containing the data for this segment.
     */
    ImageInputStream getData();

    /**
     * Set the data (CGM stream) associated with this segment.
     *
     * @param data stream containing data
     */
    void setData(ImageInputStream data);

    /**
     * Get the length of the data for this segment.
     *
     * @return the number of bytes of data for this segment.
     */
    long getDataLength();

    /**
     * Set the length of data for this segment.
     *
     * @param length the number of bytes of data in this segment.
     */
    void setDataLength(final long length);

}
