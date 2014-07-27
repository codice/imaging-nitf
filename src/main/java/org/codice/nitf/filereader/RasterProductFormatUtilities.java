/**
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
 **/
package org.codice.nitf.filereader;

import java.io.InputStream;
import java.text.ParseException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.codice.nitf.filereader.schema.Rpfs;

public class RasterProductFormatUtilities {

    private Rpfs rpfs = null;

    static final int CODE_LENGTH = 2;
    static final int EXPECTED_FILE_SUFFIX_LENGTH = 3;

    public RasterProductFormatUtilities() throws ParseException {
        InputStream is = getClass().getResourceAsStream("/rpf_codes.xml");
        try {
            unmarshal(is);
        } catch (JAXBException e) {
            throw new ParseException("Exception while loading RPF codes XML" + e.getMessage(), 0);
        }
    }

    private void unmarshal(final InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Rpfs.class);
        Unmarshaller u = jc.createUnmarshaller();
        rpfs = (Rpfs) u.unmarshal(inputStream);
    }

    public final String getAbbreviationForFileName(final String fileName) {
        Rpfs.Rpf rpf = getRpfForFileName(fileName);
        if (rpf == null) {
            return null;
        }
        return rpf.getAbbreviation();
    }

    public final String getNameForFileName(final String fileName) {
        Rpfs.Rpf rpf = getRpfForFileName(fileName);
        if (rpf == null) {
            return null;
        }
        return rpf.getName();
    }

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
