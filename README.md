A fork of JaCoCo used by the Java fuzzer [Jazzer](https://github.com/CodeIntelligenceTesting/jazzer).

Build the release with (you may have to adjust the path to the OpenJDK 8):
```
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/ JDK_VERSION=8 mvn -f org.jacoco.build -Djdk.version=$JDK_VERSION package -pl '!org.jacoco:org.jacoco.doc,!org.jacoco:org.jacoco.core.test.validation.java5,!org.jacoco:jacoco'
```

The agent and cli JARs can then be retrieved from the `target` directory in the respective subproject.
