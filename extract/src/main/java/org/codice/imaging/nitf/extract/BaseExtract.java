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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang.StringUtils;
import org.codice.imaging.nitf.core.tre.Tre;

public abstract class BaseExtract implements Function<Tre, Map<String, String>> {

  private final String treName;

  private final List<String> nitfAttributes;

  protected BaseExtract(String treName, List<String> nitfAttributes) {
    this.treName = treName + ".";
    this.nitfAttributes = nitfAttributes;
  }

  @Override
  public final Map<String, String> apply(Tre tre) {

    Map<String, String> metadata = new HashMap<>();

    nitfAttributes.forEach(
        nitfAttribute ->
            Optional.ofNullable(NitfUtils.getTreValue(tre, nitfAttribute))
                .filter(StringUtils::isNotBlank)
                .ifPresent(value -> metadata.put(treName + nitfAttribute, value)));

    return metadata;
  }

  String getTreName() {
    return treName;
  }
}
