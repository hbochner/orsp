package edu.mit.broad.orsp

class BaseController {

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
}
