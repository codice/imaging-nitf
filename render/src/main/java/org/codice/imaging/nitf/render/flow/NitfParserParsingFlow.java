package org.codice.imaging.nitf.render.flow;

import java.text.ParseException;
import org.codice.imaging.nitf.core.AllDataExtractionParseStrategy;
import org.codice.imaging.nitf.core.HeaderOnlyNitfParseStrategy;
import org.codice.imaging.nitf.core.NitfFileParser;
import org.codice.imaging.nitf.core.NitfReader;
import org.codice.imaging.nitf.core.SlottedNitfParseStrategy;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegmentNitfParseStrategy;
import org.codice.imaging.nitf.core.image.ImageDataExtractionParseStrategy;

/**
 * A builder class that handles parsing.
 */
public class NitfParserParsingFlow {
    private NitfReader nitfReader;

    NitfParserParsingFlow(NitfReader nitfReader) {
        this.nitfReader = nitfReader;
    }

    /**
     * Parses the Nitf using an AllDataExtractionParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     */
    public NitfSegmentsFlow allData() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        return build(parseStrategy);
    }

    /**
     * Parses the Nitf using the HeaderOnlyNitfParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     */
    public NitfSegmentsFlow headerOnly() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new HeaderOnlyNitfParseStrategy();
        return build(parseStrategy);
    }

    /**
     * Parses the Nitf using the DataExtensionSegmentNitfParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     */
    public NitfSegmentsFlow dataExtensionSegment() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new DataExtensionSegmentNitfParseStrategy();
        return build(parseStrategy);
    }

    /**
     * Parses the Nitf using the ImageDataExtractionParseStrategy.
     *
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     */
    public NitfSegmentsFlow imageData() throws ParseException {
        SlottedNitfParseStrategy parseStrategy = new ImageDataExtractionParseStrategy();
        return build(parseStrategy);
    }

    /**
     * Parses the Nitf using the supplied SlottedNitfParseStrategy.
     *
     * @param parseStrategy - The NitfParserStrategy to use for parsing.
     * @return a new NitfSegmentsFlow.
     * @throws ParseException when it's thrown by the parser.
     */
    public NitfSegmentsFlow build(SlottedNitfParseStrategy parseStrategy) throws ParseException {
        NitfFileParser.parse(nitfReader, parseStrategy);
        return new NitfSegmentsFlow(parseStrategy);
    }
}
