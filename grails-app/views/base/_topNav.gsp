<div id="header">
    <div class="app-title">
        <div class="float-left">
            <a href="#"><img src="${createLinkTo(dir: 'images', file: 'minilogo.png')}"/></a>
        </div>

        <div class="float-left">
            <div class="app-name ">
                <a href="${request.contextPath}">ORSP Portal</a>
            </div>

            <div class="app-by">
                Broad Institute
            </div>
        </div>

        <div class="float-clear"/>
    </div>

    <div class="nav">
        <ul>
            <g:if test="${! atHome}">
            <li>
                <a class="home" href="${createLink(uri: '/')}">Home</a>
            </li>
            <li>
                <a class="list" href="${createLink(uri: '/')}#search">Search</a>
            </li>
            <li>
                <a class="create" href="${createLink(uri: '/')}#create">Create</a>
            </li>
            </g:if>
            <g:if test="${!loggingIn}">
                <login:isLoggedIn>
                    <li class="float-right">
                        <g:link action="logout"
                                controller="login">${session.user.name} Logout</g:link>
                    </li>
                </login:isLoggedIn>
                <login:notLoggedIn>
                    <li class="float-right">
                        <g:link action="enter" controller="login">Login</g:link>
                    </li>
                </login:notLoggedIn>
            </g:if>
        </ul>
    </div>

</div>
