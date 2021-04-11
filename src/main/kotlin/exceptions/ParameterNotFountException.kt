package exceptions

class ParameterNotFountException(name: String, line: Int) : ParseException("PARAMETER NOT FOUND ${name}:${line}")