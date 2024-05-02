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
package com.evolvedbinary.j8xu.builder.impl.string;

import com.evolvedbinary.j8xu.builder.api.*;
import org.junit.jupiter.api.Test;

import static com.evolvedbinary.j8xu.builder.api.XmlBuilder.attribute;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringXmlBuilderTest {

  @Test
  public void emptyElement() {
    final StringXmlBuilder x = new StringXmlBuilder();

    XmlElementBuilder<String> elementBuilder =
        x.element("elem1");

    assertEquals("<elem1/>", elementBuilder.build());

    elementBuilder =
        x.element("http://myns", "elem1");

    assertEquals("<elem1 xmlns=\"http://myns\"/>", elementBuilder.build());

    elementBuilder =
        x.element("http://myns", "elem1", "my");

    assertEquals("<my:elem1 xmlns:my=\"http://myns\"/>", elementBuilder.build());
  }

  @Test
  public void emptyElementWithAttributes() {
    final StringXmlBuilder x = new StringXmlBuilder();

    XmlElementBuilder<String> elementBuilder =
        x.element("elem1", x.attributes(attribute("k1", "v1"), attribute("k2", "v2")));

    assertEquals("<elem1 k1=\"v1\" k2=\"v2\"/>", elementBuilder.build());

    elementBuilder =
        x.element("http://myns", "elem1", x.attributes(attribute("k1", "v1"), attribute("k2", "v2")));

    assertEquals("<elem1 xmlns=\"http://myns\" k1=\"v1\" k2=\"v2\"/>", elementBuilder.build());

    elementBuilder =
        x.element("http://myns", "elem1", "my", x.attributes(attribute("k1", "v1"), attribute("k2", "v2")));

    assertEquals("<my:elem1 xmlns:my=\"http://myns\" k1=\"v1\" k2=\"v2\"/>", elementBuilder.build());
  }

  @Test
  public void elementWithText() {
    final StringXmlBuilder x = new StringXmlBuilder();

    XmlElementBuilder<String> elementBuilder =
        x.element("elem1", x.text("text1"));

    assertEquals("<elem1>text1</elem1>", elementBuilder.build());

    elementBuilder =
        x.element("http://myns", "elem1", x.text("text1"));

    assertEquals("<elem1 xmlns=\"http://myns\">text1</elem1>", elementBuilder.build());

    elementBuilder =
        x.element("http://myns", "elem1", "my", x.text("text1"));

    assertEquals("<my:elem1 xmlns:my=\"http://myns\">text1</my:elem1>", elementBuilder.build());
  }

  @Test
  public void elementWithComment() {
    final StringXmlBuilder x = new StringXmlBuilder();

    XmlElementBuilder<String> elementBuilder =
        x.element("elem1", x.comment("comment1"));

    assertEquals("<elem1><!-- comment1 --></elem1>", elementBuilder.build());

    elementBuilder =
        x.element("http://myns", "elem1", x.comment("comment1"));

    assertEquals("<elem1 xmlns=\"http://myns\"><!-- comment1 --></elem1>", elementBuilder.build());

    elementBuilder =
        x.element("http://myns", "elem1", "my", x.comment("comment1"));

    assertEquals("<my:elem1 xmlns:my=\"http://myns\"><!-- comment1 --></my:elem1>", elementBuilder.build());
  }

  @Test
  public void elementWithCdata() {
    final StringXmlBuilder x = new StringXmlBuilder();

    XmlElementBuilder<String> elementBuilder =
        x.element("elem1", x.cdata("cdata1"));

    assertEquals("<elem1><![CDATA[cdata1]]></elem1>", elementBuilder.build());

    elementBuilder =
        x.element("http://myns", "elem1", x.cdata("cdata1"));

    assertEquals("<elem1 xmlns=\"http://myns\"><![CDATA[cdata1]]></elem1>", elementBuilder.build());

    elementBuilder =
        x.element("http://myns", "elem1", "my", x.cdata("cdata1"));

    assertEquals("<my:elem1 xmlns:my=\"http://myns\"><![CDATA[cdata1]]></my:elem1>", elementBuilder.build());
  }

  @Test
  public void elementWithChildElementsNoIndent() {
    final StringXmlBuilder x = new StringXmlBuilder();

    XmlElementBuilder<String> elementBuilder =
        x.element("elem1", x.element("elem2", x.element("elem3")));

    assertEquals("<elem1><elem2><elem3/></elem2></elem1>", elementBuilder.build());

    final String ns = "http://myns";

    elementBuilder =
        x.element(ns, "elem1",  x.element(ns, "elem2", x.element(ns, "elem3")));

    assertEquals("<elem1 xmlns=\"" + ns + "\"><elem2><elem3/></elem2></elem1>", elementBuilder.build());

    final String prefix = "my";

    elementBuilder =
        x.element(ns, "elem1", prefix,  x.element(ns, "elem2", prefix, x.element(ns, "elem3", prefix)));

    assertEquals("<" + prefix + ":elem1 xmlns:my=\"" + ns + "\"><" + prefix + ":elem2><" + prefix + ":elem3/></" + prefix + ":elem2></" + prefix + ":elem1>", elementBuilder.build());
  }

  @Test
  public void elementWithChildElementsIndent() {
    final StringXmlBuilder x = new StringXmlBuilder("\t");

    XmlElementBuilder<String> elementBuilder =
        x.element("elem1", x.element("elem2", x.element("elem3")));

    assertEquals("<elem1>\n\t<elem2>\n\t\t<elem3/>\n\t</elem2>\n</elem1>", elementBuilder.build());

    final String ns = "http://myns";

    elementBuilder =
        x.element(ns, "elem1",  x.element(ns, "elem2", x.element(ns, "elem3")));

    assertEquals(
        "<elem1 xmlns=\"" + ns + "\">\n" +
        "\t<elem2>\n" +
        "\t\t<elem3/>\n" +
        "\t</elem2>\n" +
        "</elem1>",
        elementBuilder.build());

    final String prefix = "my";

    elementBuilder =
        x.element(ns, "elem1", prefix,  x.element(ns, "elem2", prefix, x.element(ns, "elem3", prefix)));

    assertEquals(
        "<" + prefix + ":elem1 xmlns:my=\"" + ns + "\">\n" +
        "\t<" + prefix + ":elem2>\n" +
        "\t\t<" + prefix + ":elem3/>\n" +
        "\t</" + prefix + ":elem2>\n" +
        "</" + prefix + ":elem1>",
        elementBuilder.build());

  }

  @Test
  public void elementWithMixedContentIndent() {
    final StringXmlBuilder x = new StringXmlBuilder("\t");

    XmlElementBuilder<String> elementBuilder =
        x.element("div",
            x.element("p", x.text("hello "),
                x.element("b", x.text("world")),
                x.text("!")
            )
        );

    assertEquals(
        "<div>\n" +
            "\t<p>hello <b>world</b>!</p>\n" +
            "</div>",
        elementBuilder.build());

    final String ns = "http://myns";

    elementBuilder =
        x.element(ns, "div",
            x.element(ns, "p", x.text("hello "),
                x.element(ns, "b", x.text("world")),
                x.text("!")
            )
        );


    assertEquals(
        "<div xmlns=\"" + ns + "\">\n" +
        "\t<p>hello <b>world</b>!</p>\n" +
        "</div>",
        elementBuilder.build());

    final String prefix = "my";

    elementBuilder = x.element(ns, "div", prefix,
        x.element(ns, "p", prefix, x.text("hello "),
            x.element(ns, "b", prefix, x.text("world")),
            x.text("!")
        )
    );

    assertEquals(
        "<" + prefix + ":div xmlns:my=\"http://myns\">\n" +
        "\t<" + prefix + ":p>hello <" + prefix + ":b>world</" + prefix + ":b>!</" + prefix + ":p>\n" +
        "</" + prefix + ":div>",
        elementBuilder.build());
  }

  @Test
  public void attributes() {
    final StringXmlBuilder x = new StringXmlBuilder();

    XmlAttributesBuilder<String> attributesBuilder =
        x.attributes(attribute("k1", "v1"));

    assertEquals("k1=\"v1\"", attributesBuilder.build());

    attributesBuilder =
        x.attributes(
            attribute("k1", "v1"),
            attribute("k2", "v2"),
            attribute("k3", "v3")
        );

    assertEquals("k1=\"v1\" k2=\"v2\" k3=\"v3\"", attributesBuilder.build());
  }

  @Test
  public void text() {
    final String content = "This is text";

    final XmlTextBuilder<String> textBuilder =
        new StringXmlBuilder().text(content);

    assertEquals(content, textBuilder.build());
  }

  @Test
  public void comment() {
    final String content = "This is a comment";

    final XmlCommentBuilder<String> commentBuilder =
        new StringXmlBuilder().comment(content);

    assertEquals("<!-- " + content + " -->", commentBuilder.build());
  }

  @Test
  public void cdata() {
    final String content = "This is some CDATA";

    final XmlCdataBuilder<String> cdataBuilder =
        new StringXmlBuilder().cdata(content);

    assertEquals("<![CDATA[" + content + "]]>", cdataBuilder.build());
  }
}
