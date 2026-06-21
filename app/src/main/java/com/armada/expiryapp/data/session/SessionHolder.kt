package com.armada.expiryapp.data.session

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionHolder @Inject constructor() {
    var merchandiser: String = ""
    var salesman:     String = ""
    var outletName:   String = ""
    var outletCode:   String = ""

    fun set(merchandiser: String, salesman: String, outletName: String, outletCode: String) {
        this.merchandiser = merchandiser
        this.salesman     = salesman
        this.outletName   = outletName
        this.outletCode   = outletCode
    }
}
