Open Wide : Nuxeo Commons
=========================

## Introduction

This repository provides various tools and features for your Nuxeo projects. Most of them have been extracted from actual client projects.

**Core**

* `owsi-nuxeo-constants`: Various constants exposed on Java classes, mainly to ease the manipulation of documents.
* `owsi-nuxeo-tests-helper`: Thin helper to set up tests.
* `owsi-nuxeo-utils`: Miscellaneous utility methods, plus an extension point to display your project version.

**Features**

* `owsi-nuxeo-property-sync`: Synchronizes properties from documents to their children.
* `owsi-nuxeo-avatar-importer`: Watches a given folder to import its contents as avatars.
* `owsi-nuxeo-ecm-types-ordering`: Customizes the appearance of the doctype selection pop-up.
* `owsi-nuxeo-document-creation-script`: An alternative to the Content Template service.
* `owsi-nuxeo-generic-properties`: Generic extension point to store simple data.
* `owsi-nuxeo-field-formatting`: Real-time validation of text inputs.

## Compatibility

Nuxeo version | OWSI Commons version | Comments
--- | --- | ---
5.8 | 0.1.4 | 
5.6 | 0.1.3 | 

## How to

#### Get the binaries

The bundles are on Maven Central, meaning you can either:

* [Download them manually](http://search.maven.org/#search|ga|1|g%3A%22fr.openwide.nuxeo.commons%22)
* Embed them in your Maven project, to use them as libraries and/or include them in your Marketplace package (see below).

#### Build from sources

Clone this repository, run `mvn install`, you're done.

#### Deploy the bundles

You can just put the binaries in the *nxserver/bundles* folder of Nuxeo, but if your project is built into a Marketplace package, the preferred way is to make them part of your assembly:

> **Important**: most features require both `owsi-nuxeo-constants` and `owsi-nuxeo-utils`, so make sure to deploy them too.

**pom.xml**

```xml
    ...
    <dependency>
      <groupId>fr.openwide.nuxeo.commons</groupId>
      <artifactId>owsi-nuxeo-constants</artifactId>
      <version>0.1.4</version>
    </dependency>
    <dependency>
      <groupId>fr.openwide.nuxeo.commons</groupId>
      <artifactId>owsi-nuxeo-utils</artifactId>
      <version>0.1.4</version>
    </dependency>
    <dependency>
      <groupId>fr.openwide.nuxeo.commons</groupId>
      <artifactId>owsi-nuxeo-avatar-importer</artifactId>
      <version>0.1.4</version>
    </dependency>
    ...
```

**assembly.xml**

```xml
<project name="nuxeo-assembly" ...>
  ...
  <copy todir="${outdir}/marketplace/install/bundles">
			<artifact:resolveFile key="fr.openwide.nuxeo.commons:owsi-nuxeo-constants::jar" />
			<artifact:resolveFile key="fr.openwide.nuxeo.commons:owsi-nuxeo-utils::jar" />
			<artifact:resolveFile key="fr.openwide.nuxeo.commons:owsi-nuxeo-avatar-importer::jar" />
			...
  </copy>
  ...
</project>
```

## Licensing

The contents of this repository, unless otherwise mentioned, are licensed under the [LGPL](http://www.gnu.org/copyleft/lesser.html).

## Links

* [Open Wide corporate website](http://www.openwide.fr/)
* [Nuxeo corporate website](http://www.nuxeo.com/fr)

