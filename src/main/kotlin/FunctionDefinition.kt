import exceptions.ArgumentNumberException
import expression.Expression
import expression.Identifier

class FunctionDefinition(
    val identifier: Identifier,
    private val parameterList: List<Identifier>,
    val expression: Expression
) {
    fun run(
        valueList: List<Expression>,
        identifierToFunction: Map<Identifier, FunctionDefinition>, line: Int
    ): Int {
        if (parameterList.size != valueList.size) {
            throw ArgumentNumberException(identifier.toString(), line)
        }
        val varToExpression = parameterList.zip(valueList).toMap()
        return expression.apply(varToExpression, identifierToFunction)
    }
}