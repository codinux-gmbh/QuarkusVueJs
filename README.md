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

The development files of the Vue.js application are stored under [src/main/webapp](src/main/webapp).

When the application gets build, the task `buildVueJsApp`, which gets executed before `quarkusBuild`, transpiles
the Vue.js components and copies the resulting files to 
[src/main/resources/META-INF/resources](src/main/resources/META-INF/resources), see [build.gradle.kts](build.gradle.kts).

For development there are two options:

### Run Backend (Quarkus) and Frontend (Vite) in separate processes

Run backend:
```shell
./gradlew quarkusDev
```
-> Quarkus backend gets served under port 8080.

Run frontend (from a second terminal):
```shell
cd src/main/webapp

npm run dev
```
-> the frontend gets (most likely) served under port 5173.

The advantage is, changes to .vue files automatically reload the site in browser.

The disadvantages are
- You have to hard code the port of the backend in frontend files, which may be undesired if e.g. running the backend during development under a different port than in production.
- During development you have to enable CORS in Quarkus backend as the frontend is now being served from a different port than the backend (see [src/main/resources/application.properties](src/main/resources/application.properties)).

### Add watch for Vue.js files and serve frontend and backend with Quarkus app

For this I added the script `watch` to [package.json](src/main/webapp/package.json).

The task `quarkusDev` automatically starts this script, see [build.gradle.kts](build.gradle.kts).

As frontend and backend are now served from the same port also during development neither CORS has to be enabled nor 
has the backend port to be specified in frontend files.

Disadvantage:
- The browser doesn't get reloaded on changes, site has to be manually reloaded.