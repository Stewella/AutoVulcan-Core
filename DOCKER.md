# Docker usage for Autovulcan_core

This repository includes a multi-stage `Dockerfile` that builds the project inside a Maven image and runs the produced jar on Eclipse Temurin JRE.

Build the image (run from project root):

```powershell
docker build -t autovulcan-core:latest .
```

Run the container and map port 8080:

```powershell
docker run --rm -p 8080:8080 --name autovulcan autovulcan-core:latest
```

Optional: reuse the host Maven cache to speed subsequent builds:

```powershell
docker run --rm -p 8080:8080 -v ${env:USERPROFILE}/.m2:/root/.m2 --name autovulcan autovulcan-core:latest
```

Notes:
- The container build runs `mvn package` and needs network access to download dependencies on the first build.
- The `Dockerfile` copies `target/*-jar-with-dependencies.jar` (if present) or any `*.jar` from `target/` into the runtime image. If your artifact has a different name, update the `Dockerfile`.
- `.dockerignore` is provided to reduce build context size.
