<g:if test='${issue?."$field" || doEdit}'>
    <li class="fieldcontain">
        <span id="$field-label" class="property-label">${label}</span>
        <span class="property-value" aria-labelledby="$field-label">
            <g:if test="${doEdit}">
                <g:radioGroup name="dbgap-id"
                              values='${issue.desc."$field".options.collect { it.id }}'
                              labels='${issue.desc."$field".options.collect { it.value }}'
                              value='${issue."$field"?.id}'>
                    ${it.radio} ${it.label}
                </g:radioGroup>
            </g:if>
            <g:else>${issue."$field-value"}</g:else>
        </span>
    </li>
</g:if>
