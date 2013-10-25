<g:render template="/base/project">
<ol class="property-list">
    <g:render template="/base/text_field"
              model="${[field: 'key', label: 'Project', doEdit: false]}"/>

    <g:render template="/base/text_field" model="${[field: 'summary', label: 'Project Title']}"/>
    <g:render template="/base/user" model="${[field: 'pi', label: 'PI']}"/>

    <g:render template="/base/select"
              model="${[field: 'affiliation', label: 'PI Affiliation', child: 'affiliationOther']}"/>
    <g:render template="/base/text_field"
              model="${[field: 'affiliationOther', label: 'PI Other Affiliation']}"/>

    <g:render template="/base/textarea"
              model="${[field: 'description', label: "Project Description"]}"/>

    <g:render template="/base/user"
              model="${[field: 'respParty', label: 'Broad Responsible Party']}"/>
    <g:render template="/base/radio_group"
              model="${[field: 'cited', label: 'Will Broad be Cited?']}">
        Will the authors of any future publications resulting from this project
        cite the Broad as their institutional affiliation?
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'manage', label: 'Broad manage DB\'s?']}">
        Will the Broad manage establishment of project-specific information
        into any public/protected databases?
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'federal', label: 'Federal Funding?']}">
        Will the Broad receive any direct federal funding for this project?
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'identity', label: 'Key to Identity?']}">
        If samples/data are coded, will Broad staff members receive a key
        to the code that would enable access to identifiable private information?
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'accurate', label: 'Accurate?']}">
        I attest that the information provided above is accurate and complete.
    </g:render>
    %{--<g:render template="/base/date_field"--}%
              %{--model="${[field: 'expiration', label: 'Expiration Date']}"/>--}%
</ol>
</g:render>