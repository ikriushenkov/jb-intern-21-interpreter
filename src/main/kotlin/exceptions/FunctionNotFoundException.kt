package exceptions

class FunctionNotFoundException(name: String, line: Int) : ParseException("FUNCTION NOT FOUND ${name}:${line}")