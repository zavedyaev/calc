package sample

import platform.posix.pow
import kotlin.math.sqrt

data class Operation(
    val operator: Operator,
    val operands: List<Operand>
) {
    fun calculateResult(): Double = operator.calculate(operands)
}

private const val eps = 0.00001
private fun factorial(num: Double): Double  {
    val minusOne = num - 1.0
    if (minusOne < eps) return 1.0
    return num * factorial(minusOne)

        //todo replace by a real code
}

data class OperatorMatch(
    val fullMatch: String,
    val operandsStrings: List<String>,
    val found: Boolean = true
) {
    companion object {
        fun notFound() = OperatorMatch("", emptyList(), false)
    }
}


//todo float numbers
enum class Operator(
    val priorityGroup: Int,
    val operandsCount: Int,
    val labels: Set<String>,
    val calculate: (List<Operand>) -> Double,
    val findMatch: (String) -> OperatorMatch
) {
    FACTORIAL(5, 1, setOf("!"), { operands ->
        if (operands.size != 1) Double.NaN
        else factorial(operands.first().getValue())
    }, { string ->
        val pattern = "((\\d+)!|(~~\\d+~~)!)".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup = (groups[2] ?: groups[3])!!
            val operandStr = operandGroup.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operandStr))
        }
    }),
    POWER(5, 2, setOf("^"), { operands ->
        if (operands.size != 2) Double.NaN
        else pow(operands.first().getValue(), operands.last().getValue())
    }, { string ->
        val pattern = "(((\\d+)|(~~\\d+~~))\\^((\\d+)|(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup1 = (groups[2] ?: groups[3] ?: groups[4])!!
            val operand1Str = operandGroup1.value

            val operandGroup2 = (groups[5] ?: groups[6] ?: groups[7])!!
            val operand2Str = operandGroup2.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operand1Str, operand2Str))
        }
    }),
    //todo sin cos tn tan combinations
    SQUARE_ROOT(5, 1, setOf("v"),{ operands ->
        if (operands.size != 1) Double.NaN
        else sqrt(operands.first().getValue())
    }, { string ->
        val pattern = "(v((\\d+)|(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup = (groups[2] ?: groups[3] ?: groups[4])!!
            val operandStr = operandGroup.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operandStr))
        }
    }),
    LOGARITHM_BASE_10(4, 1, setOf("log"),{ operands ->
        if (operands.size != 1) Double.NaN
        else kotlin.math.log10(operands.first().getValue())
    }, { string ->
        val pattern = "(log((\\d+)|(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup = (groups[2] ?: groups[3] ?: groups[4])!!
            val operandStr = operandGroup.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operandStr))
        }
    }),
    LOGARITHM_BASE_E(4, 1, setOf("ln"),{ operands ->
        if (operands.size != 1) Double.NaN
        else kotlin.math.ln(operands.first().getValue())
    }, { string ->
        val pattern = "(ln((\\d+)|(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup = (groups[2] ?: groups[3] ?: groups[4])!!
            val operandStr = operandGroup.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operandStr))
        }
    }),
    SINUS(4, 1, setOf("sin"),{ operands ->
        if (operands.size != 1) Double.NaN
        else kotlin.math.sin(operands.first().getValue())
    }, { string ->
        val pattern = "(sin((\\d+)|(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup = (groups[2] ?: groups[3] ?: groups[4])!!
            val operandStr = operandGroup.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operandStr))
        }
    }),
    COSINUS(4, 1, setOf("cos"),{ operands ->
        if (operands.size != 1) Double.NaN
        else kotlin.math.cos(operands.first().getValue())
    }, { string ->
        val pattern = "(cos((\\d+)|(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup = (groups[2] ?: groups[3] ?: groups[4])!!
            val operandStr = operandGroup.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operandStr))
        }
    }),
    TANGENT(4, 1, setOf("tg", "tan"),{ operands ->
        if (operands.size != 1) Double.NaN
        else kotlin.math.tan(operands.first().getValue())
    }, { string ->
        val pattern = "((tg|tan)((\\d+)|(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup = (groups[3] ?: groups[4] ?: groups[5])!!
            val operandStr = operandGroup.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operandStr))
        }
    }),
    MULTIPLY(3, 2, setOf("*" , "x"),{ operands ->
        if (operands.size != 2) Double.NaN
        else operands.first().getValue() * operands.last().getValue()
    }, { string ->
        val pattern = "(((\\d+)|(~~\\d+~~))[*x]((\\d+)|(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup1 = (groups[2] ?: groups[3] ?: groups[4])!!
            val operand1Str = operandGroup1.value

            val operandGroup2 = (groups[5] ?: groups[6] ?: groups[7])!!
            val operand2Str = operandGroup2.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operand1Str, operand2Str))
        }
    }),
    MULTIPLY_WITHOUT_SIGN(3, 2, setOf(""),{ operands ->
        if (operands.size != 2) Double.NaN
        else operands.first().getValue() * operands.last().getValue()
    }, { string ->
        val pattern = "(((\\d+)(~~\\d+~~))|((~~\\d+~~)(\\d+))|((~~\\d+~~)(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup1 = (groups[3] ?: groups[6] ?: groups[9])!!
            val operand1Str = operandGroup1.value

            val operandGroup2 = (groups[4] ?: groups[7] ?: groups[10])!!
            val operand2Str = operandGroup2.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operand1Str, operand2Str))
        }
    }),
    DIVIDE(3, 2, setOf("/"), { operands ->
        if (operands.size != 2) Double.NaN
        else operands.first().getValue() / operands.last().getValue()
    }, { string ->
        val pattern = "(((\\d+)|(~~\\d+~~))/((\\d+)|(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup1 = (groups[2] ?: groups[3] ?: groups[4])!!
            val operand1Str = operandGroup1.value

            val operandGroup2 = (groups[5] ?: groups[6] ?: groups[7])!!
            val operand2Str = operandGroup2.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operand1Str, operand2Str))
        }
    }),
    MINUS(2, 2, setOf("-"),{ operands ->
        if (operands.size != 2) Double.NaN
        else operands.first().getValue() - operands.last().getValue()
    }, { string ->
        val pattern = "(((\\d+)|(~~\\d+~~))-((\\d+)|(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup1 = (groups[2] ?: groups[3] ?: groups[4])!!
            val operand1Str = operandGroup1.value

            val operandGroup2 = (groups[5] ?: groups[6] ?: groups[7])!!
            val operand2Str = operandGroup2.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operand1Str, operand2Str))
        }
    }),
    PLUS(2, 2, setOf("+"), { operands ->
        if (operands.size != 2) Double.NaN
        else operands.first().getValue() + operands.last().getValue()
    }, { string ->
        val pattern = "(((\\d+)|(~~\\d+~~))\\+((\\d+)|(~~\\d+~~)))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val operandGroup1 = (groups[2] ?: groups[3] ?: groups[4])!!
            val operand1Str = operandGroup1.value

            val operandGroup2 = (groups[5] ?: groups[6] ?: groups[7])!!
            val operand2Str = operandGroup2.value

            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(operand1Str, operand2Str))
        }
    }),
    NUMBER_FALLBACK(1, 1, setOf("-"),{ operands ->
        if (operands.size != 1) Double.NaN
        else operands.first().getValue()
    }, { string ->
        val pattern = "((\\d+)|(~~\\d+~~))".toRegex()
        val matchResult = pattern.find(string)
        if(matchResult == null) OperatorMatch.notFound()
        else {
            val groups = matchResult.groups
            val matchGroup = groups[1]!!
            val matchStr = matchGroup.value

            OperatorMatch(matchStr, listOf(matchStr))
        }
    });

    companion object {
        val orderedByPriority = values().sortedByDescending { it.priorityGroup }
    }
}

interface Operand{
    fun getValue(): Double
}

data class NumberOperand(
    val number: Double
) : Operand {
    override fun getValue() = number
}

data class OperationOperand(
    val operation: Operation
): Operand{
    override fun getValue() = operation.calculateResult()
}
