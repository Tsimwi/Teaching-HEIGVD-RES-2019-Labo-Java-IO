package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 * <p>
 * Hello\n\World -> 1\tHello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

    private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());
    private int lineCount = 0;
    private int lastChar = -1;

    public FileNumberingFilterWriter(Writer out) {
        super(out);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {

        String ret = "";
        String tmp = str.substring(off, off + len);

        if (lineCount == 0) {
            ret += ++lineCount + "\t";
        }

        for (int i = 0; i < tmp.length(); ++i) {
            if (tmp.charAt(i) != '\n' && tmp.charAt(i) != '\r') {
                ret += tmp.charAt(i);
            }

            if (tmp.charAt(i) == '\n') {
                ret += "\n" + ++lineCount + "\t";
            }

            if (tmp.charAt(i) == '\r' && tmp.charAt(i+1) != '\n') {
                ret += "\r" + ++lineCount + "\t";
            }

            if (tmp.charAt(i) == '\r' && tmp.charAt(i+1) == '\n') {
                ret += tmp.charAt(i);
            }
        }

        out.write(ret);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        char[] cbufup = new char[len + 10];
        Boolean separatorDetected = false;
        int lineNumber = 1;

        cbufup[0] = (char) lineNumber++;
        cbufup[1] = '\t';

        for (int i = off; i < (len + off); ++i) {
            if (separatorDetected) {
                cbufup[i++ - off + 2] = (char) lineNumber++;
                cbufup[i++ - off + 2] = '\t';
                separatorDetected = false;
            }

            cbufup[i - off + 2] = cbuf[i];

            if (cbuf[i] == '\n') {
                separatorDetected = true;
            }
        }

        out.write(cbufup);
    }

    @Override
    public void write(int c) throws IOException {

        if ((lineCount == 0 && lastChar == -1) ||   /* Case 1 : very beginning of file */
                (lastChar == '\n') ||               /* Case 2 : '\n' encountered */
                (lastChar == '\r' && c != '\n')) {  /* Case 3 : '\r' encountered */

            out.write(Character.forDigit(++lineCount, 10));
            out.write('\t');
        }
        out.write(c);
        lastChar = c;
    }
}
