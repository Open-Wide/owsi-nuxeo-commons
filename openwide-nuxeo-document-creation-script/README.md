Open Wide : Nuxeo Document Creation Script
==========================================

## Introduction

This module is quite similar to the [Content Template service](http://explorer.nuxeo.org/nuxeo/site/distribution/current/viewComponent/org.nuxeo.ecm.platform.content.template.service.ContentTemplateService), bus is more targeted towards providing a simple way to instanciate documents through XML contributions.

The way to execute these contributions is up to the user, this module only provides a Java service to fetch and execute the scripts.

(**TODO**: Contribute improvements the Content Template service instead, to support the use cases targeted by this module)

## Example

**Creating a script through XML contribution:**

```xml
<extension target="fr.openwide.nuxeo.dcs.service.DocumentCreationScriptService" point="scripts">
  <script name="folderInit">
      <document path="myfile" doctype="File" title="Foo" />
      <document path="mypicture" doctype="Picture" title="Bar" />
      <document path="myfolder" doctype="Folder" title="Hello">
          <acl blockInheritance="true">
              <ace principal="Bob" permission="Everything" granted="true" />
              <ace principal="John" permission="Everything" granted="true" />
          </acl>
          <properties>
              <property xpath="dc:description" value="For Bob and John only" />
          </properties>
          <facets>
              <facet>MyFacet</facet>
          </facets>
          <allowedTypes>
              <type>File</type>
              <type>Note</type>
          </allowedTypes>
  </script>
</extension>
```

**Calling the script:**

```java
Framework.getService(DocumentCreationScriptService.class).runScript(documentManager, "folderInit", folderModel, false);
```
