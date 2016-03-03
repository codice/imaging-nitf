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
package org.codice.imaging.nitf.fluent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.codice.imaging.nitf.core.NitfDataSource;
import org.codice.imaging.nitf.core.NitfOutputStreamWriter;

/**
 * Provides methods for writing a NITF to a java.io.File or java.io.OutputStream.  Instances of
 * this class will maintain state for a single OutputStream.  Expected usage is that either
 * outputStream() or file() will be called, not both. Calling them both will have the same effect
 * as calling only the last one.  Calling write() before outputStream() or file() will result in
 * no operation being performed.
 */
public class NitfWriterFlow {
    private OutputStream outputStream;

    /**
     * Sets the OutputStream that a call to write() will write to.  Calling this method multiple
     * times, or calling file() after outputStream() will overwrite the previous value.
     *
     * @param outputStream the OutputStream to write the NITF to.
     * @return this NitfWriterFlow.
     */
    public NitfWriterFlow outputStream(OutputStream outputStream) {
        if (outputStream != null) {
            this.outputStream = outputStream;
        }

        return this;
    }

    /**
     * Sets the OutputStream that a call to write() will write to.  Calling this method multiple
     * times, or calling outputStream() after file() will overwrite the previous value.
     *
     * @param file the File to write the NITF to.
     * @return this NitfWriterFlow.
     */
    public NitfWriterFlow file(File file)  {
        if (file != null) {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                outputStream(new FileOutputStream(file));
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }

        return this;
    }

    /**
     * Writes the NITF to the current OutputStream.  If either outputStream() or file() hasn't
     * been called, no operation will be performed.
     *
     * @param dataSource the NitfDataSource that represents the state of the NITF.
     * @return this NitfWriterFlow.
     */
    public NitfWriterFlow write(NitfDataSource dataSource) {
        if (this.outputStream != null) {
            NitfOutputStreamWriter nitfWriter = new NitfOutputStreamWriter(dataSource, outputStream);
            nitfWriter.write();
        }

        return this;
    }
}
