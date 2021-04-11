package exceptions

class ParserRuntimeException(expression: String, line: Int) : RuntimeException("RUNTIME ERROR ${expression}:${line}")