package expression

import Expression
import FunctionDefinition
import Operation
import exceptions.ParserRuntimeException

class BinaryExpression(
    private val firstExpression: Expression,
    private val secondExpression: Expression,
    private val operation: Operation, override val line: Int
) : Expression {
    override fun apply(varToExpression: Map<Identifier, Expression>,
                       identifierToFunction: Map<Identifier, FunctionDefinition>): Int {
        try {
            return operation.apply(firstExpression.apply(varToExpression, identifierToFunction),
                secondExpression.apply(varToExpression, identifierToFunction))
        } catch (e: RuntimeException) {
            throw ParserRuntimeException(toString(), line)
        }
    }

    override fun toString(): String {
        return "(${firstExpression}${operation.char}${secondExpression})"
    }
}