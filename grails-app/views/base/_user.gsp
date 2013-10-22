<g:if test='${issue?."$field" || doEdit}'>
    <li class="fieldcontain">
        <g:set var="vals" value='${issue."$field"}' />
        <g:if test='${! (vals instanceof List)}'>
            <g:set var="vals" value='${[vals]}' />
        </g:if>
        <span id="${field}-label" class="property-label">${label}</span>
            <g:if test="${doEdit}">
                <g:each in="${vals}">
                    <div class="property-value" >
                        <g:textField name="$field-name" value='${it.name}'/>
                    </div>
                </g:each>
            </g:if>
            <g:else>
                <span class="property-value" aria-labelledby="${field}-label">
                    <g:each in="${vals}" status="i" var="user">
                        ${user.displayName}<g:if test="${i < vals.size()-1}">, </g:if>
                    </g:each>
                </span>
            </g:else>
    </li>
</g:if>
