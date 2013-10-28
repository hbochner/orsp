<g:render template="/base/project">
    %{--<g:render template="/base/text_field"--}%
              %{--model="${[field: 'protocol', label: 'Protocol #']}"/>--}%
    %{--<g:render template="/base/textarea"--}%
              %{--model="${[field: 'description', label: "Scientific Summary"]}"/>--}%

    %{--<g:render template="/base/user" model="${[field: 'pi', label: 'PI']}"/>--}%
    %{--<g:render template="/base/select"--}%
              %{--model="${[field: 'affiliation', label: 'PI Affiliation', child: 'affiliationOther']}"/>--}%
    %{--<g:render template="/base/text_field"--}%
              %{--model="${[field: 'affiliationOther', label: 'PI Other Affiliation']}"/>--}%

    %{--<g:render template="/base/text_field"--}%
              %{--model="${[field: 'pm-name', label: 'Project Manager']}"/>--}%

    %{--<g:render template="/base/select"--}%
              %{--model="${[issue: issue, field: 'funding', label: 'Funding Source',--}%
                      %{--child: 'fundingOther']}">--}%
        %{--The funding source(s) for a project reviewed by ORSP.--}%
    %{--</g:render>--}%
    %{--<g:render template="/base/text_field"--}%
              %{--model="${[field: 'fundingOther', label: 'Other Funding']}">--}%
        %{--If 'other', name of funding source.--}%
    %{--</g:render>--}%

    %{--<g:render template="/base/select"--}%
              %{--model="${[field: 'irb', label: 'Preferred IRB']}"/>--}%

    %{--<g:render template="/base/text_field"--}%
              %{--model="${[field: 'rationale', label: 'IRB rationale']}">--}%
        %{--Brief rationale for IRB selection (optional)--}%
    %{--</g:render>--}%
    %{--<g:render template="/base/radio_group"--}%
              %{--model="${[field: 'dbgap', label: 'dbGAP?', child: 'responsible']}">--}%
        %{--Will data be submitted to dbGAP?--}%
    %{--</g:render>--}%
    %{--<g:render template="/base/radio_group"--}%
              %{--model="${[field: 'responsible', label: 'Broad Responsible']}">--}%
        %{--If data will be submitted to dbGAP, is Broad responsible for this submission?--}%
    %{--</g:render>--}%
</g:render>
