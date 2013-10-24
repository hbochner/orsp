package edu.mit.broad.orsp

class BaseController {
    def openidService

    def beforeInterceptor = {
        if (! session.user) {
            session.savedParams = params
            redirect(controller: 'login', action: 'required')
            return false
        }
    }

    def getType() {
        throw new Error("sub-class must implement getType")
    }

    def index() { }

    def show() {
        def issue = new DynaIssueFacade()
        issue.key = params.id
        [issue: issue]
    }

    def edit() {
        def issue = new DynaIssueFacade()
        issue.key = params.id
        [issue: issue]
    }

    def update() {
        def issue = new DynaIssueFacade()
        issue.key = params.id
        issue.setFields(params)
        issue.update()
        redirect([action: "show", id: params.id])
    }

    def create() {
        def issue = new DynaIssueFacade()
        [issue: issue]
    }

    def add() {
        def issue = new DynaIssueFacade();
        issue.setType(getType())
        issue.setFields(params);
        def key  = issue.add();
        redirect([action: "show", id: key]);
    }
}
