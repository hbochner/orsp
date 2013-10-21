<ol class="property-list">
    <g:render template="/base/text_field" model="${[field: 'key', label: 'Key', doEdit: false]}" />

    <g:render template="/base/text_field" model="${[field: 'summary', label: 'Summary']}" />
    <g:render template="/base/radio_group" model="${[field: 'dbgap', label: 'dbGAP?']}" />
</ol>
