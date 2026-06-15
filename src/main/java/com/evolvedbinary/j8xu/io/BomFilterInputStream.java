/*
 * Copyright © 2024, Evolved Binary Ltd. <tech@evolvedbinary.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.evolvedbinary.j8xu.io;

import com.evolvedbinary.j8xu.BOM;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implements an InputStream that can extract a BOM from the start of the stream
 * for the user by calling {@link #parseBom()}.
 *
 * The BOM is still preserved and returned in reads to this stream.
 */
public class BomFilterInputStream extends InputStream {

    private final InputStream in;

    /**
     * Make be null if {@link #parseBom()} was never called.
     *
     * If {@link #parseBom()} was called it will always have
     * at least length >=1.
     *
     * May end with a -1 value to indicate that {@link InputStream#read()}
     * returned -1 on the underling input stream {@link #in}.
     *
     * Otherwise, contains the BOM bytes, plus possibly one more byte
     * that we may have had to read to be certain of the BOM.
     */
    private @Nullable byte[] buf = null;

    /**
     * This indicates how many bytes have been read from {@link #buf}.
     *
     * When bufRead == buf.length, then all available bytes have been read from buf.
     */
    private int bufRead = 0;

    /**
     * This is set by calls to {@link #parseBom()}.
     */
    private @Nullable BOM bom = null;

    public BomFilterInputStream(final InputStream in) {
        this.in = in;
    }

    /**
     * Read the start of the InputStream and attempt to parse the BOM.
     *
     * @return The BOM, or null if there is no BOM.
     */
    @Nullable public BOM parseBom() throws IOException {
        @Nullable BOM bom = null;

        final int c1 = in.read();

        if (c1 == -1) {
            // No BOM
            this.buf = new byte[] { (byte) c1 };

        } else {
            // possible BOM

            int c2 = -1;
            int c3 = -1;
            int c4 = -1;

            if (c1 == BOM.UTF_8.c1) {
                // possible UTF-8
                c2 = in.read();
                if (c2 == BOM.UTF_8.c2) {
                    // still possible UTF-8
                    c3 = in.read();
                    if (c3 == BOM.UTF_8.c3) {
                        // is UTF-8
                        bom = BOM.UTF_8;
                    }
                    this.buf = new byte[]{(byte) c1, (byte) c2, (byte) c3};

                } else {
                    // Not UTF-8
                    this.buf = new byte[]{(byte) c1, (byte) c2};
                }

            } else if (c1 == BOM.UTF_16_BE.c1) {
                // possible UTF-16-BE
                c2 = in.read();
                if (c2 == BOM.UTF_16_BE.c2) {
                    // is UTF-16-BE
                    bom = BOM.UTF_16_BE;
                }
                this.buf = new byte[]{(byte) c1, (byte) c2};

            } else if (c1 == BOM.UTF_16_LE.c1) {
                // possible UTF-16-LE, or UTF-32-LE
                c2 = in.read();
                if (c2 == BOM.UTF_16_LE.c2) {
                    // still possible UTF-16-LE, or UTF-32-LE
                    c3 = in.read();
                    if (c3 == BOM.UTF_32_LE.c3) {
                        // still possible UTF-32-LE
                        c4 = in.read();
                        if (c4 == BOM.UTF_32_LE.c4) {
                            // is UTF-32-LE
                            bom = BOM.UTF_32_LE;
                        }
                        this.buf = new byte[]{(byte) c1, (byte) c2, (byte) c3, (byte) c4};
                    } else {
                        // is UTF-16-LE
                        bom = BOM.UTF_16_LE;
                        this.buf = new byte[]{(byte) c1, (byte) c2, (byte) c3 };
                    }
                } else {
                    // Not UTF-16-LE and not UTF-32-LE
                    this.buf = new byte[]{(byte) c1, (byte) c2};
                }

            } else if(c1 == BOM.UTF_32_BE.c1) {
                // possible UTF-32-BE
                c2 = in.read();
                if (c2 == BOM.UTF_32_BE.c2) {
                    // still possible UTF-32-BE
                    c3 = in.read();
                    if (c3 == BOM.UTF_32_BE.c3) {
                        // still possible UTF-32-BE
                        c4 = in.read();
                        if (c4 == BOM.UTF_32_BE.c4) {
                            // is UTF-32-BE
                            bom = BOM.UTF_32_BE;
                        }
                        this.buf = new byte[]{(byte) c1, (byte) c2, (byte) c3, (byte) c4};
                    } else {
                        this.buf = new byte[]{(byte) c1, (byte) c2, (byte) c3};
                    }
                } else {
                    // Not UTF-32-BE
                    this.buf = new byte[]{(byte) c1, (byte) c2};
                }

            } else if (c1 == BOM.SCSU.c1) {
                // possible SCSU
                c2 = in.read();
                if (c2 == BOM.SCSU.c2) {
                    // still possible SCSU
                    c3 = in.read();
                    if (c3 == BOM.SCSU.c3) {
                        // is SCSU
                        bom = BOM.SCSU;
                    }
                    this.buf = new byte[]{(byte) c1, (byte) c2, (byte) c3};
                } else {
                    // Not SCSU
                    this.buf = new byte[]{(byte) c1, (byte) c2};
                }

            } else if (c1 == BOM.BOCU_1.c1) {
                // possible BOCU-1
                c2 = in.read();
                if (c2 == BOM.BOCU_1.c2) {
                    // still possible BOCU-1
                    c3 = in.read();
                    if (c3 == BOM.BOCU_1.c3) {
                        // is BOCU-1
                        bom = BOM.BOCU_1;
                    }
                    this.buf = new byte[]{(byte) c1, (byte) c2, (byte) c3};
                } else {
                    // Not BOCU-1
                    this.buf = new byte[]{(byte) c1, (byte) c2};
                }

            } else if (c1 == BOM.GB_18030.c1) {
                // possible GB 18030
                c2 = in.read();
                if (c2 == BOM.GB_18030.c2) {
                    // still possible GB 18030
                    c3 = in.read();
                    if (c3 == BOM.GB_18030.c3) {
                        // still possible GB 18030
                        c4 = in.read();
                        if (c4 == BOM.GB_18030.c4) {
                            // is GB 18030
                            bom = BOM.GB_18030;
                        }
                        this.buf = new byte[]{(byte) c1, (byte) c2, (byte) c3, (byte) c4};
                    } else {
                        this.buf = new byte[]{(byte) c1, (byte) c2, (byte) c3};
                    }
                } else {
                    this.buf = new byte[]{(byte) c1, (byte) c2};
                }

            } else {
                // Not Bom
                this.buf = new byte[]{(byte) c1};
            }
        }

        this.bom = bom;
        return bom;
    }

    @Override
    public int read() throws IOException {
        if (buf != null && bufRead < buf.length) {
            // we have bytes in the buffer we have not yet returned, return a byte from there first.
            return buf[bufRead++];
        }

        // nothing available in the buffer, so behave as a proxy to the underlying input stream
        return in.read();
    }

    @Override
    public int read(final byte[] b, int off, int len) throws IOException {
        int readFromBuf = -1;
        if (buf != null && bufRead < buf.length) {
           // we have bytes in the buffer we have not yet returned, read bytes from there first.
           final int bufAvailable = buf.length - bufRead;
           final int maxReadFromBuf = Math.min(len, bufAvailable);

           // NOTE(AR) we must check if we would read the last byte in buf, and if so, if that byte is -1, and if so we must not read that byte, and we can just return without that byte
           if (bufRead + maxReadFromBuf == buf.length && buf[buf.length - 1] == (byte) -1) {
               readFromBuf = maxReadFromBuf - 1;
           } else {
               readFromBuf = maxReadFromBuf;
           }

           System.arraycopy(buf, bufRead, b, off, readFromBuf);
           bufRead += maxReadFromBuf;

           off += readFromBuf;
           len -= readFromBuf;
        }

        int readFromIn = -1;
        if (len > 0) {
            // we still have bytes to read, try and get them from the underlying input stream
            readFromIn = in.read(b, off, len);
        }

        if (readFromBuf == -1) {
            return readFromIn;
        }

        if (readFromIn == -1) {
            return readFromBuf;
        }

        return readFromBuf + readFromIn;
    }

    @Override
    public long skip(long n) throws IOException {
        long skipFromBuf = 0;
        if (buf != null && bufRead < buf.length) {
            // we have bytes in the buffer we have not yet returned, skip bytes from there first.
            final int bufAvailable = buf.length - bufRead;
            final long maxSkipFromBuf = Math.min(n, bufAvailable);

            // NOTE(AR) we must check if we would skip the last byte in buf, and if so, if that byte is -1
            if (bufRead + maxSkipFromBuf == buf.length && buf[buf.length - 1] == (byte) -1) {
                skipFromBuf = maxSkipFromBuf - 1;
            } else {
                skipFromBuf = maxSkipFromBuf;
            }

            bufRead += maxSkipFromBuf;
            n -= skipFromBuf;
        }

        long skipFromIn = 0;
        if (n > 0) {
            // we still have bytes to skip, try and skip them from the underlying input stream
            skipFromIn = in.skip(n);
        }

        return skipFromBuf + skipFromIn;
    }

    @Override
    public int available() throws IOException {
        int available = 0;
        if (buf != null) {
            // are there bytes in the buffer that we have not yet read
            available = buf.length - bufRead;
        }

        // are there bytes available from the underlying input stream
        available += in.available();

        return available;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    /**
     * Get the buffer length.
     * Only used for Unit Tests.
     *
     * @return the length of the buffer.
     */
    int bufLength() {
        if (buf == null) {
            return 0;
        }
        return buf.length;
    }
}
