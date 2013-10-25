<%--
  Created by IntelliJ IDEA.
  User: hbochner
  Date: 10/23/13
  Time: 2:06 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="noNav">
    <title>Login</title>
</head>

<body>
<g:render template="/base/topNav" model="${[loggingIn: true]}"/>

<div id="login-form" class="content scaffold-show" role="main">
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${user}">
        <ul class="errors" role="alert">
            <g:eachError bean="${user}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                    <g:message error="${error}"/>
                </li>
            </g:eachError>
        </ul>
    </g:hasErrors>

    <g:renderErrors bean="${user}"/>

    <g:form method="post" action="login">
        <ol class="property-list">
            <g:set var="doEdit" value="true"/>
            <g:render template="/base/text_field"
                      model="${[field: 'name', label: "User Name"]}"/>
            <li class="fieldcontain">
                <span id="password-label" class="property-label">Password</span>
                <span class="property-value" aria-labelledby="password-label">
                    <g:passwordField name="password" value="${password ?: ''}"/>
                </span>
            </li>
        </ol>
        <fieldset class="buttons">
            <g:submitButton name="Login"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>