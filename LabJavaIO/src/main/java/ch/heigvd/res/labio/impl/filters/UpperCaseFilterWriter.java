package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Olivier Liechti
 */
public class UpperCaseFilterWriter extends FilterWriter {
  
  public UpperCaseFilterWriter(Writer wrappedWriter) {
    super(wrappedWriter);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    /* Writes a portion of a string */
    out.write((str.substring(off, off + len)).toUpperCase());
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    /* Writes a portion of an array of characters */
    char[] cbufup = new char[len];

    for (int i = off; i < (len + off); ++i) {
      cbufup[i-off] = Character.toUpperCase(cbuf[i]);
    }
    out.write(cbufup);
  }

  @Override
  public void write(int c) throws IOException {
    /* Writes a single character */
    out.write(Character.toUpperCase(c));
  }

}
