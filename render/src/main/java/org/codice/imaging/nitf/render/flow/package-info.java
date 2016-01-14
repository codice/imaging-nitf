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

/**
 * The classes in this package are intended to simplify parsing and processing of Nitf files.  They
 * follow the builder pattern to allow chaining method calls in a declarative style.  The flow
 * begins with a call to NitfParserInputFlow, which creates a NitfParserParsingFlow, then a
 * NitfSegmentsFlow.
 *
 * example:
 *
 *   new NitfParserInputFlow()
 *       .inputStream(getImageINputStream())
 *       .allData()
 *       .fileHeader(header -> System.out.println(header.getIdentifier())
 *       .forEachImage((header, imageInputStream) -> renderImage(header, imageInputStream);
 *
 *
 */

package org.codice.imaging.nitf.render;
