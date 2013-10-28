<fieldset class="buttons">
    <g:actionSubmit class="save" action="update"
                    value="${message(code: 'default.button.update.label', default: 'Update')}"/>
    <g:link class="cancel" action="show" id="${issue?.key}">Cancel</g:link>
</fieldset>
