package ru.zavedyaev.calc

fun main(args: Array<String>) {
    val parsedArgs = ArgsParser.parse(args)
    if (parsedArgs.showHelp) {
        ArgsParser.printHelp()
        return
    }

    val operand: Operand
    try {
        operand = ExpressionParser.parse(parsedArgs.parsed)
    } catch (_: Throwable) {
        println("Error while expression parsing. Please make sure your expression is valid")
        return
    }

    try {
        val calcResult = operand.getValue()
        println(calcResult)
    } catch (_: Throwable) {
        println("Error while expression calculation. Please make sure your expression is valid")
        return
    }
}
