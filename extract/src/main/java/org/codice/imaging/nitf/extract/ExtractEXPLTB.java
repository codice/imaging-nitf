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

class ExtractEXPLTB extends BaseExtract {

  private static final List<String> ATTRIBUTES =
      Arrays.asList(
          "ANGLE_TO_NORTH",
          "ANGLE_TO_NORTH_ACCY",
          "GRAZE_ANG",
          "GRAZE_ANG_ACCY",
          "IPR",
          "MODE",
          "NSAMP",
          "N_SEC",
          "POLAR",
          "PRIME_BE",
          "PRIME_ID",
          "SEQ_NUM",
          "SLOPE_ANG",
          "SQUINT_ANGLE",
          "SQUINT_ANGLE_ACCY");

  ExtractEXPLTB() {
    super("EXPLTB", ATTRIBUTES);
  }
}
