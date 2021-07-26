package io.sunil.testingandroid


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest{

    @Test
    fun `empty userName return false`()
    {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "123",
            "123"
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `validate userName and correctly repeated password returns true`()
    {
        val result = RegistrationUtil.validateRegistrationInput(
            "test",
            "123",
            "123"
        )

        assertThat(result).isTrue()
    }

    @Test
    fun `userName already exists return false`()
    {
        val result = RegistrationUtil.validateRegistrationInput(
            "Sunil",
            "123",
            "123"
        )

        assertThat(result).isFalse()
    }


    @Test
    fun `empty password return false`()
    {
        val result = RegistrationUtil.validateRegistrationInput(
            "Sunil",
            "",
            ""
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `incorrectly confirm password return false`()
    {
        val result = RegistrationUtil.validateRegistrationInput(
            "Sunil",
            "123",
            "321"
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `password contain less than 2 digits return false`()
    {
        val result = RegistrationUtil.validateRegistrationInput(
            "Sunil",
            "1",
            "1"
        )

        assertThat(result).isFalse()
    }
}