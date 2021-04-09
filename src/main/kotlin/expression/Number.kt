package expression

class Number(private val value : List<Digit>) {
    fun toInt() : Int {
        return value.fold(0) { sum, digit -> sum * 10 + digit.getInt() }
    }

    constructor(string : String) : this(string.map { Digit(it) })

    constructor(int : Int) : this(int.toString())
}