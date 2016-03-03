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
 * The classes in this package provide shared pixel-level reading.
 *
 * IOReaderFunction is a FunctionalInterface. Implementations return the right
 * kind of data for a particular kind of NITF file, taking into account the
 * pixel representation (e.g. Binary, Integer, Real), bits per pixel (nominal
 * and actual) and pixel justification (whether valid bits are left or right
 * justified within nominal bits).
 *
 */

package org.codice.imaging.nitf.render.datareader;
