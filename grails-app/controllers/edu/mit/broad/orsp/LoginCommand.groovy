package edu.mit.broad.orsp

/**
 * Created with IntelliJ IDEA.
 * User: hbochner
 * Date: 10/24/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
class LoginCommand {
    String name
    String password
    static constraints = {
        name blank: false
        password blank: false
    }
}
