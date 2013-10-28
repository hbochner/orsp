<g:render template="/base/project">
    <g:render template="/base/user"
              model="${[field: 'respParty', label: 'Broad Responsible Party']}"/>
    <g:render template="/base/user"
              model="${[field: 'contact', label: 'Broad Contact']}"/>
    <g:render template="/base/text_field"
              model="${[field: 'source', label: 'Source of Samples/External Collaborator']}"/>

    <g:render template="/base/radio_group"
              model="${[field: 'idr', label: 'Initial Determination Request?']}"/>
    <g:render template="/base/radio_group"
              model="${[field: 'clarification', label: 'Clarification Request?']}">
        To ascertain whether a change in the project (e.g. new cohort, change in technology)
        affects a previous determination.
    </g:render>

    <g:render template="/base/select"
              model="${[issue: issue, field: 'funding', label: 'Funding Source',
                      child: 'fundingOther']}">
        The funding source(s) for a project reviewed by ORSP.
    </g:render>
    <g:render template="/base/text_field"
              model="${[field: 'fundingOther', label: 'Other Funding']}"/>

    <g:render template="/base/radio_group"
              model="${[field: 'research', label: 'Research?']}">
        Is the activity research (a systematic investigation designed to develop
        or contribute to generalizable knowledge)?
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'living', label: 'Living Subject?']}">
        Does the research involve a living individual, about whom an investigator conducting research
        obtains either:
        <ol>
            <li>data through intervention or interaction OR</li>
            <li>identifiable private information.  Coded data/specimens are considered identifiable if
            the researcher has access to a key that could link the code to private information.</li>
        </ol>
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'collAnalysis', label: 'Collaborate on Analysis?']}">
        If specimens are received from an external investigator who has access
        to subject identifiers, will the Broad investigator collaborate with that individual
        on data interpretation or analysis?
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'collPublication', label: 'Collaborate on Publication?']}">
        If specimens are received from an external investigator who has access
        to subject identifiers, will the Broad investigator collaborate with that individual
        on publications or presentations related to the research?
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'available', label: 'Available?']}">
        Are the biological materials to be used in this project commercially
        or otherwise publicly available?
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'established', label: 'Established?']}">
        Does the project involve an already established cell line from which
        the identity of the donor(s) cannot be readily ascertained?
    </g:render>
    <g:render template="/base/radio_group"
              model="${[field: 'determined', label: 'Determined NHSR?']}">
        Has the IRB of the institution from which samples will be received
        determined the project to be “not human subjects research”?
    </g:render>
    <g:render template="/base/textarea"
              model="${[field: 'description', label: "Project Summary"]}"/>
    <g:render template="/base/radio_group"
              model="${[field: 'accurate', label: 'Accurate?']}">
        I attest that the information provided above is accurate and complete.
    </g:render>
</g:render>
