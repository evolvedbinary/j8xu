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
package com.evolvedbinary.j8xu;

import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;

/**
 * BOMs (Byte Order Marks)
 */
public enum BOM {
    UTF_8(StandardCharsets.UTF_8,           0xEF, 0xBB, 0xBF),
    UTF_16_BE(StandardCharsets.UTF_16BE,    0xFE, 0xFF),
    UTF_16_LE(StandardCharsets.UTF_16LE,    0xFF, 0xFE),
    UTF_32_BE("UTF-32BE",                   0x00, 0x00, 0xFE, 0xFF),
    UTF_32_LE("UTF-32LE",                   0xFF, 0xFE, 0x00, 0x00),
    SCSU("SCSU",                            0x0E, 0xFE, 0xFF),
    BOCU_1("BOCU-1",                        0xFB, 0xEE, 0x28),
    GB_18030("GB-18030",                    0x84, 0x31, 0x95, 0x33);

    @Nullable private final Charset charset;
    @Nullable private final UnsupportedCharsetException unsupportedCharsetException;
    private final byte[] bomBytes;
    public final int c1;
    public final int c2;
    public final int c3;
    public final int c4;


    BOM(final String charset, final int... bomBytes) {
        @Nullable Charset c = null;
        @Nullable UnsupportedCharsetException uce = null;
        try {
            c = Charset.forName(charset);
        } catch (final UnsupportedCharsetException e) {
            uce = e;
        }

        this.charset = c;
        this.unsupportedCharsetException = uce;
        this.bomBytes = new byte[bomBytes.length];
        for (int i = 0; i < bomBytes.length; i++) {
            this.bomBytes[i] = (byte) bomBytes[i];
        }
        this.c1 = bomBytes[0];
        this.c2 = bomBytes[1];
        if (bomBytes.length >= 3) {
            this.c3 = bomBytes[2];
        } else {
            this.c3 = -1;
        }
        if (bomBytes.length >= 4) {
            this.c4 = bomBytes[3];
        } else {
            this.c4 = -1;
        }
    }

    BOM(final Charset charset, final int... bomBytes) {
        this.charset = charset;
        this.unsupportedCharsetException = null;
        this.bomBytes = new byte[bomBytes.length];
        for (int i = 0; i < bomBytes.length; i++) {
            this.bomBytes[i] = (byte) bomBytes[i];
        }
        this.c1 = bomBytes[0];
        this.c2 = bomBytes[1];
        if (bomBytes.length >= 3) {
            this.c3 = bomBytes[2];
        } else {
            this.c3 = -1;
        }
        if (bomBytes.length >= 4) {
            this.c4 = bomBytes[3];
        } else {
            this.c4 = -1;
        }
    }

    /**
     * Return the bytes of the BOM.
     *
     * @return the bytes making up the BOM.
     */
    public byte[] getBomBytes() {
        return Arrays.copyOf(bomBytes, bomBytes.length);
    }

    /**
     * Get the charset that should be used when this BOM is present.
     *
     * @return the charset relevant to the BOM.
     *
     * @throws UnsupportedCharsetException if no support for the named charset is available in this instance of the JVM.
     */
    public Charset getCharset() throws UnsupportedCharsetException {
        if (unsupportedCharsetException != null) {
            throw unsupportedCharsetException;
        }
        return charset;
    }
}
