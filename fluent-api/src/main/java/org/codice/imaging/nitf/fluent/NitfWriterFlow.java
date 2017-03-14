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

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

import org.codice.imaging.nitf.core.DataSource;
import org.codice.imaging.nitf.core.impl.NitfOutputStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides methods for writing a NITF to a java.io.File or java.io.OutputStream.  Instances of
 * this class will maintain state for a single OutputStream.
 */
public class NitfWriterFlow {
    private Supplier<OutputStream> streamSupplier;

    private static final Logger LOGGER = LoggerFactory.getLogger(NitfWriterFlow.class);

    /**
     * Sets the OutputStream that a call to write() will write to.  Calling this method multiple
     * times will overwrite the previous value.
     *
     * @param outputStreamSupplier a Supplier for the OutputStream to write the NITF to.
     * @return this NitfWriterFlow.
     */
    public final NitfWriterFlow outputStream(final Supplier<OutputStream> outputStreamSupplier) {
        if (outputStreamSupplier != null) {
            this.streamSupplier = outputStreamSupplier;
        }

        return this;
    }

    /**
     * Writes the NITF to the current OutputStream.
     * If either outputStream() hasn't been called, no operation will be performed.
     *
     * @param dataSource the DataSource that represents the state of the NITF.
     * @return this NitfWriterFlow.
     */
    public final NitfWriterFlow write(final DataSource dataSource) {
        if (this.streamSupplier != null) {
            try (OutputStream outputStream = streamSupplier.get()) {
                NitfOutputStreamWriter nitfWriter = new NitfOutputStreamWriter(dataSource,
                        outputStream);
                nitfWriter.write();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        return this;
    }
}
