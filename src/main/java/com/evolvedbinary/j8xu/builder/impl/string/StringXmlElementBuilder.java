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

import com.evolvedbinary.j8xu.builder.api.XmlAttributesBuilder;
import com.evolvedbinary.j8xu.builder.api.XmlChildNodeBuilder;
import com.evolvedbinary.j8xu.builder.api.XmlElementBuilder;

import javax.annotation.Nullable;

/**
 * An implementation of an XML Element Builder that builds a string representation.
 */
public class StringXmlElementBuilder implements XmlElementBuilder<String> {
  private final StringContext context;
  @Nullable private final String namespace;
  private final String localName;
  private @Nullable final String prefix;
  private @Nullable final XmlAttributesBuilder<String> attributes;
  private final XmlChildNodeBuilder<String>[] children;

  @SafeVarargs
  StringXmlElementBuilder(final StringContext context, @Nullable final String namespace, final String localName, @Nullable final String prefix, @Nullable final XmlAttributesBuilder<String> attributes, final XmlChildNodeBuilder<String>... children) {
    this.context = context;
    this.namespace = namespace;
    this.localName = localName;
    this.prefix = prefix;
    this.attributes = attributes;
    this.children = children;
  }

  @Override
  public String build() {
    final StringBuilder buffer = new StringBuilder();

    // indent?
    final int startTreeDepth = context.getTreeDepth();
    if (startTreeDepth > 0 && context.indent() && !context.inMixedContext()) {
      buffer.append('\n');
      for (int i = 0; i < startTreeDepth; i++) {
        buffer.append(context.getIndent());
      }
    }

    buffer.append('<');
    if (prefix != null) {
      buffer.append(prefix);
      buffer.append(':');
    }
    buffer.append(localName);

    // namespace
    boolean pushedNamespace = false;
    if (prefix != null && !context.isPrefixInScope(prefix, namespace)) {
      buffer.append(" xmlns:");
      buffer.append(prefix);
      buffer.append("=\"");
      buffer.append(namespace);
      buffer.append("\"");

      context.pushNamespace(new StringXmlElementNamespace(namespace, prefix));
      pushedNamespace = true;
    } else if (namespace != null && !context.isNamespaceInScope(namespace)) {
      buffer.append(" xmlns");
      buffer.append("=\"");
      buffer.append(namespace);
      buffer.append("\"");

      context.pushNamespace(new StringXmlElementNamespace(namespace));
      pushedNamespace = true;
    }

    if (attributes != null) {
      buffer.append(' ');
      buffer.append(attributes.build());
    }

    context.incrementTreeDepth();

    if (children == null || children.length == 0) {
      // no children, so self-closing element
      buffer.append("/>");

    } else {
      buffer.append(">");

      // build the children
      for (int i = 0; i < children.length; i++) {
        final XmlChildNodeBuilder<String> child = children[i];

        // build the child
        buffer.append(child.build());
      }

      // indent?
      if (context.indent() && !context.inMixedContext()) {
        buffer.append('\n');
        for (int i = 0; i < startTreeDepth; i++) {
          buffer.append(context.getIndent());
        }
      }

      context.resetMixedContentTreeDepth();

      // close the element
      buffer.append("</");
      if (prefix != null) {
        buffer.append(prefix);
        buffer.append(':');
      }
      buffer.append(localName);
      buffer.append('>');
    }

    context.decrementTreeDepth();

    if (pushedNamespace) {
      context.popNamespace();
    }

    return buffer.toString();
  }
}
