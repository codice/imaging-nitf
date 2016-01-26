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
package org.codice.imaging.nitf.core.image;

/**
    Image category (ICAT).
*/
public enum ImageCategory {

    /**
        Unknown format of image category.
        <p>
        This indicates an unknown category, and typically indicates a broken file or
        an error during parsing. This is not a valid value in a NITF file.
    */
    UNKNOWN (""),

    /**
        Visible Imagery.
        <p>
        Visible Imagery in the electromagnetic spectrum that is visible to the human eye,
        usually between .4 and .7 micrometers; this type of imagery is usually captured via digital aerial
        photographs.
    */
    VISUAL ("VIS"),

    /**
        Side Looking Radar.
        <p>
        Side-Looking Radar represents An airborne radar, viewing at right angles to
        the axis of the vehicle, which produces a presentation of terrain or moving targets.
    */
    SIDELOOKINGRADAR ("SL"),

    /**
        Thermal Infrared.
        <p>
        Thermal Infrared is imagery produced by sensing and recording the thermal
        energy emitted or reflected from the objects which are imaged.
    */
    THERMALINFRARED ("TI"),

    /**
        Forward Looking Infrared.
        <p>
        Forward Looking Infrared is an airborne, electro-optical thermal
        imaging device that detects far-infrared energy, converts the energy into an electronic signal, and
        provides a visible image for day or night viewing.
    */
    FLIR ("FL"),

    /**
        Radar.
        <p>
        Radar or Radio Detection and Ranging is imagery produced by recording radar waves
        reflected from a given target surface.
    */
    RADAR ("RD"),

    /**
        Electro-optical.
        <p>
        Electro-Optical sensing systems sense things a film camera cannot see by using a
        wider range of the electromagnetic spectrum.
    */
    ELECTROOPTICAL ("EO"),

    /**
        Optical.
        <p>
        Optical imagery is captured using the principle of a focal plane intersecting an optical axis
        in a film camera.
    */
    OPTICAL ("OP"),

    /**
        High Resolution Radar.
        <p>
        High Resolution Radar which has been attenuated to take advantage of
        maximum pulse length and antenna beamwidth.
    */
    HIGHRESRADAR ("HR"),

    /**
        Hyperspectral.
        <p>
        Hyperspectral imagery or imagery with narrow bandwidth and hundreds of bands;
        compare/contrast with monochromatic, multispectral, and ultraspectral.
    */
    HYPERSPECTRAL ("HS"),

    /**
        Color Frame Photography.
        <p>
        The film or imagery produced by a color camera to produce planimetric
        and topographic maps of the earth’s surface; includes surveying cameras, hand-held camera, and
        most reconnaissance cameras.
    */
    COLOURFRAMEPHOTO ("CP"),

    /**
        Black/White Frame Photography.
        <p>
        The film or imagery produced by a black/white camera to
        produce planimetric and topographic maps of the earth’s surface;
        includes surveying cameras, hand-held camera, and most
        reconnaissance cameras.
    */
    BLACKWHITEFRAMEPHOTO ("BP"),

    /**
        Synthetic Aperture Radar.
        <p>
        Synthetic Aperture Radar is radar which overcomes image resolution
        deficiencies by using a short physical antenna to synthesize the effect of a very large antenna giving
        increased beamwidth.
    */
    SYNTHETICAPERTURERADAR ("SAR"),

    /**
        Synthetic Aperture Radar Radio Hologram.
        <p>
        Radio hologram (initial phase information) from a Synthetic Aperture Radar (SAR) with 13,000 elements/slant range.
    */
    SARRADIOHOLOGRAM ("SARIQ"),

    /**
        Infrared.
        <p>
        That imagery produced as a result of sensing electromagnetic radiation emitted or
        reflected from a given target surface in the infrared position of the electromagnetic spectrum
        (approximately 0.72 to 1,000 microns).
    */
    INFRARED ("IR"),

    /**
        Multispectral.
        <p>
        Multispectral imagery or imagery from an object obtained simultaneously in a
        number of discrete spectral bands.
    */
    MULTISPECTRAL ("MS"),

    /**
        Fingerprints.
        <p>
        Fingerprints used for identification which represent the markings on the inner surface
        of the fingertip, particularly when made with ink.
    */
    FINGERPRINTS ("FP"),

    /**
        Magnetic Resonance Imagery.
        <p>
        Magnetic Resonance Imagery is imagery formed from the response
        of electrons, atoms, molecules, or nuclei to discrete radiation frequencies.
    */
    MAGNETICRESONANCEIMAGERY ("MRI"),

    /**
        X-ray.
        <p>
        A form of electromagnetic radiation, similar to light but of shorter wavelength.
    */
    XRAY ("XRAY"),

    /**
        Computerized Axial Tomography Scan.
        <p>
        Cat Scans represent specialized x-rays of cross-sectional
        images from within the body; used for medical diagnosis.
    */
    CATSCAN ("CAT"),

    /**
        Video.
        <p>
        Video imagery is motion Imagery defined as imaging sensor / systems that generate
        sequential or continuous streaming images at specified temporal rates (normally expressed as frames
        per second).
    */
    VIDEO ("VD"),

    /**
        Barometric Pressure.
    */
    BAROMETRICPRESSURE ("BARO"),

    /**
        Water Current.
    */
    WATERCURRENT ("CURRENT"),

    /**
        Water Depth.
    */
    WATERDEPTH ("DEPTH"),

    /**
        Air Wind Charts.
    */
    AIRWINDCHART ("WIND"),

    /**
        Raster Map.
        <p>
        Raster Maps result from the numerical process that scans contiguous pixel values to
        produce an image representation.
    */
    RASTERMAP ("MAP"),

    /**
        Color Patch.
        <p>
        Color Patch usually accompanied with a Look-up-Table (LUT) to equate colors to an image.
    */
    COLOURPATCH ("PAT"),

    /**
        Legends.
        <p>
        Legends - Textual data that provides reference amplification for images.
    */
    LEGEND ("LEG"),

    /**
        Elevation Model.
        <p>
        A numerical model of the elevations of points on the earth's surface.
    */
    ELEVATIONMODEL ("DTEM"),

    /**
        Matrix Data.
        <p>
        Geometric Data other than terrain and elevation.
    */
    MATRIXDATA ("MATR"),

    /**
        Location Grid.
        <p>
        Location Grid - geolocation of an image within a frame.
    */
    LOCATIONGRID ("LOCG");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    ImageCategory(final String abbreviation) {
        this.textEquivalent = abbreviation;
    }

    /**
        Create image category from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the text equivalent for an image category
        @return the image category enumerated value.
    */
    public static ImageCategory getEnumValue(final String textEquivalent) {
        for (ImageCategory icat : values()) {
            if (textEquivalent.equals(icat.textEquivalent)) {
                return icat;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for an image category.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the text equivalent for an image category.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }
};

