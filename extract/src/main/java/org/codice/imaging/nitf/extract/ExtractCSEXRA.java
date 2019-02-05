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
package org.codice.imaging.nitf.extract;

import java.util.Arrays;
import java.util.List;

class ExtractCSEXRA extends BaseExtract {

  private static final List<String> ATTRIBUTES =
      Arrays.asList(
          "GRD_COVER",
          "ALONG_SCAN_GSD",
          "ANGLE_TO_NORTH",
          "AZ_OF_OBLIQUITY",
          "A_S_VERT_GSD",
          "CIRCL_ERR",
          "CROSS_SCAN_GSD",
          "C_S_VERT_GSD",
          "DYNAMIC_RANGE",
          "GEO_MEAN_GSD",
          "GEO_MEAN_VERT_GSD",
          "GSD_BETA_ANGLE",
          "LINEAR_ERR",
          "MAX_GSD",
          "NUM_LINES",
          "NUM_SAMPLES",
          "OBLIQUITY_ANGLE",
          "SENSOR",
          "SNOW_DEPTH_CAT",
          "SUN_AZIMUTH",
          "SUN_ELEVATION",
          "TIME_FIRST_LINE_IMAGE",
          "TIME_IMAGE_DURATION");

  ExtractCSEXRA() {
    super("CSEXRA", ATTRIBUTES);
  }
}
