package expression

import exceptions.SyntaxException

class Character(val char : Char) {
    init {
        if (!isCharacter(char)) {
            throw SyntaxException()
        }
    }

    companion object {
        fun isCharacter(char: Char): Boolean {
            return char.isLetter() || char == '_'
        }
    }
}