Open Wide : ECM Types Ordering
==============================

## Introduction

This provides an extension point to reorder all document types from the "New document" pop-up.

**Module state:** Incubation

## Example

```xml
<extension target="fr.openwide.nuxeo.ordering.service.EcmTypesOrderingService" point="ordering">
  <ordering name="folderTypesOrdering" sort="alphabetical"><!-- or sort="manual" -->
    <filter name="folders">
      <rule grant="true">
        <type>Folder</type>
      </rule>
    </filter>
    <category name="Useful">
      <type>Note</type>
      <type>File</type>
      <type>Picture</type>
    </category>
    <category name="misc" default="true" />
  </ordering>
</extension>
```

You can also configure globally the column size:

```xml
<extension target="fr.openwide.nuxeo.ordering.service.EcmTypesOrderingService" point="config">
  <config>
    <columnSize>6</columnSize>
  </config>
</extension>
```
