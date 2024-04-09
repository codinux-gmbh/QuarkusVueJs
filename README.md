# Quarkus Vue.js

Show case how to use Vue.js 3 with Quarkus 3 including Gradle, WebJars and Kotlin.

## Run

Execute
```shell
./gradlew quarkusDev
```

and then navigate in your browser to http://localhost:8080 .

## Relevant files

For the Vue.js application see [index.html](src/main/resources/META-INF/resources/index.html).

For how to set up Quarkus and add Vue.js via WebJars see [build.gradle.kts](build.gradle.kts).

## Vue with build setup

To use e.g. Single File Components (SFC), Vue needs to be integrated into a build setup wit Node / Yarn / Bun / ... so that
*.vue files get pre-compiled to standard JavaScript and CSS files.

For an example how to do this see branch `node-build`.

