#!/bin/bash

URL=http://localhost:8080
CURL="curl -i -s -w '\n'"
OK='grep -q "200 OK"'
UNAUTHORIZED='grep -q "401 Unauthorized"'
FORBIDDEN='grep -q "403 Forbidden"'

TESTS=0
FAILED=0

function ok() {
	TESTS=$((TESTS + 1))
	echo OK
}

function fail() {
	TESTS=$((TESTS + 1))
	FAILED=$((FAILED + 1))
	echo FAILED
}

echo -n "Admin /secure/allusers 200... "
X_AUTH_TOKEN=$($CURL -X POST -H "X-Username: admin" -H "X-Password: admin" $URL/ | grep "X-Auth-Token" | cut -d' ' -f2) &&
	$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/secure/allusers | eval $OK && ok || fail

echo -n "Admin role check /test... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/test | grep -q "Granted Authorities: ADMIN" && ok || fail

echo -n "Admin /admin 200... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/admin | eval $OK && ok || fail

echo -n "Admin /secure/special 403... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/secure/special | eval $FORBIDDEN && ok || fail

echo -n "Admin /logout POST 200... "
$CURL -X POST -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/logout | eval $OK && ok || fail

echo -n "Admin repeated /logout POST 401... "
$CURL -X POST -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/logout | eval $UNAUTHORIZED && ok || fail

echo -n "Admin /logout POST without token falls through to controller 405... "
$CURL -X POST $URL/logout | grep -q "405 Method Not Allowed" && ok || fail

echo
echo -n "Anonymous / 200... "
$CURL $URL/ | eval $OK && ok || fail

echo -n "Anonymous /secure/service1 401... "
$CURL $URL/secure/service1 | eval $UNAUTHORIZED && ok || fail

echo -n "Invalid token / 401... "
$CURL -H "X-Auth-Token: invalid" $URL/admin | eval $UNAUTHORIZED && ok || fail

echo
echo -n "Special /secure/service1 200... "
X_AUTH_TOKEN=$($CURL -X POST -H "X-Username: special" -H "X-Password: special" $URL/ | grep "X-Auth-Token" | cut -d' ' -f2) &&
	$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/secure/service1 | eval $OK && ok || fail

echo -n "Special /secure/special 200... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/secure/special | eval $OK && ok || fail

echo -n "Special /secure/allusers 403... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/secure/allusers | eval $FORBIDDEN && ok || fail

echo -n "Special /admin 403... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/admin | eval $FORBIDDEN && ok || fail

echo -n "Special relogin... "
X_AUTH_OLD=$X_AUTH_TOKEN
X_AUTH_TOKEN=$($CURL -X POST -H "X-Auth-Token: $X_AUTH_TOKEN" -H "X-Username: special" -H "X-Password: special" $URL/ | grep "X-Auth-Token" | cut -d' ' -f2) &&
	$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/secure/service1 | eval $OK && ok || fail

echo -n "Special using old token 401... "
$CURL -H "X-Auth-Token: $X_AUTH_OLD" $URL/secure/service1 | eval $UNAUTHORIZED && ok || fail

echo
echo "Tests: $TESTS, Failed: $FAILED"