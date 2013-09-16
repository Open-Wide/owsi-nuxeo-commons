Open Wide : Nuxeo Avatar Importer
=================================

## Introduction

This module watches a given folder to import its content as avatars.

## Documentation

Configure the folder location with:

```xml
  <extension target="fr.openwide.nuxeo.avatar.AvatarImporterService" point="config">
    <configuration>
      <dossierAvatars>/path/to/avatars</dossierAvatars>
    </configuration>
  </extension>
```

If this folder, for instance, contains a file named *Administrator.jpg* (any image extension supported), it will be set as the avatar or user *Administrator*.

By default, an import is run every minute. You can override this with:

```xml
  <extension target="org.nuxeo.ecm.core.scheduler.SchedulerService" point="schedule">
    <schedule id="avatarImporter">
      <username>USERNAME</username> <!-- Example: "Administrator" -->
      <event>avatarImport</event>
      <cronExpression>CRON EXPRESSION</cronExpression> <!-- Example: "0 * * * * ?" -->
    </schedule>
  </extension>
```
