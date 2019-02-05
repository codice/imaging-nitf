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

class ExtractMTIRPB extends BaseExtract {

  private static final List<String> ATTRIBUTES =
      Arrays.asList(
          "ACFT_ALT",
          "ACFT_ALT_UNIT",
          "ACFT_HEADING",
          "ACFT_LOC",
          "COSGRZ",
          "DATIME",
          "MTI_DP",
          "MTI_LR",
          "PATCH_NO",
          "SQUINT_ANGLE",
          "TARGETS",
          "TGT_AMPLITUDE",
          "TGT_CAT",
          "TGT_HEADING",
          "TGT_LOC_ACCY",
          "TGT_SPEED",
          "TGT_VEL_R",
          "WAMTI_BAR_NO",
          "WAMTI_FRAME_NO");

  ExtractMTIRPB() {
    super("MTIRPB", ATTRIBUTES);
  }
}
