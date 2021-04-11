package expression

import FunctionDefinition

interface Expression {
    val line: Int

    fun apply(varToExpression: Map<Identifier, Expression> = emptyMap(),
              identifierToFunction: Map<Identifier, FunctionDefinition>) : Int
}