package edu.mit.broad.orsp

class LoginController {

    //static defaultAction = "require"

    def required() {
        // need a better format for errors
        flash.message = "Login required"
        render(view: 'enter', loggingIn: true)
    }

    def enter() {
        // just default
        [loggingIn: true]
    }

    def login() {
        def jira = new JiraRestService()
        if (! jira.validateUser(params.name, params.password)) {
            flash.message = "Login failed"
            redirect([action: 'enter', loggingIn: true])
            return
        }
        session.user = [name: params.name, password: params.password]
        if (session.savedParams) {
            def tmp = session.savedParams
            session.savedParams = null
            redirect(tmp)
            return
        }

        render(view: "/index.gsp")
    }

    def logout() {
        session.user = null
        redirect(uri: "/")
    }
}
