Open Wide : Nuxeo Field Formatter
=================================

## Introduction

This plugin integrates Nuxeo with [Formatter.js](http://firstopinion.github.io/formatter.js/) to enable complex, real-time validation of text input ([see demos here](http://firstopinion.github.io/formatter.js/demos.html)). All configuration can be done through XML.

**Module state:** Incubation

## Examples

### XML contribution

```xml
<extension point="widgets" target="org.nuxeo.ecm.platform.forms.layout.WebLayoutManager">
  <widget name="creditCard" type="formatter">
    <labels>
      <label mode="any">label.myschema.creditcard</label>
    </labels>
    <translated>true</translated>
    <fields>
      <field>myschema:creditcard</field>
    </fields>
    <properties mode="any">
      <property name="pattern">{{9999}}-{{9999}}-{{9999}}-{{9999}}</property>
    </properties>
  </widget>
</extension>
```

You can also make things a bit more maintainable with:

```xml
<extension target="fr.openwide.nuxeo.formatter.FieldFormatterService" point="patterns">
  <pattern name="creditCard">{{9999}}-{{9999}}-{{9999}}-{{9999}}</pattern>
</extension>

<extension point="widgets" target="org.nuxeo.ecm.platform.forms.layout.WebLayoutManager">
  <widget name="creditCard" type="formatter">
    <labels>
      <label mode="any">label.myschema.creditcard</label>
    </labels>
    <translated>true</translated>
    <fields>
      <field>myschema:creditcard</field>
    </fields>
    <properties mode="any">
      <property name="patternName">creditCard</property>
    </properties>
  </widget>
</extension>
```

The optional `persistent` property from formatter.js is also supported.

### Nuxeo Studio

* Create a generic widget (Advanced Widgets > Generic)
* Set "Widget Type" to "formatter"
* Unfold "Custom properties configuration" and set a property with name "*pattern*" and value "*{{9999}}-{{9999}}-{{9999}}-{{9999}}*"
