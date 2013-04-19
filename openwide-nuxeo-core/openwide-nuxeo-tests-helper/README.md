Open Wide : Nuxeo Tests Helper
==============================

This repository provides:

* An extension point to easily reconfigure directories for tests
* An abstract test class, that lays down some basics and logs the contents of your repository

**Module state:** Put into production

## Sql Directories Fixer

This extension point fixes the 'datasource lookup' errors when manipulating directories from tests, by making each directory use the embedded database instead. Sample contribution:

```
<?xml version="1.0"?>
<component name="myproject.directoriesfixer.test.contrib">

  <require>(the components that first registered the directories to fix)</require>

  <extension target="fr.openwide.nuxeo.test.directories.SqlDirectoriesFixer" point="directories">
    <directory name="mydirectory1" />
    <directory name="mydirectory2" />
    <directory name="userDirectory" />
    <directory name="groupDirectory" />
  </extension>
  
</component>
```

To make the contribution work, you have both to deploy it and deploy the `tests-helper` bundle that provides the extension point:

```
@Deploy({
    "fr.openwide.nuxeo.commons.testshelper"
})
@LocalDeploy({
    "mybundle:OSGI-INF/fix-directories-contrib.xml"
})
@RunWith(FeaturesRunner.class)
@Features(PlatformFeature.class)
public class MyTest {
    ...
    
```
