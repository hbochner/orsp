<g:if test='${issue?."$field" || doEdit}'>
    <li class="fieldcontain" id="${field}-item">
        <span id="${field}-label" class="property-label">${label}</span>
        <span class="property-value" aria-labelledby="${field}-label">
            <g:if test="${doEdit}">
                <g:set var="vals" value='${issue.desc."$field".options}'/>
                <g:radioGroup name="$field-id"
                              values='${vals*.id}'
                              labels='${issue.desc."$field".options.collect { it.value }}'
                              value='${issue."$field"?.id}'>
                    ${it.radio} ${it.label}
                </g:radioGroup>
                <g:if test="${child}">
                    <g:set var="yesId" value="${vals.find{it.value=='Yes'}.id}" />
                    <g:if test="${yesId}">
                        <r:script disposition="defer">
        orsp.radioChild("${field}", "${child}", "${yesId}");
                        </r:script>
                    </g:if>
                </g:if>
            </g:if>
            <g:else>${issue."$field-value"}</g:else>
        </span>
        <g:if test="${doEdit && body()}">
            <div class="property-value description">${body()}</div>
        </g:if>
    </li>
</g:if>
