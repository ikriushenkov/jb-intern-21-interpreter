package exceptions

class ArgumentNumberException(name: String, line: Int)
    : ParseException("ARGUMENT NUMBER MISMATCH ${name}:${line}")