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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.schema.Rpfs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Utility class for Raster Product Format (RPF) handling.
 */
public class RasterProductFormatUtilities {

    private static final Logger LOG = LoggerFactory.getLogger(RasterProductFormatUtilities.class);

    private static final String RPF_LOAD_ERROR_MESSAGE = "Exception while loading RPF codes XML";

    private Rpfs rpfs = null;

    static final int CODE_LENGTH = 2;

    static final int EXPECTED_FILE_SUFFIX_LENGTH = 3;

    /**
     * Construct utilities class.
     * <p>
     * This performs internal initialisation.
     *
     * @throws NitfFormatException if the internal initialisation fails.
     */
    public RasterProductFormatUtilities() throws NitfFormatException {
        try (InputStream is = getClass().getResourceAsStream("/rpf_codes.xml")) {
            unmarshal(is);
        } catch (JAXBException ex) {
            LOG.warn("JAXBException parsing RPF codes", ex);
            throw new NitfFormatException(RPF_LOAD_ERROR_MESSAGE + ex.getMessage());
        } catch (IOException ex) {
            LOG.warn("IOException parsing RPF codes", ex);
            throw new NitfFormatException(RPF_LOAD_ERROR_MESSAGE + ex.getMessage());
        }
    }

    /**
     * Unmarshal input stream to RPFs list.
     *
     * @param inputStream the input stream to parse.
     * @throws JAXBException if the unmarshalling of the XML fails.
     */
    private void unmarshal(final InputStream inputStream) throws JAXBException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document document = null;
        try {
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setExpandEntityReferences(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(inputStream);
        } catch (ParserConfigurationException e) {
            LOG.debug("Could not set features on {}", dbf);
        } catch (SAXException | IOException e) {
            LOG.warn("Error parsing input. Set log to DEBUG for more information.");
            LOG.debug("Error parsing input. {}", e);
            throw new JAXBException(e);
        }
        JAXBContext jc = JAXBContext.newInstance(Rpfs.class);
        Unmarshaller u = jc.createUnmarshaller();
        rpfs = (Rpfs) u.unmarshal(document);
    }

    /**
     * Return the data series abbreviation for an RPF file name.
     * <p>
     * This encodes MIL-STD-2411-1 Section 5.1.4 data series abbreviations.
     *
     * @param fileName the filename to look-up the abbreviation for.
     * @return standard abbreviation for the data series.
     */
    public final String getAbbreviationForFileName(final String fileName) {
        Rpfs.Rpf rpf = getRpfForFileName(fileName);
        if (rpf == null) {
            return null;
        }
        return rpf.getAbbreviation();
    }

    /**
     * Return the data series name for an RPF file name.
     * <p>
     * This encodes MIL-STD-2411-1 Section 5.1.4 data series names.
     *
     * @param fileName the filename to look-up the name for.
     * @return standard name for the data series.
     */
    public final String getNameForFileName(final String fileName) {
        Rpfs.Rpf rpf = getRpfForFileName(fileName);
        if (rpf == null) {
            return null;
        }
        return rpf.getName();
    }

    /**
     * Find RPF entry corresponding to a file name.
     *
     * @param fileName the filename
     * @return corresponding RPF.
     */
    private Rpfs.Rpf getRpfForFileName(final String fileName) {
        String fileSuffix = null;
        int dotPosition = fileName.lastIndexOf('.');
        if (dotPosition > 0) {
            fileSuffix = fileName.substring(dotPosition + 1);
        }
        if ((fileSuffix == null) || (fileSuffix.length() != EXPECTED_FILE_SUFFIX_LENGTH)) {
            return null;
        }
        String code = fileSuffix.substring(0, CODE_LENGTH);
        for (Rpfs.Rpf rpf : rpfs.getRpf()) {
            if (code.equals(rpf.getCode())) {
                return rpf;
            }
        }
        return null;
    }
}
