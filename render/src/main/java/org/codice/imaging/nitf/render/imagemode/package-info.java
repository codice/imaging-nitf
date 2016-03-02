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
 * The classes in this package provide image level rendering.
 *
 * The basic concept is to use the ImageModeHandlerFactory to get an instance of
 * the required kind of ImageModeHandler for the image segment. That
 * ImageModeHandler then provides for rendering.
 *
 * There are different handler implementations to deal with the various NITF
 * IMODE types. Since those are so closely related to image blocking, the
 * rendering of individual blocks to make a complete image.
 */

package org.codice.imaging.nitf.render.imagemode;
