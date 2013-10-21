<g:if test='${issue?."$field" || doEdit}'>
    <li class="fieldcontain">
        <span id="$field-label" class="property-label">${label}</span>
        <span class="property-value"
              aria-labelledby="$field-label">${issue."$field"}</span>
    </li>
</g:if>
