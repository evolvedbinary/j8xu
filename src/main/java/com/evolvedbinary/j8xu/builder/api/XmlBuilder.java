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
package com.evolvedbinary.j8xu.builder.api;

/**
 * Interface for a builder class that builds an XML document or fragment.
 *
 * Typically the starting point is the {@link #document(XmlChildNodeBuilder[])} function.
 *
 * @param <T> The result of the build.
 */
public interface XmlBuilder<T> {

  /**
   * Create a builder for building an XML Document Node.
   *
   * @param children the child nodes (builders) of the document.
   *
   * @return the document node builder.
   */
  @SuppressWarnings("unchecked")
  XmlDocumentBuilder<T> document(final XmlChildNodeBuilder<T>... children);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param localName the local name of the element.
   * @param attributes the attributes of the element.
   *
   * @return the element node builder.
   */
  XmlElementBuilder<T> element(final String localName, final XmlAttributesBuilder<T> attributes);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param localName the local name of the element.
   * @param attributes the attributes of the element.
   * @param children the child nodes (builders) of the element.
   *
   * @return the element node builder.
   */
  @SuppressWarnings("unchecked")
  XmlElementBuilder<T> element(final String localName, final XmlAttributesBuilder<T> attributes, final XmlChildNodeBuilder<T>... children);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param namespace the namespace in which the element resides.
   * @param localName the local name of the element.
   * @param attributes the attributes of the element.
   *
   * @return the element node builder.
   */
  XmlElementBuilder<T> element(final String namespace, final String localName, final XmlAttributesBuilder<T> attributes);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param namespace the namespace in which the element resides.
   * @param localName the local name of the element.
   * @param attributes the attributes of the element.
   * @param children the child nodes (builders) of the element.
   *
   * @return the element node builder.
   */
  @SuppressWarnings("unchecked")
  XmlElementBuilder<T> element(final String namespace, final String localName, final XmlAttributesBuilder<T> attributes, final XmlChildNodeBuilder<T>... children);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param namespace the namespace in which the element resides.
   * @param localName the local name of the element.
   * @param prefix the namespace prefix for the element.
   * @param attributes the attributes of the element.
   *
   * @return the element node builder.
   */
  XmlElementBuilder<T> element(final String namespace, final String localName, final String prefix, final XmlAttributesBuilder<T> attributes);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param namespace the namespace in which the element resides.
   * @param localName the local name of the element.
   * @param prefix the namespace prefix for the element.
   * @param attributes the attributes of the element.
   * @param children the child nodes (builders) of the element.
   *
   * @return the element node builder.
   */
  @SuppressWarnings("unchecked")
  XmlElementBuilder<T> element(final String namespace, final String localName, final String prefix, final XmlAttributesBuilder<T> attributes, final XmlChildNodeBuilder<T>... children);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param localName the local name of the element.
   *
   * @return the element node builder.
   */
  XmlElementBuilder<T> element(final String localName);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param localName the local name of the element.
   * @param children the child nodes (builders) of the element.
   *
   * @return the element node builder.
   */
  @SuppressWarnings("unchecked")
  XmlElementBuilder<T> element(final String localName, final XmlChildNodeBuilder<String>... children);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param namespace the namespace in which the element resides.
   * @param localName the local name of the element.
   *
   * @return the element node builder.
   */
  XmlElementBuilder<T> element(final String namespace, final String localName);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param namespace the namespace in which the element resides.
   * @param localName the local name of the element.
   * @param children the child nodes (builders) of the element.
   *
   * @return the element node builder.
   */
  @SuppressWarnings("unchecked")
  XmlElementBuilder<T> element(final String namespace, final String localName, final XmlChildNodeBuilder<T>... children);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param namespace the namespace in which the element resides.
   * @param localName the local name of the element.
   * @param prefix the namespace prefix for the element.
   *
   * @return the element node builder.
   */
  XmlElementBuilder<T> element(final String namespace, final String localName, final String prefix);

  /**
   * Create a builder for building an XML Element Node.
   *
   * @param namespace the namespace in which the element resides.
   * @param localName the local name of the element.
   * @param prefix the namespace prefix for the element.
   * @param children the child nodes (builders) of the element.
   *
   * @return the element node builder.
   */
  @SuppressWarnings("unchecked")
  XmlElementBuilder<T> element(final String namespace, final String localName, final String prefix, final XmlChildNodeBuilder<T>... children);

  /**
   * Create a builder for building an XML Attribute Node(s).
   *
   * @param attributes the attributes.
   *
   * @return the attributes nodes builder.
   */
  @SuppressWarnings("unchecked")
  XmlAttributesBuilder<T> attributes(final XmlAttribute... attributes);

  /**
   * Utility method for more simply constructing an attribute.
   *
   * @param name the name of the attribute.
   * @param <V> the type of the value of the attribute.
   * @param value the value of the attribute.
   *
   * @return the attribute.
   */
  static <V> XmlAttribute<V> attribute(final String name, final V value) {
    return new XmlAttribute<>(name, value);
  }

  /**
   * Create a builder for building an XML Text Node.
   *
   * @param content the ontent of the text node.
   *
   * @return the text node builder.
   */
  XmlTextBuilder<T> text(final String content);

  /**
   * Create a builder for building an XML Comment Node.
   *
   * @param content the content of the comment node.
   *
   * @return the comment node builder.
   */
  XmlCommentBuilder<T> comment(final String content);

  /**
   * Create a builder for building an XML CDATA Node.
   *
   * @param content the content of the CDATA node.
   *
   * @return the CDATA node builder.
   */
  XmlCdataBuilder<T> cdata(final String content);
}
