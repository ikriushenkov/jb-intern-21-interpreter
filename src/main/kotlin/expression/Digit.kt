package expression

import exceptions.SyntaxException

class Digit(private val value: Char) {
    init {
        if (value < '0' || value > '9') {
            throw SyntaxException()
        }
    }

    fun getInt() : Int {
        return value - '0'
    }
}