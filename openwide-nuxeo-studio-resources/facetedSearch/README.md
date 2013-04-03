Nuxeo Studio how to : Create a faceted search form
==================================================

## Introduction

The content views can be used to build faceted searches (i.e. additional search forms within the search tab on the left menu), but *as of april 2013 (Nuxeo 5.6)* it is a bit tricky.

The problem: creating a new "Content View", adding some widgets and simply checking the "Faceted search" flag leaves you with a form that lacks various elements, including the mandatory *Go* button.

## Getting a working faceted search

#### Pre-requisites

* Having a content view with at least one widget (for example, add fulltext search with a simple drag'n'drop of the *"Featured Widgets > Full text"* widget).
* Having checked the *"Flags > Faceted search"* option.

#### Adding the faceted search list

* Put a *"More Widgets > Generic"* widget at the top of your form
* Name it *"[Faceted searches]"* or whatever
* Check *"Hide label"*
* Set **faceted_searches_selector** as the widget type

#### Adding the saved searches list

* Put a *"More Widgets > Generic"* widget below "\[Faceted searches\]"
* Name it *"[Saved searches]"* or whatever
* Check *"Hide label"*
* Set **saved_faceted_searches_selector** as the widget type

#### Adding the go button

Using the same steps as above doesn't work, so:

* Put a *"Featured Widgets > Template"* (the last one) widget below "\[Saved searches\]"
* Name it *"[Go]"* or whatever
* Check *"Hide label"*
* Attach the **faceted_search_actions_widget_template.xhtml** template found in this folder (or even better, use the one from your Nuxeo distribution)

Put an identical widget at the bottom of your content view.

## Special widgets

This should be enough to have a complete and working content view. Even if you don't use the exact same widgets as the ones from the default faceted search, you can get similar features with "classic" widgets, with the exception of:

#### Path selection

Making path selection work on a Nuxeo Studio-based content view is a bit tricky, since we have to make Studio generate what he calls a "content view predicate" of the form:

```xml
<predicate parameter="ecm:path" operator="STARTSWITH">
  <field schema="myfacetedsearch_cv" name="ecm_path"/>
</predicate>
```

...while the "myfacetedsearch_cv:ecm_path" field has to be of type *string[]*. This use case is currently not supported by Nuxeo, so we have to find a workaround.

**1.** Creating a fake "ecm:path" field to query

* In the left menu, go to *Settings > Registries > Document Schemas*
* Add the following schema:

```json
{ schemas: {

ecm: {
@prefix: "ecm",
path: "string[]"
}

}}
```

**2.** Creating the widget

* Create a *"Featured Widgets > Template"* widget
* Attach the **select_path_tree_widget_template.xhtml** template found in this folder (or even better, use the one from your Nuxeo distribution)
* The "Operator" should be set to IN. We want STARTSWITH, which is unavailable, leading us to a third step:

**3.** Fixing the content view predicate

* Download the package, open the Jar and look for the **OSGI-INF/extensions.xml** file
* Within the file, look for **<contentview name="mycontentview">**
* Copy the whole content view contribution (or at least the whole "coreQueryPageProvider" part), put it in a new XML Extension (*Advanced Settings > XML Extensions*)
* Look for the "ecm_path" bit and replace manually the "IN" operator with **"STARTSWITH"**.
* You should end with something like:

```xml
<extension target="org.nuxeo.ecm.platform.ui.web.ContentViewService" point="contentViews">
  <contentView name="myfacetedsearch">
    <coreQueryPageProvider>
      <property name="coreSession">#{documentManager}</property>
      <whereClause docType="myfacetedsearch_cv">
         ....
        <predicate parameter="ecm:path" operator="STARTSWITH">
          <field schema="faceted_search_default_cv" name="ecm_path"/>
        </predicate>
         ....
        <fixedPart>....</fixedPart>
      </whereClause>
      <sort column="dc:title" ascending="true"/>
      <pageSize>20</pageSize>
    </coreQueryPageProvider>
  </contentView>
</extension>
```

Note that you'll have to edit this contribution to keep the "coreQueryPageProvider" part updated each time you add/change a widget, or edit the query filter/page size/sort infos.
