fun main() {
    val parse = Parser.Companion::evaluate

    println(parse(readLine() ?: error("Enter program")))
}