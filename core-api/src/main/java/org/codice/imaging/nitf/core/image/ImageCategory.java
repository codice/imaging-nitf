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
     * Visible Motion Imagery.
     * <p>
     * Visible Imagery in the electromagnetic spectrum that is visible to the human eye, usually between .4 and .7
     * micrometers; this type of imagery is usually captured via digital aerial photographs.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    VISUAL_MOTION("VIS.M"),
    /**
        Side Looking Radar.
        <p>
        Side-Looking Radar represents An airborne radar, viewing at right angles to
        the axis of the vehicle, which produces a presentation of terrain or moving targets.
    */
    SIDELOOKINGRADAR ("SL"),
    /**
     * Side Looking Radar Motion Imagery.
     * <p>
     * Side-Looking Radar represents An airborne radar, viewing at right angles to the axis of the vehicle, which
     * produces a presentation of terrain or moving targets.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    SIDELOOKINGRADAR_MOTION("SL.M"),
    /**
        Thermal Infrared.
        <p>
        Thermal Infrared is imagery produced by sensing and recording the thermal
        energy emitted or reflected from the objects which are imaged.
    */
    THERMALINFRARED ("TI"),
    /**
     * Thermal Infrared Motion Imagery.
     * <p>
     * Thermal Infrared is imagery produced by sensing and recording the thermal energy emitted or reflected from the
     * objects which are imaged.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    THERMALINFRARED_MOTION("TI.M"),
    /**
        Forward Looking Infrared.
        <p>
        Forward Looking Infrared is an airborne, electro-optical thermal
        imaging device that detects far-infrared energy, converts the energy into an electronic signal, and
        provides a visible image for day or night viewing.
    */
    FLIR ("FL"),
    /**
     * Forward Looking Infrared Motion Imagery.
     * <p>
     * Forward Looking Infrared is an airborne, electro-optical thermal imaging device that detects far-infrared energy,
     * converts the energy into an electronic signal, and provides a visible image for day or night viewing.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    FLIR_MOTION("FL.M"),
    /**
        Radar.
        <p>
        Radar or Radio Detection and Ranging is imagery produced by recording radar waves
        reflected from a given target surface.
    */
    RADAR ("RD"),
    /**
     * Radar Motion Imagery.
     * <p>
     * Radar or Radio Detection and Ranging is imagery produced by recording radar waves reflected from a given target
     * surface.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    RADAR_MOTION("RD.M"),
    /**
        Electro-optical.
        <p>
        Electro-Optical sensing systems sense things a film camera cannot see by using a
        wider range of the electromagnetic spectrum.
    */
    ELECTROOPTICAL ("EO"),
    /**
     * Electro-optical Motion Imagery.
     * <p>
     * Electro-Optical sensing systems sense things a film camera cannot see by using a wider range of the
     * electromagnetic spectrum.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    ELECTROOPTICAL_MOTION("EO.M"),
    /**
        Optical.
        <p>
        Optical imagery is captured using the principle of a focal plane intersecting an optical axis
        in a film camera.
    */
    OPTICAL ("OP"),
    /**
     * Optical Motion Imagery.
     * <p>
     * Optical imagery is captured using the principle of a focal plane intersecting an optical axis in a film camera.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    OPTICAL_MOTION("OP.M"),
    /**
        High Resolution Radar.
        <p>
        High Resolution Radar which has been attenuated to take advantage of
        maximum pulse length and antenna beamwidth.
    */
    HIGHRESRADAR ("HR"),
    /**
     * High Resolution Radar Motion Imagery.
     * <p>
     * High Resolution Radar which has been attenuated to take advantage of maximum pulse length and antenna beamwidth.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    HIGHRESRADAR_MOTION("HR.M"),
    /**
        Hyperspectral.
        <p>
        Hyperspectral imagery or imagery with narrow bandwidth and hundreds of bands;
        compare/contrast with monochromatic, multispectral, and ultraspectral.
    */
    HYPERSPECTRAL ("HS"),
    /**
     * Hyperspectral Motion Imagery.
     * <p>
     * Hyperspectral imagery or imagery with narrow bandwidth and hundreds of bands; compare/contrast with
     * monochromatic, multispectral, and ultraspectral.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    HYPERSPECTRAL_MOTION("HS.M"),
    /**
        Color Frame Photography.
        <p>
        The film or imagery produced by a color camera to produce planimetric
        and topographic maps of the earth’s surface; includes surveying cameras, hand-held camera, and
        most reconnaissance cameras.
    */
    COLOURFRAMEPHOTO ("CP"),
    /**
     * Color Frame Photography Motion Imagery.
     * <p>
     * The film or imagery produced by a color camera to produce planimetric and topographic maps of the earth’s
     * surface; includes surveying cameras, hand-held camera, and most reconnaissance cameras.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    COLOURFRAMEPHOTO_MOTION("CP.M"),
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
     * Black/White Frame Photography Motion Imagery.
     * <p>
     * The film or imagery produced by a black/white camera to produce planimetric and topographic maps of the earth’s
     * surface; includes surveying cameras, hand-held camera, and most reconnaissance cameras.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    BLACKWHITEFRAMEPHOTO_MOTION("BP.M"),
    /**
        Synthetic Aperture Radar.
        <p>
        Synthetic Aperture Radar is radar which overcomes image resolution
        deficiencies by using a short physical antenna to synthesize the effect of a very large antenna giving
        increased beamwidth.
    */
    SYNTHETICAPERTURERADAR ("SAR"),
    /**
     * Synthetic Aperture Radar Motion Imagery.
     * <p>
     * Synthetic Aperture Radar is radar which overcomes image resolution deficiencies by using a short physical antenna
     * to synthesize the effect of a very large antenna giving increased beamwidth.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    SYNTHETICAPERTURERADAR_MOTION("SAR.M"),
    /**
        Synthetic Aperture Radar Radio Hologram.
        <p>
        Radio hologram (initial phase information) from a Synthetic Aperture Radar (SAR) with 13,000 elements/slant range.
    */
    SARRADIOHOLOGRAM ("SARIQ"),
    /**
     * Synthetic Aperture Radar Radio Hologram Motion Imagery.
     * <p>
     * Radio hologram (initial phase information) from a Synthetic Aperture Radar (SAR) with 13,000 elements/slant
     * range.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    SARRADIOHOLOGRAM_MOTION("SARIQ.M"),
    /**
        Infrared.
        <p>
        That imagery produced as a result of sensing electromagnetic radiation emitted or
        reflected from a given target surface in the infrared position of the electromagnetic spectrum
        (approximately 0.72 to 1,000 microns).
    */
    INFRARED ("IR"),
    /**
     * Infrared Motion Imagery.
     * <p>
     * That imagery produced as a result of sensing electromagnetic radiation emitted or reflected from a given target
     * surface in the infrared position of the electromagnetic spectrum (approximately 0.72 to 1,000 microns).
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    INFRARED_MOTION("IR.M"),
    /**
        Multispectral.
        <p>
        Multispectral imagery or imagery from an object obtained simultaneously in a
        number of discrete spectral bands.
    */
    MULTISPECTRAL ("MS"),
    /**
     * Multispectral Motion Imagery.
     * <p>
     * Multispectral imagery or imagery from an object obtained simultaneously in a number of discrete spectral bands.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    MULTISPECTRAL_MOTION("MS.M"),
    /**
        Fingerprints.
        <p>
        Fingerprints used for identification which represent the markings on the inner surface
        of the fingertip, particularly when made with ink.
    */
    FINGERPRINTS ("FP"),
    /**
     * Fingerprints Motion Imagery.
     * <p>
     * Fingerprints used for identification which represent the markings on the inner surface of the fingertip,
     * particularly when made with ink.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    FINGERPRINTS_MOTION("FP.M"),
    /**
        Magnetic Resonance Imagery.
        <p>
        Magnetic Resonance Imagery is imagery formed from the response
        of electrons, atoms, molecules, or nuclei to discrete radiation frequencies.
    */
    MAGNETICRESONANCEIMAGERY ("MRI"),
    /**
     * Magnetic Resonance Imagery with Motion.
     * <p>
     * Magnetic Resonance Imagery is imagery formed from the response of electrons, atoms, molecules, or nuclei to
     * discrete radiation frequencies.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    MAGNETICRESONANCEIMAGERY_MOTION("MRI.M"),
    /**
        X-ray.
        <p>
        A form of electromagnetic radiation, similar to light but of shorter wavelength.
    */
    XRAY ("XRAY"),
    /**
     * X-ray Motion Imagery.
     * <p>
     * A form of electromagnetic radiation, similar to light but of shorter wavelength.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    XRAY_MOTION("XRAY.M"),
    /**
        Computerized Axial Tomography Scan.
        <p>
        Cat Scans represent specialized x-rays of cross-sectional
        images from within the body; used for medical diagnosis.
    */
    CATSCAN ("CAT"),
    /**
     * Computerized Axial Tomography Scan Motion Imagery.
     * <p>
     * Cat Scans represent specialized x-rays of cross-sectional images from within the body; used for medical
     * diagnosis.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    CATSCAN_MOTION("CAT.M"),
    /**
        Video.
        <p>
        Video imagery is motion Imagery defined as imaging sensor / systems that generate
        sequential or continuous streaming images at specified temporal rates (normally expressed as frames
        per second).
    */
    VIDEO ("VD"),
    /**
     * Video Motion Imagery.
     * <p>
     * Video imagery is motion Imagery defined as imaging sensor / systems that generate sequential or continuous
     * streaming images at specified temporal rates (normally expressed as frames per second).
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    VIDEO_MOTION("VD.M"),
    /**
        Barometric Pressure.
     */
    BAROMETRICPRESSURE ("BARO"),
    /**
     * Barometric Pressure Motion Imagery.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    BAROMETRICPRESSURE_MOTION("BARO.M"),
    /**
        Water Current.
    */
    WATERCURRENT ("CURRENT"),
    /**
     * Water Current Motion Imagery.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    WATERCURRENT_MOTION("CURREN.M"),
    /**
        Water Depth.
    */
    WATERDEPTH ("DEPTH"),
    /**
     * Water Depth Motion Imagery.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    WATERDEPTH_MOTION("DEPTH.M"),
    /**
        Air Wind Charts.
    */
    AIRWINDCHART ("WIND"),
    /**
     * Air Wind Charts Motion Imagery.
     *
     * This is a MIE4NITF extension, and indicates motion imagery
     */
    AIRWINDCHART_MOTION("WIND.M"),
    /**
        Raster Map.
        <p>
        Raster Maps result from the numerical process that scans contiguous pixel values to
        produce an image representation.
    */
    RASTERMAP ("MAP"),
    /**
     * Raster Map Motion Imagery.
     * <p>
     * Raster Maps result from the numerical process that scans contiguous pixel values to produce an image
     * representation.
     *
     * This is a MIE4NITF extension, and indicates motion imagery
     */
    RASTERMAP_MOTION("MAP.M"),
    /**
        Color Patch.
        <p>
        Color Patch usually accompanied with a Look-up-Table (LUT) to equate colors to an image.
    */
    COLOURPATCH ("PAT"),
    /**
     * Color Patch Motion Imagery.
     * <p>
     * Color Patch usually accompanied with a Look-up-Table (LUT) to equate colors to an image.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    COLOURPATCH_MOTION("PAT.M"),
    /**
        Legends.
        <p>
        Legends - Textual data that provides reference amplification for images.
    */
    LEGEND ("LEG"),
    /**
     * Legends Motion Imagery.
     * <p>
     * Legends - Textual data that provides reference amplification for images.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    LEGEND_MOTION("LEG.M"),
    /**
        Elevation Model.
        <p>
        A numerical model of the elevations of points on the earth's surface.
    */
    ELEVATIONMODEL ("DTEM"),
    /**
     * Elevation Model Motion Imagery.
     * <p>
     * A numerical model of the elevations of points on the earth's surface.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    ELEVATIONMODEL_MOTION("DTEM.M"),
    /**
        Matrix Data.
        <p>
        Geometric Data other than terrain and elevation.
    */
    MATRIXDATA ("MATR"),
    /**
     * Matrix Data Motion Imagery.
     * <p>
     * Geometric Data other than terrain and elevation.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    MATRIXDATA_MOTION("MATR.M"),
    /**
        Location Grid.
        <p>
        Location Grid - geolocation of an image within a frame.
    */
    LOCATIONGRID("LOCG"),
    /**
     * Location Grid Motion Imagery.
     * <p>
     * Location Grid - geolocation of an image within a frame.
     *
     * This is a MIE4NITF extension, and indicates motion imagery.
     */
    LOCATIONGRID_MOTION("LOCG.M");

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
            if (icat.textEquivalent.equals(textEquivalent)) {
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

    /**
     * Test whether the image category is motion imagery.
     *
     * Motion Imagery formats are defined in MIE4NITF.
     *
     * @return true if the imagery category indicates motion imagery.
     */
    public boolean isMotionImagery() {
        return this.textEquivalent.endsWith(".M");
    }
}

