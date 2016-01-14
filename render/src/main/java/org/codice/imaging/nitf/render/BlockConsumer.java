package org.codice.imaging.nitf.render;

import java.io.IOException;

@FunctionalInterface
interface BlockConsumer {
    void acccept(int rowIndex, int columnIndex) throws IOException;
}
