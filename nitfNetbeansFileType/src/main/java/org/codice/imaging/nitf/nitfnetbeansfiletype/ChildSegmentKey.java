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
package org.codice.imaging.nitf.nitfnetbeansfiletype;

/**
 *
 */
class ChildSegmentKey {
    private DeferredSegmentParseStrategy parseStrategy;
    private String segmentType;
    private Integer index;


    /**
     * @return the segmentType
     */
    public String getSegmentType() {
        return segmentType;
    }

    /**
     * @param type the segmentType to set
     */
    public void setSegmentType(final String type) {
        segmentType = type;
    }

    /**
     * @return the index (zero base) for this child key within the segment type
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index (zero base) for this child key within the segment type
     */
    public void setIndex(final int i) {
        index = i;
    }

    /**
     * @return the parseStrategy
     */
    public DeferredSegmentParseStrategy getParseStrategy() {
        return parseStrategy;
    }

    /**
     * @param strategy the parseStrategy to set
     */
    public void setParseStrategy(final DeferredSegmentParseStrategy strategy) {
        parseStrategy = strategy;
    }
}
