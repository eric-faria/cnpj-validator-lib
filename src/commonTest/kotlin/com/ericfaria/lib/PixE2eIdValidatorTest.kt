package com.ericfaria.lib

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PixE2eIdValidatorTest {

    @Test
    fun shouldValidateCorrectE2eId() {
        val validE2e = "E00000000202604130900abcdef12345"
        assertTrue(PixE2eIdValidator.isValid(validE2e), "Deveria aceitar E2E padrão de 32 caracteres")
    }

    @Test
    fun shouldRejectWrongPrefix() {
        assertFalse(PixE2eIdValidator.isValid("A00000000202604130900abcdef12345"))
    }

    @Test
    fun shouldRejectInvalidLength() {
        assertFalse(PixE2eIdValidator.isValid("E00000000202604130900abcdef1234"))
        assertFalse(PixE2eIdValidator.isValid("E00000000202604130900abcdef123456"))
    }

    @Test
    fun shouldRejectInvalidDateComponents() {
        assertFalse(PixE2eIdValidator.isValid("E00000000202613130900abcdef12345"))
        assertFalse(PixE2eIdValidator.isValid("E00000000202604320900abcdef12345"))
        assertFalse(PixE2eIdValidator.isValid("E00000000202604132500abcdef12345"))
    }

    @Test
    fun shouldRejectNullOrBlank() {
        assertFalse(PixE2eIdValidator.isValid(null))
        assertFalse(PixE2eIdValidator.isValid(" "))
    }
}