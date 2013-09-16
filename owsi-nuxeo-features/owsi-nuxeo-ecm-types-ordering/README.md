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
    <category name="Basics">
      <type>Note</type>
      <type>File</type>
      <type>Picture</type>
    </category>
    <category name="Web">
      <type>WebSite</type>
      <type>BlogSite</type>
    </category>
    <hiddenTypes>
      <type>Forum</type>
    </hiddenTypes>
  </ordering>
</extension>
```

* Default (and already localized) category names are: *SimpleDocument, Collaborative, SuperDocument, misc*.
* Types not explicitly mentioned remain in their original category.

You can also configure globally the column size:

```xml
<extension target="fr.openwide.nuxeo.ordering.service.EcmTypesOrderingService" point="config">
  <config>
    <columnSize>6</columnSize>
  </config>
</extension>
```
