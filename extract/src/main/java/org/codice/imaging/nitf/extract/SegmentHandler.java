package org.codice.imaging.nitf.extract;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.codice.imaging.nitf.core.common.DateTime;
import org.codice.imaging.nitf.core.common.TaggedRecordExtensionHandler;

public abstract class SegmentHandler<T extends TaggedRecordExtensionHandler>
    implements BiConsumer<T, Map<String, String>> {

  private Map<String, Function<T, String>> mapping;

  String getPrefix() {
    return prefix;
  }

  private String prefix;

  private BiConsumer<T, Map<String, String>> finalProcessing;

  SegmentHandler(
      String prefix,
      Map<String, Function<T, String>> mapping,
      BiConsumer<T, Map<String, String>> finalProcessing) {
    this.mapping = mapping;
    this.prefix = prefix;
    this.finalProcessing = finalProcessing;
  }

  static Date nitfDate(@Nullable DateTime nitfDateTime) {
    if (nitfDateTime == null || nitfDateTime.getZonedDateTime() == null) {
      return null;
    }

    ZonedDateTime zonedDateTime = nitfDateTime.getZonedDateTime();
    Instant instant = zonedDateTime.toInstant();

    return Date.from(instant);
  }

  @Override
  public final void accept(T t, Map<String, String> metadata) {
    mapping.forEach(
        (name, accessor) -> {
          String value = accessor.apply(t);
          if (StringUtils.isNotBlank(value)) {
            metadata.put(prefix + "." + name, value);
          }
        });

    extraProcessing(t, metadata);

    finalProcessing.accept(t, metadata);
  }

  protected abstract void extraProcessing(T t, Map<String, String> metadata);
}
