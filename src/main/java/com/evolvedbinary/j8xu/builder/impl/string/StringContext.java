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

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;

class StringContext {
  private final Deque<StringXmlElementNamespace> inScopeNamespaces = new ArrayDeque<>();
  private int treeDepth = 0;
  private int mixedContentTreeDepth = -1;
  @Nullable private final String indent;

  StringContext(@Nullable final String indent) {
    this.indent = indent;
  }

  /**
   * Determine if a prefix is in scope.
   *
   * @param prefix the prefix to test.
   * @param namespace the namespace associated with the prefix
   *
   * @return true if the prefix is in scope, false otherwise.
   */
  boolean isPrefixInScope(final String prefix, final String namespace) {
      for (final StringXmlElementNamespace inScopeNamespace : inScopeNamespaces) {
        if (prefix.equals(inScopeNamespace.prefix)) {
          return namespace.equals(inScopeNamespace.namespace);
        }
      }
      return false;
  }

  /**
   * Determine if a namespace is in scope.
   *
   * @param namespace the namespace to test.
   *
   * @return true if the namespace is in scope, false otherwise.
   */
  boolean isNamespaceInScope(final String namespace) {
    for (final StringXmlElementNamespace inScopeNamespace : inScopeNamespaces) {
      if (namespace.equals(inScopeNamespace.namespace)) {
        return true;
      }
    }
    return false;
  }

  void pushNamespace(final StringXmlElementNamespace namespace) {
    inScopeNamespaces.push(namespace);
  }

  void popNamespace() {
    inScopeNamespaces.pop();
  }

  boolean indent() {
    return indent != null;
  }

  String getIndent() {
    return indent;
  }

  int getTreeDepth() {
    return treeDepth;
  }

  void incrementTreeDepth() {
    treeDepth++;
  }

  void decrementTreeDepth() {
    treeDepth--;
  }

  void markMixedContentTreeDepth() {
    if (this.mixedContentTreeDepth == -1) {
      this.mixedContentTreeDepth = this.treeDepth;
    }
  }

  boolean inMixedContext() {
    return this.mixedContentTreeDepth != -1 && this.treeDepth >= this.mixedContentTreeDepth;
  }

  void resetMixedContentTreeDepth() {
    if (this.mixedContentTreeDepth == this.treeDepth) {
      this.mixedContentTreeDepth = -1;
    }
  }
}
