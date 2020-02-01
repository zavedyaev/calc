package ru.zavedyaev.calc

object ArgsParser {
    data class ParsedArgs(
        val parsed: String,
        val showHelp: Boolean = false
    )

    fun parse(args: Array<String>): ParsedArgs {
        if (args.size != 1) {
            return ParsedArgs("", true)
        }

        //todo add ability to run it without quotes
        val arg = args.first()
        if (arg.contains(HELP_ARG)) {
            return ParsedArgs("", true)
        }

        return ParsedArgs(arg, false)
    }

    private const val HELP_ARG = "help"
    fun printHelp() {
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
}