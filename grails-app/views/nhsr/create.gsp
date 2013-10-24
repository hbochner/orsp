<%--
  Created by IntelliJ IDEA.
  User: hbochner
  Date: 10/3/13
  Time: 4:06 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="NHSR Project"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<div id="edit-issue" class="content scaffold-show" role="main">
    <h1><g:message code="default.create.label" args="[entityName]"/></h1>
    <g:form>
        <g:render template="nhsr_fields" model="${[doEdit: true]}"/>

        <g:render template="/base/createNav" />
    </g:form>
</div>
</body>
</html>