package expression

import Expression
import FunctionDefinition
import kotlin.math.absoluteValue

class ConstantExpression(
    private val number: Number,
    private val isNegate: Boolean, override val line: Int) : Expression {
    override fun apply(varToExpression: Map<Identifier, Expression>,
                       identifierToFunction: Map<Identifier, FunctionDefinition>): Int {
        return if (isNegate) -number.toInt() else number.toInt()
    }

    constructor(value: Int, line: Int): this(Number(value.absoluteValue), value < 0, line)

    override fun toString(): String {
        return apply(emptyMap(), emptyMap()).toString()
    }
}