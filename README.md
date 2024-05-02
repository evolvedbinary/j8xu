# Java 8 XML Utilities

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/evolvedbinary/j8xu/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/evolvedbinary/j8xu/tree/main)
[![Coverage Status](https://coveralls.io/repos/github/evolvedbinary/j8xu/badge.svg?branch=main)](https://coveralls.io/github/evolvedbinary/j8xu?branch=main)
[![Java 8](https://img.shields.io/badge/java-8-blue.svg)](https://adoptopenjdk.net/)
[![License](https://img.shields.io/badge/license-BSD%203-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)


Some extra utility classes for making working with XML in Java just that little bit easier.

The main thing here at the moment is a DSL like Builder Pattern for programmatically constructing.

## XML Builder

The project is split into an API and implementations. The main API class of interest is `XmlBuilder` and where you should start from. At the moment there is a single implementation `StringXmlBuilder`, however implementing the API is trivial, and you are free to create your own implementations.

The XmlBuilder provides methods for constructing all XML Node types:
* `document`
* `element`
* `attributes`
* `text`
* `cdata`
* `comment`

There are several overloaded `element` methods to allow you to specify the namespace and/or prefixes for an element, and any attributes.

## String XML Builder Example
```java
import com.evolvedbinary.j8xu.builder.api.XmlBuilder;
import com.evolvedbinary.j8xu.builder.api.XmlDocumentBuilder;
import static com.evolvedbinary.j8xu.builder.api.XmlBuilder.attribute;

import com.evolvedbinary.j8xu.builder.impl.string.StringXmlBuilder;

public class Example {

  public static void main(String args[]) {

    XmlBuilder<String> x = new StringXmlBuilder();  // or instantiate your own implementation here

    XmlDocumentBuilder<String> document = x.document(
        x.comment("start of the doc"),
        x.element("http://example.com/people", "people",
            x.element("person",
                x.attributes(
                    attribute("id", 1)
                ),
                x.element("firstName", x.text("john")),
                x.element("lastName", x.text("doe")),
                x.element("phone", x.text("555-555-55")),
                x.element("status")
            )
        ),
        x.comment("end of the doc")
    );

    String asString = document.build();
    System.out.println(asString);
  }
}
```

