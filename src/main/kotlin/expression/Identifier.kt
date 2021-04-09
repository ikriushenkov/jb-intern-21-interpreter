package expression

class Identifier(private val string: List<Character>) {
    override fun toString(): String {
        return string.map { it.char }.joinToString()
    }

    override fun equals(other: Any?): Boolean {
        return toString() == other.toString()
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }
}