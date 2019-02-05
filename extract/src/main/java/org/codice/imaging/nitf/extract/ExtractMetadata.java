package org.codice.imaging.nitf.extract;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.TaggedRecordExtensionHandler;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.fluent.NitfSegmentsFlow;
import org.codice.imaging.nitf.fluent.impl.NitfParserInputFlowImpl;

/** Extract metadata from a NITF as map of strings to strings. */
public class ExtractMetadata {

  private static final ExtractFileHeader EXTRACT_FILE_HEADER =
      new ExtractFileHeader((segment, metadata) -> {});

  private static final Map<String, Function<Tre, Map<String, String>>> TRE_HANDLERS =
      Stream.of(
              new ExtractACFTB(),
              new ExtractAIMIDB(),
              new ExtractCMETAA(),
              new ExtractCSEXRA(),
              new ExtractCSDIDA(),
              new ExtractEXPLTB(),
              new ExtractHISTOA(),
              new ExtractMTIRPB(),
              new ExtractPIAIMC(),
              new ExtractPIAPRD(),
              new ExtractPIATGB(),
              new ExtractSTDIDC())
          .collect(Collectors.toMap(BaseExtract::getTreName, Function.identity()));

  private static final Function<Tre, Map<String, String>> IGNORE = (tre) -> Collections.emptyMap();

  private static final ImageSegmentHandler IMAGE_SEGMENT_HANDLER =
      new ImageSegmentHandler((segment, metadata) -> extractTreMetadata(metadata, segment));

  private static final TextSegmentHandler TEXT_SEGMENT_HANDLER =
      new TextSegmentHandler((segment, metadata) -> {});

  private static final GraphicSegmentHandler GRAPHIC_SEGMENT_HANDLER =
      new GraphicSegmentHandler((segment, metadata) -> {});

  private static final SymbolSegmentHandler SYMBOL_SEGMENT_HANDLER =
      new SymbolSegmentHandler((segment, metadata) -> {});

  private static final LabelSegmentHandler LABEL_SEGMENT_HANDLER =
      new LabelSegmentHandler((segment, metadata) -> {});

  public Map<String, String> extract(InputStream inputStream) throws IOException {
    Map<String, String> metadata = new HashMap<>();
    extract(inputStream, metadata);
    return metadata;
  }

  public void extract(InputStream inputStream, Map<String, String> metadata) throws IOException {
    NitfSegmentsFlow flow;

    try {
      flow = new NitfParserInputFlowImpl().inputStream(inputStream).allData();
    } catch (NitfFormatException e) {
      throw new IOException("Unable to get the nitf segments flow.", e);
    }

    // TODO The metadata is being populated as a side-effect, but it may be better to have each step
    // return a metadata map and then combine them.
    extractFileHeaderMetadata(flow, metadata);

    extractTreMetadata(flow, metadata);

    flow.forEachImageSegment(segment -> IMAGE_SEGMENT_HANDLER.accept(segment, metadata))
        .forEachGraphicSegment(segment -> GRAPHIC_SEGMENT_HANDLER.accept(segment, metadata))
        .forEachTextSegment(segment -> TEXT_SEGMENT_HANDLER.accept(segment, metadata))
        .forEachSymbolSegment(segment -> SYMBOL_SEGMENT_HANDLER.accept(segment, metadata))
        .forEachLabelSegment(segment -> LABEL_SEGMENT_HANDLER.accept(segment, metadata));
  }

  private void extractFileHeaderMetadata(NitfSegmentsFlow flow, Map<String, String> metadata) {
    flow.fileHeader(nitfHeader -> EXTRACT_FILE_HEADER.accept(nitfHeader, metadata));
  }

  private static void extractTreMetadata(NitfSegmentsFlow flow, Map<String, String> metadata) {
    flow.fileHeader(nitfHeader -> extractTreMetadata(metadata, nitfHeader));
  }

  private static void extractTreMetadata(
      Map<String, String> metadata, TaggedRecordExtensionHandler taggedRecordExtensionHandler) {
    taggedRecordExtensionHandler.getTREsRawStructure().getTREs().stream()
        .map(ExtractMetadata::applyTreHandler)
        .forEach(metadata::putAll);
  }

  private static Map<String, String> applyTreHandler(Tre tre) {
    return TRE_HANDLERS.getOrDefault(tre.getName().trim(), IGNORE).apply(tre);
  }
}
