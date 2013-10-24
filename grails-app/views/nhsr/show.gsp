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
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<div id="show-issue" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>

    <g:render template="nhsr_fields"/>

    <fieldset class="buttons">
        <g:link class="edit" action="edit" id="${issue?.key}">
            <g:message code="default.button.edit.label" default="Edit"/>
        </g:link>
    </fieldset>
</div>
</body>
</html>