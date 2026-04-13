package com.ericfaria.lib

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
object CnpjValidator {

    private val WEIGHTS_FIRST_DIGIT = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
    private val WEIGHTS_SECOND_DIGIT = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

    fun isValid(cnpj: String?): Boolean {
        if (cnpj.isNullOrBlank()) return false

        val cleanCnpj = cnpj.replace(Regex("[^a-zA-Z0-9]"), "").uppercase()

        if (cleanCnpj.length != 14 || cleanCnpj.all { it == cleanCnpj[0] }) {
            return false
        }

        val values = cleanCnpj.map { char ->
            if (char.isDigit()) char.digitToInt() else char.code - 48
        }

        val calcFirst = calculateVerifierDigit(values.take(12), WEIGHTS_FIRST_DIGIT)
        val calcSecond = calculateVerifierDigit(values.take(13), WEIGHTS_SECOND_DIGIT)

        return values[12] == calcFirst && values[13] == calcSecond
    }

    private fun calculateVerifierDigit(baseValues: List<Int>, weights: IntArray): Int {
        var sum = 0
        for (i in baseValues.indices) {
            sum += baseValues[i] * weights[i]
        }
        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }
}