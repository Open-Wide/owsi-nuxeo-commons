Open Wide : Nuxeo Commons
=========================

## Introduction

This repository provides various reusable resources for your Nuxeo projects. Most of them have been extracted from actual client projects.

**Core**

* `openwide-nuxeo-constants`: Various constants exposed on Java classes, mainly to ease the manipulation of documents.
* `openwide-nuxeo-tests-helper`: Thin helper to set up tests.
* `openwide-nuxeo-utils`: Miscellaneous utility methods.

**Features**

* `openwide-nuxeo-property-sync`: Allows to synchronize properties from a document to its children.
* `openwide-nuxeo-avatar-importer`: Watches a given folder to import its contents as avatars.
* `openwide-nuxeo-document-creation-script`: An alternative to the Content Template service.
* `openwide-nuxeo-generic-properties`: Generic extension point to store simple data.

## How to

These projects are not (yet) deployed to any public Maven Repository. Clone this repository (or download the sources [by tag](https://github.com/Open-Wide/openwide-nuxeo-commons/tags)) and simply run `mvn install` to add the latest version of these projects to your local repository.

Then, add the desired Maven dependencies to your Nuxeo modules' POMs, and deploy the JARs to your Nuxeo setup the way you prefer (either manually, or through a Marketplace assembly of your modules).

## Licensing

The contents of this repository, unless otherwise mentioned, is licensed under the [LGPL](http://www.gnu.org/copyleft/lesser.html).

## Links

* [Open Wide corporate website](http://www.openwide.fr/)
* [Nuxeo corporate website](http://www.nuxeo.com/fr)
