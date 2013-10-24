<%--
  Created by IntelliJ IDEA.
  User: hbochner
  Date: 10/23/13
  Time: 2:06 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>Login Required</title>
</head>
<body>

<h1>You must log in for this operation</h1>

%{--work out a way to pass on arguments--}%
<g:render template="/fragments/loginFormTemplate" />

</body>
</html>