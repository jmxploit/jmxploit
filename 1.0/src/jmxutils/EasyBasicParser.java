package jmxutils;

import java.util.ListIterator;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.ParseException;

public class EasyBasicParser extends BasicParser {
  
    protected void processOption(String arg, ListIterator iter) throws ParseException
    {
        try {
            super.processOption(arg, iter);
        } catch (ParseException e) {
            // do nothing
        }
    }
}