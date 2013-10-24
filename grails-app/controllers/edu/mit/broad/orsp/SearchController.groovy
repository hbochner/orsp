package edu.mit.broad.orsp

class SearchController {
    def beforeInterceptor = {
        if (! session.user) {
            session.savedParams = params
            redirect(controller: 'login', action: 'required')
            return false
        }
    }

    def list() {
        def max = params.int('max') ?: 10
        def offset = params.int('offset') ?: 0
        def issue = new DynaIssueFacade()
        def total = issue.issueCount(params)
        def list = issue.search(params, max, offset)
        [projectList: list, instanceTotal: total, offset: offset]
    }
}
