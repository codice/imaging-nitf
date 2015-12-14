package org.codice.imaging.nitf.common.tre;

import java.text.ParseException;
import java.util.List;

/**
 * An interface to represents a Tre.
 */
public interface Tre {
    /**
     Set the metadata prefix format string.

     @param mdPrefix the metadata prefix.
     */
    void setPrefix(String mdPrefix);

    /**
     Return the metadata prefix format string.

     @return the metadata prefix.
     */
    String getPrefix();

    /**
     Set the raw data for this TRE.
     <p>
     This is only used for TREs that we couldn't parse.

     @param treDataRaw the raw bytes for the TRE.
     */
    void setRawData(byte[] treDataRaw);

    /**
     Get the raw data for this TRE.
     <p>
     This is only used for TREs that we couldn't parse.

     @return the raw bytes for the TRE.
     */
    byte[] getRawData();

    /**
     *
     * @return - the Tre name.
     */
    String getName();

    /**
     *
     * @return - a list of TreEntry objects.
     */
    List<TreEntry> getEntries();

    /**
     *
     * @param tagName - a field key.
     * @return the field value.
     * @throws ParseException - when an unexpected value is encountered in the input stream.
     */
    String getFieldValue(String tagName) throws ParseException;

    /**
     *
     * @param additionalParameterDataSets - a String representation of additional parameter data sets.
     * @return a TreEntry.
     * @throws ParseException - when an unexpected value is encountered in the input stream.
     */
    TreEntry getEntry(String additionalParameterDataSets) throws ParseException;
}
