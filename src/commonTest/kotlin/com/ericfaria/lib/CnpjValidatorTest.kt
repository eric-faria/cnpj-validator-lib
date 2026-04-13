package com.ericfaria.lib

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CnpjValidatorTest {

    @Test
    fun shouldValidateClassicNumericCnpj() {
        assertTrue(CnpjValidator.isValid("19.131.243/0001-97"))
        assertTrue(CnpjValidator.isValid("19131243000197"))
    }

    @Test
    fun shouldValidateNewAlphanumericCnpj() {
        assertTrue(CnpjValidator.isValid("12ABC34501DE35"))
    }

    @Test
    fun shouldRejectInvalidCnpjDigits() {
        assertFalse(CnpjValidator.isValid("19.131.243/0001-98"))
    }

    @Test
    fun shouldRejectRepeatedSequences() {
        assertFalse(CnpjValidator.isValid("00000000000000"))
        assertFalse(CnpjValidator.isValid("11111111111111"))
    }

    @Test
    fun shouldReturnFalseForEmptyOrNull() {
        assertFalse(CnpjValidator.isValid(null))
        assertFalse(CnpjValidator.isValid(""))
        assertFalse(CnpjValidator.isValid("   "))
    }
}