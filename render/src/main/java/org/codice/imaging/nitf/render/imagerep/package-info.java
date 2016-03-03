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
 * The classes in this package provide image representation handling.
 *
 * The basic concept is to use the ImageRepresentationHandlerFactory to get an
 * instance of the required kind of ImageRepresentationHandler for the image
 * segment. That ImageRepresentationHandler then provides for block-level
 * rendering, taking into account the various image representations (e.g. RGB,
 * multi-band, single-band mono, lookup-table).
 *
 * There are different handler implementations to deal with the various NITF
 * IREP types.
 */

package org.codice.imaging.nitf.render.imagerep;
