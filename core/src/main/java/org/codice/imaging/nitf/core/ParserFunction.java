package org.codice.imaging.nitf.core;

import java.text.ParseException;

/**
 * This functional interface accepts a String and returns the type &lt;T&gt;. This allows us to
 * write lambdas to handle specific parsing cases.
 * @param <T> - The type that will be returned when apply() is called.
 */

@FunctionalInterface
public interface ParserFunction<T> {
    /**
     *
     * @param string - the string to be parsed.
     * @return - the parsed object of type &lt;T&gt;.
     * @throws ParseException - when an error is encountered during parsing.
     */
    T apply(String string) throws ParseException;
}
