package expression

import Expression
import FunctionDefinition
import exceptions.FunctionNotFoundException

class CallExpression(val identifier: Identifier,
                     private val argumentList: List<Expression>,
                     override val line: Int): Expression {
    override fun apply(varToExpression: Map<Identifier, Expression>,
                       identifierToFunction: Map<Identifier, FunctionDefinition>): Int {
        return identifierToFunction[identifier]?.run(argumentList
                .map { ConstantExpression(it.apply(varToExpression, identifierToFunction), line) },
            identifierToFunction, line)
            ?: throw FunctionNotFoundException(identifier.toString(), line)
    }

    override fun toString(): String {
        return "${identifier}(${argumentList.joinToString(",")})"
    }
}