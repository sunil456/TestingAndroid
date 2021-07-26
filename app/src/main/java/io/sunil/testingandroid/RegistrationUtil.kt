package io.sunil.testingandroid

object RegistrationUtil {

    private val existingUsers = listOf("Sunil", "Vijay", "Peter", "Carl")

    /**
     * the input is not valid if...
     * ... the userName/password is empty
     * ... the userName is already exists
     * ... the confirm password is not the same as the real password
     * ... the password contains less than 2 digits
     */


    fun validateRegistrationInput(
        userName: String,
        password: String,
        confirmPassword: String
    ) : Boolean{

        if (userName.isEmpty() || password.isEmpty()) return false

        if (userName in existingUsers) return false

        if (!password.equals(confirmPassword)) return false

        if (password.count { it.isDigit() } < 2) return false

        return true
    }


    /**
     * Returns the n-th fibonacci number
     * They are defined like this:
     * fib(0) = 0
     * fib(1) = 1
     * fib(n) = fib(n-2) + fib(n-2)
     */
    fun fib(num : Int) : Long
    {
        if (num == 0 || num ==1) return num.toLong()

        var a = 0L
        var b = 1L
        var c = 1L
        (1 until num).forEach { i ->
            c = a+b
            a = b
            b = c
        }
        return c
    }

}