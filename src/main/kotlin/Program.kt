class Program(functionDefinitionList : List<FunctionDefinition>,
              private val expression: Expression) {
    private val identifierToFunction = functionDefinitionList.associateBy { it.identifier }

    fun evaluate(): Int {
        return expression.apply(identifierToFunction = identifierToFunction)
    }
}