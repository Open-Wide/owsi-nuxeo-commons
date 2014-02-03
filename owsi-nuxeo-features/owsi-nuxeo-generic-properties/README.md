Open Wide : Nuxeo Generic Properties
====================================

## Introduction

Generic Properties provides a generic XML extension point to store and retrieve basic data (strings, numbers, lists, maps). See this as a "quick'n'dirty" way to store configuration ; the cleaner approach would be to create your own extension points.

**Module state:** Put into production

## Example
  
**XML contribution**
  
```xml
<extension target="fr.openwide.nuxeo.properties.PropertiesService" point="properties">
  <property name="my.namespace.mystring" type="string">hello world</property>
  <property name="my.namespace.mynumber" type="number">9001</property>
  <property name="my.namespace.mylist" type="list">
    <value>A</value>
    <value>B</value>
    <value>C</value>
  </property>
  <property name="my.namespace.mymap" type="map">
    <value key="bob">marley</value>
    <value key="michael">jackson</value>
  </property>
</extension>
```

Note: the *type* attribute is optional and defaults to *string*.

**Accessing the data**

```java
Framework.getService(PropertiesService.class).getListValue("my.namespace.mylist");
```
