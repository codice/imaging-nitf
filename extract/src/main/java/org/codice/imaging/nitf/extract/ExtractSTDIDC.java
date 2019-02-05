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

class ExtractSTDIDC extends BaseExtract {

  private static final List<String> ATTRIBUTES =
      Arrays.asList(
          "ACQUISITION_DATE",
          "COUNTRY",
          "END_COLUMN",
          "END_ROW",
          "END_SEGMENT",
          "LOCATION",
          "MISSION",
          "OP_NUM",
          "PASS",
          "REPLAY_REGEN",
          "REPRO_NUM",
          "START_COLUMN",
          "START_ROW",
          "START_SEGMENT",
          "WAC");

  ExtractSTDIDC() {
    super("STDIDC", ATTRIBUTES);
  }
}
