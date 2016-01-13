package org.codice.imaging.nitf.render.flow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;

/**
 * The NitfParserInputFlow represents the start of the builder pattern for the Nitf parser.
 */
public class NitfParserInputFlow {

    /**
     * Begins a Nitf parsing flow using a file as input.
     *
     * @param inputFile - the Nitf file to read from.
     * @return a new NitfParserParsingFlow.
     * @throws FileNotFoundException - when the file doesn't exist.
     */
    public NitfParserParsingFlow file(File inputFile) throws FileNotFoundException {
        NitfInputStreamReader nitfReader = new NitfInputStreamReader(new FileInputStream(inputFile));
        return new NitfParserParsingFlow(nitfReader);
    }

    /**
     *  Begins a Nitf parsing flow using an InputStream as input.
     *
     * @param inputStream - the InputStream to read the Nitf data from.
     * @return a new NitfParserParsingFlow.
     */
    public NitfParserParsingFlow inputStream(InputStream inputStream) {
        NitfInputStreamReader nitfReader = new NitfInputStreamReader(inputStream);
        return new NitfParserParsingFlow(nitfReader);
    }
}
