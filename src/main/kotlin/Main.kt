fun main() {
    val parse = Parser.Companion::evaluate

    val code = generateSequence(::readLine).joinToString("\n")

    println(parse(code))
}
