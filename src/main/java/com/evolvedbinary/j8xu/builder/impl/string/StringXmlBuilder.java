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

import javax.annotation.Nullable;

/**
 * An implementation of an XML Builder that builds a string representation.
 */
public class StringXmlBuilder implements XmlBuilder<String> {

  @SuppressWarnings("unchecked")
  private static final XmlChildNodeBuilder<String>[] NO_CHILDREN = new XmlChildNodeBuilder[0];

  private final StringContext context;

  /**
   * Default constructor.
   */
  public StringXmlBuilder() {
    this(null);
  }

  /**
   * Constructor.
   *
   * @param indent if you want the XML output to be indented you should set this to the indentation character(s), otherwise set it to null.
   */
  public StringXmlBuilder(@Nullable final String indent) {
    this.context = new StringContext(indent);
  }

  @SafeVarargs
  @Override
  public final XmlDocumentBuilder<String> document(final XmlChildNodeBuilder<String>... children) {
    return new StringXmlDocumentBuilder(context, children);
  }

  @Override
  public final XmlElementBuilder<String> element(final String localName, final XmlAttributesBuilder<String> attributes) {
    return element(null, localName, null, attributes, NO_CHILDREN);
  }

  @SafeVarargs
  @Override
  public final XmlElementBuilder<String> element(final String localName, final XmlAttributesBuilder<String> attributes, final XmlChildNodeBuilder<String>... children) {
    return element(null, localName, null, attributes, children);
  }

  @Override
  public final XmlElementBuilder<String> element(final String namespace, final String localName, final XmlAttributesBuilder<String> attributes) {
    return element(namespace, localName, null, attributes, NO_CHILDREN);
  }

  @SafeVarargs
  @Override
  public final XmlElementBuilder<String> element(final String namespace, final String localName, final XmlAttributesBuilder<String> attributes, final XmlChildNodeBuilder<String>... children) {
    return element(namespace, localName, null, attributes, children);
  }

  @Override
  public final XmlElementBuilder<String> element(@Nullable final String namespace, final String localName, @Nullable final String prefix, @Nullable final XmlAttributesBuilder<String> attributes) {
    return new StringXmlElementBuilder(context, namespace, localName, prefix, attributes, NO_CHILDREN);
  }

  @SafeVarargs
  @Override
  public final XmlElementBuilder<String> element(@Nullable final String namespace, final String localName, @Nullable final String prefix, @Nullable final XmlAttributesBuilder<String> attributes, final XmlChildNodeBuilder<String>... children) {
    return new StringXmlElementBuilder(context, namespace, localName, prefix, attributes, children);
  }

  @Override
  public final XmlElementBuilder<String> element(final String localName) {
    return element(null, localName, null, null, NO_CHILDREN);
  }

  @SafeVarargs
  @Override
  public final XmlElementBuilder<String> element(final String localName, final XmlChildNodeBuilder<String>... children) {
    return element(null, localName, null, null, children);
  }

  @Override
  public final XmlElementBuilder<String> element(final String namespace, final String localName) {
    return element(namespace, localName, null, null, NO_CHILDREN);
  }

  @SafeVarargs
  @Override
  public final XmlElementBuilder<String> element(final String namespace, final String localName, final XmlChildNodeBuilder<String>... children) {
    return element(namespace, localName, null, null, children);
  }

  @Override
  public final XmlElementBuilder<String> element(final String namespace, final String localName, final String prefix) {
    return element(namespace, localName, prefix, null, NO_CHILDREN);
  }

  @SafeVarargs
  @Override
  public final XmlElementBuilder<String> element(final String namespace, final String localName, final String prefix, final XmlChildNodeBuilder<String>... children) {
    return element(namespace, localName, prefix, null, children);
  }

  @SafeVarargs
  @Override
  public final XmlAttributesBuilder<String> attributes(final XmlAttribute... attributes) {
    return new StringXmlAttributesBuilder(attributes);
  }

  @Override
  public final XmlTextBuilder<String> text(final String content) {
    return new StringXmlTextBuilder(context, content);
  }

  @Override
  public final XmlCommentBuilder<String> comment(final String content) {
    return new StringXmlCommentBuilder(context, content);
  }

  @Override
  public final XmlCdataBuilder<String> cdata(final String content) {
    return new StringXmlCdataBuilder(context, content);
  }
}
