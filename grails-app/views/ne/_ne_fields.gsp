<ol class="property-list">
    <g:render template="/base/text_field"
              model="${[field: 'key', label: 'Project', doEdit: false]}"/>

    <g:render template="/base/text_field" model="${[field: 'summary', label: 'Summary']}"/>
    <g:render template="/base/user" model="${[field: 'pi', label: 'PI']}"/>

    <g:render template="/base/select"
              model="${[field: 'affiliation', label: 'PI Affiliation', child: 'affiliationOther']}"/>
    <g:render template="/base/text_field"
              model="${[field: 'affiliationOther', label: 'PI Other Affiliation']}"/>

    <g:render template="/base/user"
              model="${[field: 'respParty', label: 'Responsible Party']}"/>
    <g:render template="/base/radio_group"
              model="${[field: 'cited', label: 'Will Broad be Cited?']}">
        Will the authors of any future publications resulting from this project
        cite the Broad as their institutional affiliation?
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'manage', label: 'Broad manage DB\'s?']}">
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'federal', label: 'Federal Funding?']}">
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'identity', label: 'Key to Identity?']}">
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'accurate', label: 'Accurate?']}">
    </g:render>
    %{--<g:render template="/base/date_field"--}%
              %{--model="${[field: 'expiration', label: 'Expiration Date']}"/>--}%
</ol>
