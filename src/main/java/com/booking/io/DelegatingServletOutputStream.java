package com.booking.io;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

public class DelegatingServletOutputStream extends ServletOutputStream {

    private final OutputStream targetStream;


    /**
     * Create a DelegatingServletOutputStream for the given target stream.
     * @param targetStream the target stream (never {@code null})
     */
    public DelegatingServletOutputStream(OutputStream targetStream) {
        this.targetStream = targetStream;
    }

    /**
     * Return the underlying target stream (never {@code null}).
     */
    public final OutputStream getTargetStream() {
        return this.targetStream;
    }


    @Override
    public void write(int b) throws IOException {
        this.targetStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        this.targetStream.flush();
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.targetStream.close();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException();
    }

}
