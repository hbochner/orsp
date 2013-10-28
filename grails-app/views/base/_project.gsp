<g:if test="${! doCreate}">
<div class="project-header">
    <span class="project-key">${issue?.key}:</span>
    <span class="project-title">${issue?.summary}</span>
</div>
</g:if>

<div id="tabs">
    <ul>
        <li><a href="#details">Details</a></li>
        <li><a href="#comments">Comments</a></li>
        <li><a href="#attachments">Attachments</a></li>
    </ul>

    <div id="details">
        <ol class="property-list">
            <g:if test="${doEdit}">
                <g:render template="/base/text_field"
                          model="${[field: 'summary', label: 'Project Title']}"/>
            </g:if>
            ${body()}
            <g:render template="/base/date_field"
                      model="${[field: 'expiration', label: 'Expiration Date']}"/>
        </ol>
    </div>

    <div id="comments">
        <g:if test="${issue?.comment?.comments}">
            <g:each in="${issue?.comment?.comments}" var="cmt">
                <div class="item">
                    <div class="author">
                        Added: ${cmt.created?.substring(0, 10)} by ${cmt.author?.displayName}
                    </div>
                    <div class="comment">
                        ${cmt.body}
                    </div>
                </div>
            </g:each>
        </g:if>
        <g:else>
            <div class="item">There are not yet any comments on this project</div>
        </g:else>
        <div>
            <div>Add a comment</div>
            <g:form action="addComment">
                <g:hiddenField name="id" value="${issue?.key}"/>
                <g:textArea name="comment" />
                <div>
                    <g:submitButton name="Add"/>
                </div>
            </g:form>
        </div>
    </div>

    <div id="attachments">
        <g:if test="${issue?.attachment}">
            <table>
                <tr>
                    <th>File</th>
                    <th>Created</th>
                    <th>Uploaded by</th>
                </tr>
                <g:each in="${issue?.attachment}" var="item" status="i">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                        <td><a href="${item.content}" target="_blank">${item.filename}</a></td>
                        <td>${item.created?.substring(0, 10)}</td>
                        <td>${item.author?.displayName}</td>
                    </tr>
                </g:each>
            </table>
        </g:if>
        <g:else>
            There are not yet any attachments to this project
        </g:else>
    </div>
</div>
