#!/usr/bin/env bash
cd src/main/java
SOURCE_FILES=$(find . -name "*.java" \! -regex ".*model.*" \! -regex ".*\/RemoteProcessClient\.java" \! -regex ".*\/Runner\.java" \! -regex ".*\/Strategy\.java")
echo "${SOURCE_FILES}" | zip ../../../solution.zip -@