package expression

import Expression
import FunctionDefinition

class IfExpression(
    private val ifExpression: Expression,
    private val trueExpression: Expression,
    private val falseExpression: Expression, override val line: Int
) : Expression {
    override fun apply(varToExpression: Map<Identifier, Expression>,
                       identifierToFunction: Map<Identifier, FunctionDefinition>): Int {
        return if (ifExpression.apply(varToExpression, identifierToFunction) == 1)
                trueExpression.apply(varToExpression, identifierToFunction)
                else falseExpression.apply(varToExpression, identifierToFunction)
    }

    override fun toString(): String {
        return "[${ifExpression}]?{${trueExpression}}:{${falseExpression}}"
    }
}