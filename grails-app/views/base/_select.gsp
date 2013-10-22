<g:if test='${issue?."$field" || doEdit}'>
    <li class="fieldcontain">
        <span id="${field}-label" class="property-label">${label}</span>
        <span class="property-value" aria-labelledby="${field}-label">
            <g:if test="${doEdit}">
            <g:select name="$field-id"
                      from='${issue.desc."$field".options}'
                      optionKey="id"
                      optionValue="value"
                      multiple='${issue.desc."$field".isMulti}'
                      value='${issue."$field"?.id}'/>
            </g:if>
            <g:elseif test='${issue.desc."$field".isMulti}'>
                <g:set var="max" value='${issue."$field".size() - 1}' />
                <g:each in='${issue."$field"}' status="i" var="it">
                    ${it.value}${i < max ? "," : ""}
                </g:each>
            </g:elseif>
            <g:else>${issue."$field-value"}</g:else>
        </span>
    </li>
</g:if>
