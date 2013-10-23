<%--
  Created by IntelliJ IDEA.
  User: hbochner
  Date: 10/8/13
  Time: 4:26 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="Project" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
    </ul>
</div>
<div id="list-project" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <g:sortableColumn property="key" title="Project" />
            <g:sortableColumn property="summary" title="Title" />
        </tr>
        </thead>

        <tbody>
        <g:each in="${projectList}" status="i" var="instance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                <td>
                    <g:link action="show" id="${instance.key}" controller="${instance.typeKey}">
                        ${instance.key}
                    </g:link>
                </td>
                <td>${instance.fields.summary}</td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${instanceTotal}" />
    </div>
</div>
</body>
</html>
