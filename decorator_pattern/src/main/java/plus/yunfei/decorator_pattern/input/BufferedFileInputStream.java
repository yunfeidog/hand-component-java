package plus.yunfei.decorator_pattern.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

public class BufferedFileInputStream extends InputStream {

    private byte[] buffer = new byte[8192]; // 8KB buffer size
    private int position = -1; // Current position in the buffer
    private int capacity = -1; // Number of bytes read into the buffer

    private final FileInputStream fileInputStream;

    public BufferedFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }


    @Override
    public int read() throws IOException {
        if (canRead()) return readFromBuffer();
        refreshBuffer();
        if (!canRead()) return -1;
        return readFromBuffer();
    }

    private void refreshBuffer() throws IOException {
        // here fileInputStream will read from the file and fill the buffer
        capacity = this.fileInputStream.read(buffer);
        position = 0;
    }


    private int readFromBuffer() {
        // why & 0xFF? because read() returns an int, but we want to return a byte
        // if we return buffer[position] directly, it will return a signed byte, which may be negative
        // so we use & 0xFF to convert it to an unsigned byte
        return buffer[position++] & 0xFF;
    }

    private boolean canRead() {
        if (capacity == -1) return false;
        if (position == capacity) return false;
        return true;
    }

    @Override
    public void close() throws IOException {
        this.fileInputStream.close();
    }
}

class CounterFileInputStream extends InputStream {
    private final InputStream fileInputStream;
    private int readCount;

    public CounterFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
        readCount = 0;
    }

    @Override
    public int read() throws IOException {
        readCount++;
       return fileInputStream.read();
    }

    @Override
    public void close() throws IOException {
        this.fileInputStream.close();
    }

    public int getReadCount() {
        return readCount;
    }
}

class Test {
    public static void main(String[] args) {
        File file = new File("pom.xml");
        long start = Instant.now().toEpochMilli();
        try (CounterFileInputStream fileInputStream = new CounterFileInputStream(new FileInputStream(file))) {
            while (true) {
                int read = fileInputStream.read(); // here we read the file byte by byte which may cause many IO operations
                if (read == -1) break;
            }
            System.out.println(fileInputStream.getReadCount());
            System.out.println("用时：" + (Instant.now().toEpochMilli() - start) + "毫秒");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
