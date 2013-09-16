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

## How to

#### Build the modules manually

These projects are not (yet) deployed to any public Maven Repository. You'll have to either:

* [Download a release from Github](https://github.com/Open-Wide/owsi-nuxeo-commons/releases)
* Clone this repository, then checkout the wanted version tag.

Use `mvn install` to add the latest version of these projects to your local repository.

#### Use the bundles in your projects

* If necessary (e.g. to use the `owsi-nuxeo-utils`), add the desired Maven dependencies to your Nuxeo modules' POMs.
* Deploy the JARs to your Nuxeo setup the way you prefer (usually either manually, or through a Marketplace assembly of your modules). Note that most feature projects require both `owsi-nuxeo-constants` and `owsi-nuxeo-utils` ; for instance, to use the Avatar import, you'll have to deploy:
  * `owsi-nuxeo-constants-(version).jar`
  * `owsi-nuxeo-utils-(version).jar`
  * `owsi-nuxeo-avatar-importer-(version).jar`

## Licensing

The contents of this repository, unless otherwise mentioned, are licensed under the [LGPL](http://www.gnu.org/copyleft/lesser.html).

## Links

* [Open Wide corporate website](http://www.openwide.fr/)
* [Nuxeo corporate website](http://www.nuxeo.com/fr)

