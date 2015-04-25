package com.wizzardo.http.template;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author: moxa
 * Date: 11/24/12
 */
public class RenderResult {
    protected byte[] bytes;
    protected ArrayList<RenderResult> renders;

    public RenderResult() {
    }

    public RenderResult(byte[] bytes) {
        this.bytes = bytes;
    }

    public RenderResult(String s, Charset charset) {
        bytes = s.getBytes(charset);
    }

    public RenderResult(String s) {
        this(s, Charset.defaultCharset());
    }

    public void add(RenderResult r) {
        if (renders == null)
            renders = new ArrayList<>();

        if (bytes != null) {
            renders.add(new RenderResult(bytes));
            bytes = null;
        }

        renders.add(r);
    }

    public RenderResult append(RenderResult r) {
        add(r);
        return this;
    }

    public RenderResult append(String r) {
        add(new RenderResult(r));
        return this;
    }

    public RenderResult append(byte[] r) {
        add(new RenderResult(r));
        return this;
    }

    public byte[] bytes() {
        if (bytes == null)
            compact();

        return bytes;
    }

    public int size() {
        if (bytes != null) {
            return bytes.length;
        }
        int size = 0;
        for (RenderResult r : renders) {
            size += r.size();
        }
        return size;
    }

    public void write(OutputStream out) throws IOException {
        if (bytes != null) {
            out.write(bytes);
        } else {
            for (RenderResult r : renders) {
                r.write(out);
            }
        }
    }

    public RenderResult compact() {
        if (bytes != null)
            return this;

        bytes = new byte[size()];
        int offset = 0;
        for (RenderResult r : renders) {
            System.arraycopy(r.bytes(), 0, bytes, offset, r.size());
            offset += r.size();
        }
        renders = null;
        return this;
    }

    public String toString() {
        return toString(StandardCharsets.UTF_8);
    }

    public String toString(Charset charset) {
        StringBuilder sb = new StringBuilder(size());
        if (bytes != null) {
            sb.append(new String(bytes, charset));
        } else {
            for (RenderResult r : renders) {
                sb.append(r.toString(charset));
            }
        }
        return sb.toString();
    }
}