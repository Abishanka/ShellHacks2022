package main.models

class optionModel(var a : String, var b : String) {
    var securityId = a
    var qry = b

    def getQry() {
        return qry
    }

    def getSecurityId() {
        return securityId
    }
}