#!/usr/bin/env bash

usage() {
cat<<EOF

Run the JMeter test

$1
EOF
  exit 1
}

cd /tmp
mvn clean verify
tail -f /dev/null