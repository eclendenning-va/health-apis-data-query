#!/usr/bin/env bash

#
# This script generates test keystore and truststores for use with
# unit tests. 
#


[ $# != 1 ] && echo "$0 <directory>" && exit 1

OUTPUT_DIR="$1"
KEYSTORE="$OUTPUT_DIR/test-keystore.jks"
TRUSTSTORE="$OUTPUT_DIR/test-truststore.jks"
CERT="$OUTPUT_DIR/test-cert.jks"
PASSWORD=secret
 
[ -f "$KEYSTORE" ] && rm -vf "$KEYSTORE"
[ -f "$TRUSTSTORE" ] && rm -vf "$TRUSTSTORE"
[ -f "$CERT" ] && rm -vf "$CERT"

keytool -genkey -alias test -keystore "$KEYSTORE" -storepass $PASSWORD > /dev/null 2>&1 << EOF
test
test
test
test
test
tt
yes

EOF

keytool -exportcert -keystore "$KEYSTORE" -storepass $PASSWORD -alias test -file "$CERT" > /dev/null 2>&1
keytool -importcert -keystore "$TRUSTSTORE" -alias test -storepass $PASSWORD -file "$CERT" -noprompt > /dev/null 2>&1

cat <<EOF

Created keystore, cert, and truststore.
Keystore ...... $KEYSTORE
Certificate ... $CERT
Truststore .... $TRUSTSTORE
Alias ......... test
Password ...... $PASSWORD

EOF

