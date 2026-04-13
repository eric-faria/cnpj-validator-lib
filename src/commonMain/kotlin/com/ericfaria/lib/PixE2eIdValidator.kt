package com.ericfaria.lib

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
object PixE2eIdValidator {
    private val E2E_REGEX = Regex("^E[0-9]{8}[0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])([01][0-9]|2[0-3])[0-5][0-9][a-zA-Z0-9]{11}$")

    fun isValid(e2eId: String?): Boolean {
        if (e2eId.isNullOrBlank() || e2eId.length != 32) return false
        return E2E_REGEX.matches(e2eId)
    }
}