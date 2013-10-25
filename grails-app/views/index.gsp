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
    <meta name="layout" content="noNav">
    <title>ORSP</title>
</head>

<body>
<g:render template="/base/topNav" model="${[atHome: true]}"/>

<div id="tabs">
    <ul>
        <li><a href="#create">Create</a> </li>
        <li><a href="#search">Search</a> </li>
    </ul>

    <div id="create">
        <ul>
            <li><a href="irb/create">Create an IRB Project</a></li>
            <li><a href="nhsr/create">Create an NHSR Project</a> </li>
            <li><a href="ne/create">Create a 'Not Engaged' Project</a> </li>
        </ul>
    </div>

    <div id="search">
        <g:form action="list" controller="Search">
            <div>Entering search criteria has not been implemented yet;<br/>
                The Search button will display all projects.
            </div>
            <g:actionSubmit value="Search" action="list" />
        </g:form>
    </div>
</div>

</body>
</html>
