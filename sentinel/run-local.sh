#!/usr/bin/env bash

mvn \
  -P'!standard' \
  -DskipTests \
  -Dexec.waitForInterrupt=true \
  clean pre-integration-test
