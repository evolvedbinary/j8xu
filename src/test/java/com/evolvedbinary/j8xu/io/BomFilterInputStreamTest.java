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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BomFilterInputStreamTest {


    @Test
    void inputWithoutBomWithoutParseBom() throws IOException {
        final String inputString = "hello world!";
        final byte[] input = inputString.getBytes(StandardCharsets.UTF_8);

        try (final InputStream baIs = new ByteArrayInputStream(input);
             final InputStream bomIs = new BomFilterInputStream(baIs)) {

            assertEquals(input.length, bomIs.available());
            assertEquals(input.length, baIs.available());

            assertFalse(bomIs.markSupported());
            assertTrue(baIs.markSupported());

            bomIs.mark(100);
            baIs.mark(100);

            assertThrows(IOException.class, bomIs::reset);
            baIs.reset();

            assertEquals(input[0], (byte) bomIs.read());  // h
            assertEquals(input[1], (byte) bomIs.read());  // e
            assertEquals(input[2], (byte) baIs.read());   // l
            assertEquals(input[3], (byte) baIs.read());   // l

            final byte[] buf = new byte[2];
            bomIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, 4, 6), buf);  // o, \s
            bomIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, 6, 8), buf);  // w, o
            baIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, 8, 10), buf);  // r, l

            assertEquals(1, bomIs.skip(1));  // d
            assertEquals(1, bomIs.available());  // !
            assertEquals(1, baIs.available());  // !
            assertEquals(input[input.length - 1], (byte) bomIs.read());  // !

            assertEquals(0, bomIs.available());
            assertEquals(0, baIs.available());

            bomIs.close();
            baIs.close();
        }
    }

    @ParameterizedTest
    @EnumSource(BOM.class)
    void inputWithBomWithoutParseBom(final BOM bom) throws IOException {
        final String inputString = "hello world!";
        final byte[] inputStringBytes = inputString.getBytes(StandardCharsets.UTF_8);
        final byte[] bomBytes = bom.getBomBytes();
        final byte[] input = new byte[bomBytes.length + inputStringBytes.length];
        System.arraycopy(bomBytes, 0, input, 0, bomBytes.length);
        System.arraycopy(inputStringBytes, 0, input, bomBytes.length, inputStringBytes.length);

        try (final InputStream baIs = new ByteArrayInputStream(input);
             final InputStream bomIs = new BomFilterInputStream(baIs)) {

            assertEquals(input.length, bomIs.available());
            assertEquals(input.length, baIs.available());

            assertFalse(bomIs.markSupported());
            assertTrue(baIs.markSupported());

            bomIs.mark(100);
            baIs.mark(100);

            assertThrows(IOException.class, bomIs::reset);
            baIs.reset();

            assertEquals(input[0], (byte) bomIs.read());
            assertEquals(input[1], (byte) bomIs.read());
            assertEquals(input[2], (byte) baIs.read());
            assertEquals(input[3], (byte) baIs.read());

            final byte[] buf = new byte[2];
            bomIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, 4, 6), buf);
            bomIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, 6, 8), buf);
            baIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, 8, 10), buf);

            assertEquals(1 + bomBytes.length, bomIs.skip(1 + bomBytes.length));
            assertEquals(1, bomIs.available());
            assertEquals(1, baIs.available());
            assertEquals(input[input.length - 1], (byte) bomIs.read());

            assertEquals(0, bomIs.available());
            assertEquals(0, baIs.available());

            bomIs.close();
            baIs.close();
        }
    }

    @Test
    void inputWithoutBomWithParseBom() throws IOException {
        final String inputString = "hello world!";
        final byte[] input = inputString.getBytes(StandardCharsets.UTF_8);

        try (final InputStream baIs = new ByteArrayInputStream(input);
             final BomFilterInputStream bomIs = new BomFilterInputStream(baIs)) {

            @Nullable final BOM parsedBom = bomIs.parseBom();
            assertNull(parsedBom);

            assertEquals(input.length, bomIs.available());
            assertEquals(input.length - 1, baIs.available()); // NOTE(AR) minus 1, because 1 byte is in the buf of bomIs

            assertFalse(bomIs.markSupported());
            assertTrue(baIs.markSupported());

            bomIs.mark(100);
            baIs.mark(100);

            assertThrows(IOException.class, bomIs::reset);
            baIs.reset();

            assertEquals(input[0], (byte) bomIs.read());
            assertEquals(input[1], (byte) bomIs.read());
            assertEquals(input[2], (byte) baIs.read());
            assertEquals(input[3], (byte) baIs.read());

            final byte[] buf = new byte[2];
            bomIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, 4, 6), buf);
            bomIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, 6, 8), buf);
            baIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, 8, 10), buf);

            assertEquals(1, bomIs.skip(1));
            assertEquals(1, bomIs.available());
            assertEquals(1, baIs.available());
            assertEquals(input[input.length - 1], (byte) bomIs.read());

            assertEquals(0, bomIs.available());
            assertEquals(0, baIs.available());

            bomIs.close();
            baIs.close();
        }
    }

    @ParameterizedTest
    @EnumSource(BOM.class)
    void inputWithBomWithParseBom(final BOM bom) throws IOException {
        final String inputString = "hello world!";
        final byte[] inputStringBytes = inputString.getBytes(StandardCharsets.UTF_8);
        final byte[] bomBytes = bom.getBomBytes();
        final byte[] input = new byte[bomBytes.length + inputStringBytes.length];
        System.arraycopy(bomBytes, 0, input, 0, bomBytes.length);
        System.arraycopy(inputStringBytes, 0, input, bomBytes.length, inputStringBytes.length);

        try (final InputStream baIs = new ByteArrayInputStream(input);
             final BomFilterInputStream bomIs = new BomFilterInputStream(baIs)) {

            @Nullable final BOM parsedBom = bomIs.parseBom();
            assertNotNull(parsedBom);
            assertEquals(bom, parsedBom);

            assertEquals(input.length, bomIs.available());
            assertEquals(input.length - bomIs.bufLength(), baIs.available());   // NOTE(AR) minus bomBytes, because bomBytes is in the buf of bomIs

            assertFalse(bomIs.markSupported());
            assertTrue(baIs.markSupported());

            bomIs.mark(100);
            baIs.mark(100);

            assertThrows(IOException.class, bomIs::reset);
            baIs.reset();

            int i = 0;
            for (; i < bomIs.bufLength(); i++) {
                assertEquals(input[i], (byte) bomIs.read());
            }
            assertEquals(input[i++], (byte) baIs.read());  // h
            assertEquals(input[i++], (byte) baIs.read());  // e

            final byte[] buf = new byte[2];
            bomIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, i++, ++i), buf);  // l, l
            bomIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, i++, ++i), buf);  // o, \s
            baIs.read(buf);
            assertArrayEquals(Arrays.copyOfRange(input, i++, ++i), buf);  // w, o

            assertEquals(2, bomIs.skip(2));
            i+=2;
            assertEquals(input.length - i, bomIs.available());
            assertEquals(input.length - i, baIs.available());

            for (i = bomIs.available(); i > 0; i--) {
                assertEquals(input[input.length - i], (byte) bomIs.read());
            }

            assertEquals(0, bomIs.available());
            assertEquals(0, baIs.available());

            bomIs.close();
            baIs.close();
        }
    }
}
