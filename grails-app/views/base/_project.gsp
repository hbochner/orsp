<div id="tabs">
    <ul>
        <li><a href="#details">Details</a></li>
        <li><a href="#comments">Comments</a></li>
        <li><a href="#attachments">Attachments</a></li>
    </ul>

    <div id="details">
        ${body()}
    </div>

    <div id="comments">
        <g:if test="${issue?.comment?.comments}">
            <g:each in="${issue?.comment?.comments}" var="cmt">
                <div>
                    <div>
                        Created: ${cmt.created?.substring(0, 10)} by ${cmt.author?.displayName}
                    </div>

                    <div>
                        ${cmt.body}
                    </div>
                </div>
            </g:each>
        </g:if>
        <g:else>
            There are not yet any comments on this project
        </g:else>
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