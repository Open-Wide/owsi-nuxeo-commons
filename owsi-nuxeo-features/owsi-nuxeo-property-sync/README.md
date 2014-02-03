Open Wide : Nuxeo Property Sync
===============================

## Introduction

Property Sync is a tool that allows to synchronize properties from a document to its children. It can be useful to initialize properties on a new document, or simply to copy information that can the be used for search.

Its behavior can be configured through XML contributions.

**Module state:** Put into in production

## Example

The following contribution:

* matches *Notes* and *Files*, and any document that have the facet *Sync*
* copies their workspace's description unless it's already set. The copy works even if the workspace is not the direct parent, or if the workspace is actually a subtype of the document type *Workspace*.
      
```xml
<extension target="fr.openwide.nuxeo.propertysync.service.PropertySyncService" point="rule">
  <rule name="descriptionFromWorkspace">
    <target>
      <type>Note</type>
      <type>File</type>
      <facet>Sync</facet>
    </target>
    <properties>
      <property xpath="dc:description" ancestorType="Workspace" 
          ancestorXpath="dc:description" onlyIfNull="true" />
    </properties>
  </rule>
</extension>
```

Here, since the workspaces can hold a large amount of documents, performance issues can occur when saving the workspace (we need to copy the updated description to all children). To avoid such situations, consider using the `noMassUpdate="true"` on your `<rule>` tag, to disable the copy on events triggered by the ancestor.

## Additional documentation

* This module supports synchronization on document creation, edition, move and copy.
* To avoid event loops with your own event listeners, you can:
  * Use `doc.putContextData(PropertySyncService.CONTEXT_BYPASS_PROPERTY_SYNC, true)` before saving your document
  * Check for `doc.getContextData(PropertySyncService.CONTEXT_BYPASS_PROPERTY_SYNC) == null` before handling an event (especially the `beforeDocumentModification` event)
