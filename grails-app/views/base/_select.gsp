<g:if test='${issue?."$field" || doEdit}'>
    <li class="fieldcontain">
        <span id="${field}-label" class="property-label">${label}</span>
        <span class="property-value" aria-labelledby="${field}-label">
            <g:if test="${doEdit}">
                <g:set var="vals" value='${issue.desc."$field".options}' />
                <g:select name="$field-id"
                          from='${vals}'
                          optionKey="id"
                          optionValue="value"
                          multiple='${issue.desc."$field".isMulti}'
                          value='${issue."$field"?.id}'/>
                <g:if test="${child}">
                    <r:script disposition="defer">
        orsp.selectChild("${field}", "${child}", "${vals[vals.size() - 1].id}");
                    </r:script>
                </g:if>
            </g:if>
            <g:elseif test='${issue.desc."$field".isMulti}'>
                <g:set var="max" value='${issue."$field".size() - 1}' />
                <g:each in='${issue."$field"}' status="i" var="it">
                    ${it.value}${i < max ? "," : ""}
                </g:each>
            </g:elseif>
            <g:else>${issue."$field-value"}</g:else>
        </span>
        <g:if test="${doEdit && body()}">
            <div class="property-value description">${body()}</div>
        </g:if>
    </li>
</g:if>
