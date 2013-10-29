package edu.mit.broad.orsp

class BaseController {
    def beforeInterceptor = {
        if (! session.user) {
            session.savedParams = params
            redirect(controller: 'login', action: 'required')
            return false
        }
    }

    def show() {
        def issue = getIssue()
        issue.key = params.id
        [issue: issue]
    }

    def edit() {
        def issue = getIssue()
        issue.key = params.id
        [issue: issue]
    }

    def update() {
        def issue = getIssue()
        issue.key = params.id
        issue.setFields(params)
        issue.update()
        redirect([action: "show", id: params.id])
    }

    def create() {
        def issue = getIssue()
        [issue: issue]
    }

    def add() {
        def issue = getIssue();
        issue.setType(getType())
        issue.setFields(params);
        def key  = issue.add();
        redirect([action: "show", id: key]);
    }

    def addComment() {
        def issue = getIssue()
        issue.key = params.id
        issue.addComment(params.comment)
        redirect([action: "show", id: params.id, fragment: "comments"]);
    }

    def attach() {
        def issue = getIssue()
        issue.key = params.id
        def files = request.getMultiFileMap().files
        issue.addAttachments(files)
        render "what now?"
    }

    def getIssue() {
        if (session.user) {
            return new DynaIssueFacade(session.user.name, session.user.password)
        }
        return new DynaIssueFacade()
    }

    def getType() {
        throw new Error("sub-class must implement getType")
    }
}
