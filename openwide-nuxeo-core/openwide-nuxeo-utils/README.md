Open Wide : Nuxeo Utils
=======================

## Introduction

Miscellaneous utility classes for lazy developers. Additionally, provides an easy way to display the version number of your project.

*Requires **openwide-nuxeo-constants**.*

## Documentation

The following XML contribution allows to display the version of any bundle in the footer.
   
``` 
  <extension target="fr.openwide.commons.utils.jsf.VersionDisplayService" point="config">
    <config>
      <versionNumberPrefix>MyProject v</versionNumberPrefix>
      <bundleMatchRegexp>openwide-.*</bundleMatchRegexp>
    </config>
  </extension>
```

If you prefer to display the version somewhere else, browse to `VersionDisplayService.xml` to find a *footerVersionNumber* action that you can replace.
