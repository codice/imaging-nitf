imaging-nitf
============
[![Build Status](https://travis-ci.org/codice/imaging-nitf.svg?branch=master)](https://travis-ci.org/codice/imaging-nitf)
[![CLA assistant](https://cla-assistant.io/readme/badge/codice/imaging-nitf)](https://cla-assistant.io/codice/imaging-nitf) 

Pure Java National Imagery Transmission Format (NITF) file support.

This implementation provides parsing for NITF 2.0 and NITF 2.1 files.
NATO Secondary Imagery Format 1.0 (NSIF 1.0) is effectively NITF 2.1 and
is also supported.

## Building

JDK11 (or potentially higher) is required.

```
git clone git://github.com/codice/imaging-nitf.git
```
Change to the root directory of the cloned repository. Run the following command:

```
mvn install
```

This will compile imaging-nitf and run all of the tests.

## Maven

```xml
  <dependency>
    <groupId>org.codice.imaging.nitf</groupId>
    <artifactId>codice-imaging-nitf-core</artifactId>
    <version>0.9-SNAPSHOT</version>
  </dependency>
  
  <dependency>
    <groupId>org.codice.imaging.nitf</groupId>
    <artifactId>codice-imaging-cgm</artifactId>
    <version>0.9-SNAPSHOT</version>
  </dependency>
```

## Using

```java
    File resourceFile = new File("sample.ntf");
    AllDataExtractionParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
    NitfReader reader = new FileReader(resourceFile);
    NitfFileParser.parse(reader, parseStrategy);
    NitfFileHeader nitfFileHeader = parseStrategy.getNitfHeader();
```

## Using the Flow API

```java
    File resourceFile = new File("sample.ntf");
    new NitfParserInputFlow()
          .file(resourceFile)
          .allData()
          .fileHeader((header) -> handleFileHeader(header))
          .forEachImageSegment((imageSegment) -> handleImageSegment(imageSegment))
          .forEachDataSegment((dataSegment) -> handleDataSegment(dataSegment))
          .forEachSymbolSegment((symbolSegment) -> handleSymbolSegment(symbolSegment))
          .forEachGraphicSegment((graphicSegment) -> handleGraphicSegment(graphicSegment))
          .forEachTextSegment((textSegment) -> handleTextSegment(textSegment))
          .forEachLabelSegment((labelSegment) -> handleLabelSegment(labelSegment));
```
