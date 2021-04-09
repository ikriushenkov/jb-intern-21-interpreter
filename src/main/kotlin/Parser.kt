import exceptions.SyntaxException
import expression.*

class Parser private constructor(string: String) {
    private val source = Source(string)

    private var line = 1

    private fun parseConstant(): ConstantExpression {
        val isNegate = source.curSym() == '-'
        val number = (if (isNegate) {
            emptyList()
        } else {
            listOf(source.curSym())
        }).toMutableList()

        while (source.nextSym().isDigit()) {
            number += source.curSym()
        }

        return ConstantExpression(
            Number(number.map { Digit(it) }),
            isNegate, line
        )
    }

    private fun parseBinary(): BinaryExpression {
        if (source.curSym() != '(') {
            throw SyntaxException()
        }
        source.nextSym()
        val firstExpression = parseExpression()
        val operation = Operation.createFromChar(source.curSym())
        source.nextSym()
        val secondOperation = parseExpression()
        if (source.curSym() != ')') {
            throw SyntaxException()
        }
        source.nextSym()
        return BinaryExpression(firstExpression, secondOperation, operation, line)
    }

    private fun parseIfExpression(): IfExpression {
        if (source.curSym() != '[') {
            throw SyntaxException()
        }
        source.nextSym()
        val ifExpression = parseExpression()
        if (source.curSym() != ']' || source.nextSym() != '?' || source.nextSym() != '{') {
            throw SyntaxException()
        }
        source.nextSym()
        val trueExpression = parseExpression()
        if (source.curSym() != '}' || source.nextSym() != ':' || source.nextSym() != '{') {
            throw SyntaxException()
        }
        source.nextSym()
        val falseExpression = parseExpression()
        if (source.curSym() != '}') {
            throw SyntaxException()
        }
        source.nextSym()
        return IfExpression(ifExpression, trueExpression, falseExpression, line)
    }

    private fun parseIdentifier(): Identifier {
        var string = source.curSym().toString()

        while (Character.isCharacter(source.nextSym())) {
            string += source.curSym()
        }

        return Identifier(string.map { Character(it) })
    }

    private fun parseVariableOrCall(): Expression {
        val identifier = parseIdentifier()

        return if (source.curSym() == '(') {
            parseCall(identifier)
        } else {
            Variable(identifier, line)
        }
    }

    private fun parseCall(identifier: Identifier): CallExpression {
        if (source.curSym() != '(') {
            throw SyntaxException()
        }
        source.nextSym()
        val argumentList = emptyList<Expression>().toMutableList()

        while (source.curSym() != ')') {
            argumentList += parseExpression()
            if (source.curSym() == ',') {
                source.nextSym()
            }
        }

        if (source.curSym() != ')') {
            throw SyntaxException()
        }
        source.nextSym()

        return CallExpression(identifier, argumentList, line)
    }

    private fun parseExpression(): Expression {
        val char = source.curSym()
        return if (char.isDigit() || char == '-') {
            parseConstant()
        } else {
            if (Character.isCharacter(char)) {
                parseVariableOrCall()
            } else {
                when (char) {
                    '(' -> parseBinary()
                    '[' -> parseIfExpression()
                    else -> throw SyntaxException()
                }
            }
        }
    }

    private fun parseFunctionDefinition(): FunctionDefinition {
        val identifier = parseIdentifier()

        if (source.curSym() != '(') {
            throw SyntaxException()
        }

        source.nextSym()

        val parameterList = emptyList<Identifier>().toMutableList()

        while (source.curSym() != ')') {
            parameterList += parseIdentifier()
            if (source.curSym() == ',') {
                source.nextSym()
            }
        }

        if (source.curSym() != ')' || source.nextSym() != '=' || source.nextSym() != '{') {
            throw SyntaxException()
        }
        source.nextSym()

        val expression = parseExpression()

        if (source.curSym() != '}' || source.nextSym() != EOL) {
            throw SyntaxException()
        }

        source.nextSym()

        return FunctionDefinition(identifier, parameterList, expression)
    }

    private fun parseProgram(): Program {
        var lines = source.source.count { it == EOL }

        val functionDefinitionList = emptyList<FunctionDefinition>().toMutableList()

        while (lines-- > 0 ) {
            functionDefinitionList += parseFunctionDefinition()
            line++
        }

        val expression = parseExpression()

        if (source.curSym() != Source.END) {
            throw SyntaxException()
        }

        return Program(functionDefinitionList, expression)
    }

    companion object {
        private const val EOL = '\n'

        fun parse(string: String): Program {
            return Parser(string).parseProgram()
        }

        fun evaluate(string: String): Int {
            return parse(string).evaluate()
        }
    }
}