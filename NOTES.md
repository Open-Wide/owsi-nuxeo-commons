## Notes for deploying a release

> See also [Sonatype OSS Maven Repository Usage Guide](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide)

To deploy on Sonatype repositories, you must have an authorized account on Sonatype's JIRA. Access can be requested on [Open Wide's issue](https://issues.sonatype.org/browse/OSSRH-7041).

Your `settings.xml` must then be updated with your Sonatype credentials:

```
  <servers>
    <server>
      <id>sonatype-nexus-snapshots</id>
      <username>USERNAME</username>
      <password>PASSWORD</password>
    </server>
    <server>
      <id>sonatype-nexus-staging</id>
      <username>USERNAME</username>
      <password>PASSWORD</password>
    </server>
  </servers>
```

To deploy a release, first use this tutorial to be able to sign your artifacts (required for publishing in Maven Central): [How To Generate PGP Signatures With Maven](https://docs.sonatype.org/display/Repository/How+To+Generate+PGP+Signatures+With+Maven). Finally, for each release run `mvn clean deploy -Prelease -Dgpg.passphrase=(yourpassphrase)` (the "release" profiles enables sources generation, Javadoc generation, and artifacts signing)

Before a release can be sent to Maven Central, it must first be promoted through [Sonatype's Nexus](https://oss.sonatype.org/).
