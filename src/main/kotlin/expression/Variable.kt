package expression

import Expression
import FunctionDefinition
import exceptions.ParameterNotFountException

class Variable(val identifier: Identifier, override val line: Int): Expression {
    override fun apply(varToExpression: Map<Identifier, Expression>,
                       identifierToFunction: Map<Identifier, FunctionDefinition>): Int {
        return varToExpression[identifier]?.apply(varToExpression, identifierToFunction)
            ?: throw ParameterNotFountException(identifier.toString(), line)
    }

    override fun toString(): String {
        return identifier.toString()
    }
}