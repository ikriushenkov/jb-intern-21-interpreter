import exceptions.SyntaxException

enum class Operation(val char: Char)  {
    PLUS('+') {
        override fun apply(a: Int, b: Int): Int {
            return a + b
        }
    },
    MINUS('-') {
        override fun apply(a: Int, b: Int): Int {
            return a - b
        }
    },
    MULTIPLY('*') {
        override fun apply(a: Int, b: Int): Int {
            return a * b
        }
    },
    DIVIDE('/') {
        override fun apply(a: Int, b: Int): Int {
            return a / b
        }
    },
    MOD('%') {
        override fun apply(a: Int, b: Int): Int {
            return a % b
        }
    },
    MORE('>') {
        override fun apply(a: Int, b: Int): Int {
            return boolToInt(a > b)
        }
    },
    LESS('<') {
        override fun apply(a: Int, b: Int): Int {
            return boolToInt(a < b)
        }
    },
    EQUALS('=') {
        override fun apply(a: Int, b: Int): Int {
            return boolToInt(a == b)
        }
    };
    abstract fun apply(a : Int, b : Int): Int

    companion object {
        private fun boolToInt(bool: Boolean): Int {
            return if (bool) 1 else 0
        }

        private val operations = values().associateBy { it.char }

        fun createFromChar(char : Char): Operation {
            return operations[char] ?: throw SyntaxException()
        }
    }
}