#!/usr/bin/env bash

usage() {
cat<<EOF

Run the JMeter test

$1
EOF
  exit 1
}

cd /tmp/src/test/jmeter
#jmeter -n -t Agent-Smith.jmx -l /tmp/results/output.jtl -q user.properties
mvn clean verify
tail -f /dev/null