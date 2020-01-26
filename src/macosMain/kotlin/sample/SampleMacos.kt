package sample

fun main(args: Array<String>) {
    println("args: ${args.toList()}")

    if (args.size != 1) {
        printHelp()
        return
    }

    val arg = args.first()
    if (arg.contains(HELP_ARG)) {
        printHelp()
        return
    }

    try {
        val calcResult = Parser.parseAndCalc(arg)
        println(calcResult)
    } catch (_: Throwable) {
        println("Error. Please make sure your expression is valid")
    }


}

private const val HELP_ARG = "help"
private fun printHelp() {
    println(
        """
        NAME
            calc -- math expression calculator

        SYNOPSIS
            calc "math expression"

        DESCRIPTION
            Will parse your math expression and calculate the result.
            
            Supported operators:
            ( )
            ! (factorial), ^ (power), v (square root)
            ln (logarithm base e), log (logarithm base 10), sin, cos, tg/tan
            */x, /
            -, +
            
            Constants:
            - PI
            - e

            The following options are available:

            --help  print this manual
        
        EXAMPLES
            calc "5 + 6 * 2"
            calc "5 (6 * 2)"
            calc "((v3) * 2.5) / (5!)"
            calc "5.4 * 3,000.02 - 5*2 + 3! - (4+5) + sin 0 - cos PI"
    """.trimIndent()
    )
}