package edu.mit.broad.orsp

class LoginTagLib {
    static namespace = "login"

    def isLoggedIn = { attrs, body ->
        if (session.user) {
            out << body()
        }
    }

    def notLoggedIn = { attrs, body ->
        if (! session.user) {
            out << body()
        }
    }
}
