#!/usr/bin/env bash

#
# Updates your cacerts trust store in your Java home to trust
# the Lighthouse development certs, internal-sys-dev.
#
# You will probably need root access to modify the trust store.
#

cd $(dirname $0)/../..

[ -z "$JAVA_HOME" ] && echo "JAVA_HOME not set" && exit 1
[ -z "$HEALTH_API_CERTIFICATE_PASSWORD" ] && echo "HEALTH_API_CERTIFICATE_PASSWORD not set" && exit 1
[ -z "$TRUST_STORE" ] && TRUST_STORE="$JAVA_HOME/jre/lib/security/cacerts"
[ ! -f "$TRUST_STORE" ] && echo "Trust store not found: $TRUST_STORE" && exit 1
[ -z "$TRUST_STORE_PASSWORD" ] && TRUST_STORE_PASSWORD=changeit

KEYSTORE=ids/target/certs/system/DVP-DVP-NONPROD.jks
ALIAS=internal-sys-dev

[ ! -f "$KEYSTORE" ] && echo -e "OH NOES! Keystore not found: $KEYSTORE\nTry building ids project" && exit 1

set -e

BACKUP=$(basename "$TRUST_STORE").$(date +%s)
cp "$TRUST_STORE" "$BACKUP"
echo -e "Backed up $TRUST_STORE\nto $(pwd)/$BACKUP"

OLD=$(keytool \
  -list \
  -alias $ALIAS \
  -keypass "$HEALTH_API_CERTIFICATE_PASSWORD" \
  -keystore "$TRUST_STORE" \
  -storepass "$TRUST_STORE_PASSWORD")
if [ -n "$OLD" ]
then
  echo "Removing old $ALIAS trusted certificate"
  keytool \
    -delete \
    -alias $ALIAS \
    -keypass "$HEALTH_API_CERTIFICATE_PASSWORD" \
    -keystore "$TRUST_STORE" \
    -storepass "$TRUST_STORE_PASSWORD"
fi

keytool \
  -exportcert \
  -storepass "$HEALTH_API_CERTIFICATE_PASSWORD" \
  -keystore "$KEYSTORE" \
  -alias $ALIAS \
  -file $ALIAS.crt

keytool \
  -import \
  -trustcacerts \
  -alias $ALIAS \
  -file $ALIAS.crt \
  -keypass "$HEALTH_API_CERTIFICATE_PASSWORD" \
  -keystore "$TRUST_STORE" \
  -storepass "$TRUST_STORE_PASSWORD" \
  -noprompt

