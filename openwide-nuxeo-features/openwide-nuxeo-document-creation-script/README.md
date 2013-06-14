Open Wide : Nuxeo Document Creation Script
==========================================

## Introduction

This module is quite similar to the [Content Template service](http://explorer.nuxeo.org/nuxeo/site/distribution/current/viewComponent/org.nuxeo.ecm.platform.content.template.service.ContentTemplateService), bus is more targeted towards providing a simple and fast way to instanciate documents through XML contributions.

The way to execute these contributions is up to the user, this module only provides a Java service and an Automation operation to fetch and execute the scripts.

(**TODO**: Contribute improvements the Content Template service instead, to support the use cases targeted by this module)

## Documentation

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

*Important* : There are two ways paths can be specified.

* By writing the full path of the file, e.g. `myworkspace/myfolder/myfile`
* By writing the parent path, e.g. `myworkspace/myfolder/` (notice the trailing slash). The name of the document with be generated according to its title.

**Calling the script with Java:**

```java
Framework.getService(DocumentCreationScriptService.class).runScript(documentManager, "folderInit", workspaceModel, false);
```

**Registering the script as an automation in Nuxeo Studio**

Add the automation definition to *Setting & Versioning > Registries > Automation Operations*: 

```json
{ "operations": [ 

  {
    "id": "OpenWide.DocumentCreationScript",
    "label": "Run Document Creation Script",
    "category": "Open Wide",
    "description": "",
    "url": "OpenWide.DocumentCreationScript",
    "signature": [ "void", "void" ],
    "params": [
      { "name": "scriptName", "type": "string", "required": true, "order": 0, "values": [] },
      { "name": "context", "type": "document", "required": false, "order": 0, "values": [] },
      { "name": "overwrite", "type": "boolean", "required": false, "order": 0, "values": [] }
    ]
  }

] }
```
