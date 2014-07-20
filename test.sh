URL=http://localhost:8080/respsec
CURL="curl -i -s -w '\n'"
OK='grep -q "200 OK"'
UNAUTHORIZED='grep -q "401 Unauthorized"'
FORBIDDEN='grep -q "403 Forbidden"'

echo -n "Admin /secure/allusers 200... "
X_AUTH_TOKEN=$($CURL -X POST -H "X-Username: admin" -H "X-Password: admin" $URL/ | grep "X-Auth-Token" | cut -d' ' -f2) &&
	$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/secure/allusers | eval $OK && echo OK || echo FAILED

echo -n "Admin role check /test... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/test | grep -q "Granted Authorities: ADMIN" && echo OK || echo FAILED

echo -n "Admin /admin 200... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/admin | eval $OK && echo OK || echo FAILED

echo -n "Admin /secure/special 403... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/secure/special | eval $FORBIDDEN && echo OK || echo FAILED

echo -n "Admin /logout POST 200... "
$CURL -X POST -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/logout | eval $OK && echo OK || echo FAILED

echo -n "Admin repeated /logout POST 401... "
$CURL -X POST -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/logout | eval $UNAUTHORIZED && echo OK || echo FAILED

echo -n "Admin /logout POST without token falls through to controller 405... "
$CURL -X POST $URL/logout | grep -q "405 Method Not Allowed" && echo OK || echo FAILED

echo
echo -n "Anonymous / 200... "
$CURL $URL/ | eval $OK && echo OK || echo FAILED

echo -n "Anonymous /secure/service1 401... "
$CURL $URL/secure/service1 | eval $UNAUTHORIZED && echo OK || echo FAILED

echo -n "Invalid token / 401... "
$CURL -H "X-Auth-Token: invalid" $URL/admin | eval $UNAUTHORIZED && echo OK || echo FAILED

echo
echo -n "Special /secure/service1 200... "
X_AUTH_TOKEN=$($CURL -X POST -H "X-Username: special" -H "X-Password: special" $URL/ | grep "X-Auth-Token" | cut -d' ' -f2) &&
	$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/secure/service1 | eval $OK && echo OK || echo FAILED

echo -n "Special /secure/special 200... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/secure/special | eval $OK && echo OK || echo FAILED

echo -n "Special /secure/allusers 403... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/secure/allusers | eval $FORBIDDEN && echo OK || echo FAILED

echo -n "Special /admin 403... "
$CURL -H "X-Auth-Token: $X_AUTH_TOKEN" $URL/admin | eval $FORBIDDEN && echo OK || echo FAILED