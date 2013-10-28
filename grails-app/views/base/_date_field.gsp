<g:render template="/base/text_field"/>
<r:script disposition="defer">
$("#${field}").datepicker({changeMonth: true, changeYear: true, dateFormat: "yy-mm-dd"});
</r:script>
