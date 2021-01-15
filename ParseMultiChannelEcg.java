import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.EOFException;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.DirectoryStream;

/**
 * Parse a CardioScout ECG file, and output the ECG channel samples row wise to stdout,
 * in a format suitable for plotting with gnuplot or spreadsheet.
 *
 * The three output columns are: sample counter, channel 1 sample value, channel 2 sample value.
 *
 * Usage: java ParseMultiChannelEcg cardioscout-ecg-file.zip
 *
 * License: SPDX short identifier: MIT
 * License: https://opensource.org/licenses/MIT
 */
public class ParseMultiChannelEcg {

    final static ByteBuffer shortb = ByteBuffer.allocate(2)
        .order(ByteOrder.LITTLE_ENDIAN);

    public static void main(String[] arg) throws IOException {
        OutputStream out = new BufferedOutputStream(System.out, 8192);
        try (ZipFile source = new ZipFile(new File(arg[0]))) {
            parseMultiChannelEcg(source, out);
        }
    }

    public static void parseMultiChannelEcg(ZipFile source, OutputStream target) throws IOException {
        InputStream channel1 = openChannelFile(source, "1");
        InputStream channel2 = openChannelFile(source, "2");

        // Skip channel file header
        channel1.skip(512);
        channel2.skip(512);

        // Parse and output channel data row wise cronologically per sample
        try {
            for (int i = 0;; i++) {
                Short ch1 = readSample(channel1);
                Short ch2 = readSample(channel2);
                target.write(String.format("%s %s %s\n", i, ch1, ch2).getBytes());
                if (i % 8960 == 0) {
                    System.err.print(String.format("%s ", i / 8960));
                    System.err.flush();
                    target.flush();
                }
            }
        } catch (EOFException end) {
            // EOF ok
        }
        System.err.println("");
    }

    public static InputStream openChannelFile(ZipFile source, String channel) throws IOException {
        for (Enumeration<? extends ZipEntry> entries = source.entries(); entries.hasMoreElements();) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().endsWith("." + channel)) {
                return new BufferedInputStream(source.getInputStream(entry));
            }
        }
        throw new RuntimeException("Invalid Cardioscout ECG file. No entry for channel " + channel);
    }

    public static short readSample(InputStream source) throws IOException {
        shortb.clear();
        shortb.put(readByte(source));
        shortb.put(readByte(source));
        return shortb.getShort(0);
    }

    public static byte readByte(InputStream source) throws IOException {
        int b = source.read();
        if (b == -1) {
            throw new EOFException();
        }
        return (byte) b;
    }
}
