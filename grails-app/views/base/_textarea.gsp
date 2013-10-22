    <g:if test="${doEdit}">
    <li class="fieldcontain" id="${field}-item">
        <span id="${field}-label" class="property-label">${label}</span>
        <span class="property-value" aria-labelledby="${field}-label">
            <g:textArea name="${field}" value='${issue?."$field"}'
                         id="${field}-input"/>
        </span>
        <g:if test="${body()}">
            <div class="property-value description">${body()}</div>
        </g:if>
    </li>
</g:if>
<g:else>
    <g:if test='${issue?."$field"}'>
        <li class="fieldcontain">
            <span id="${field}-label" class="property-label">${label}</span>
            <span class="property-value" aria-labelledby="title-label">${issue."$field"}</span>
        </li>
    </g:if>
</g:else>
