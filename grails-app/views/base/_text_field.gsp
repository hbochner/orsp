<g:if test='${issue?."$field" || doEdit}'>
    <li class="fieldcontain" id="${field}-item">
        <span id="${field}-label" class="property-label">${label}</span>
        <span class="property-value" aria-labelledby="${field}-label">
            <g:if test="${doEdit}">
                <g:textField name="$field" value='${issue?."$field"}'/>
            </g:if>
            <g:else>${issue."$field"}</g:else>
        </span>
        <g:if test="${doEdit && body()}">
            <div class="property-value description">${body()}</div>
        </g:if>
    </li>
</g:if>
