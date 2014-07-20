<%@ page pageEncoding="UTF-8" session="false" %>

<html>
<title>RESTful Spring Security</title>
<meta charset="UTF-8">
<body>
<h1>RESTful Spring Security Demo</h1>

<ul>
	<li><a href="login">login link here, quite useless actually</a></li>
	<li><a href="test">shows your security context</a></li>
	<li><a href="logout">logout link - just informative</a></li>
	<li><a href="random.jsp">directly accessible JSP page, phooey</a></li>
	<li><a href="admin">role ADMIN only</a></li>
	<li><a href="secure/service1">secure/service1 - authorized only</a></li>
	<li><a href="secure/special">secure/special - ROLE_SPECIAL role only</a></li>
	<li><a href="secure/allusers">secure/allusers - lists all active users - ADMIN role only</a></li>
</ul>

</body>
</html>