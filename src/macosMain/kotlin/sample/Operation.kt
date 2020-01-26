package sample

import kotlin.math.*

data class Operation(
    val operator: Operator,
    val operands: List<Operand>
) {
    fun calculateResult(): Double = operator.calculate(operands)
}

interface Operand {
    fun getValue(): Double
}

data class NumberOperand(
    val number: Double
) : Operand {
    override fun getValue() = number
}

data class OperationOperand(
    val operation: Operation
) : Operand {
    override fun getValue() = operation.calculateResult()
}

enum class Constant(val label: String, val operand: Operand) {
    PI("pi", NumberOperand(kotlin.math.PI)),
    E("e", NumberOperand(kotlin.math.E))
}

//todo replace by a real factorial for float numbers
private fun factorial(num: Double): Double {
    return try {
        val result = integerFactorial(num.roundToLong())
        result.toDouble()
    } catch (th: Throwable) {
        Double.NaN
    }
}

private fun integerFactorial(num: Long): Long {
    val minusOne = num - 1
    return if (minusOne <= 0) 1
    else num * integerFactorial(minusOne)
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


private val doubleNumberRegex = Regex("(\\d+\\.\\d+)|(\\.\\d+)|(\\d+)") // 1.1 or .1 or 1
const val REPLACED_OPERAND_MARK = "~~"
fun generateOperandReplacement(id: Int) = "$REPLACED_OPERAND_MARK$id$REPLACED_OPERAND_MARK"
fun extractOperandId(operandReplacement: String) = operandReplacement.replace(REPLACED_OPERAND_MARK, "").toInt()
private val operandRegex = Regex("$REPLACED_OPERAND_MARK\\d+$REPLACED_OPERAND_MARK") // ~~123~~

enum class Operator(
    val priorityGroup: Int,
    val calculate: (List<Operand>) -> Double,
    private val regex: Regex,
    /**
     * in case you have 2 operands, you will see something like [[2,3][5,6]]
     * which means first operand could be found in group 2 or 3, second operand could be found group 5 or 6
     */
    private val operandsGroupIds: List<List<Int>>,
    private val fullMatchGroupId: Int = 1
) {
    FACTORIAL(
        5, { operands ->
            if (operands.size != 1) Double.NaN
            else factorial(operands.first().getValue())
        }, Regex("(($doubleNumberRegex)!|($operandRegex)!)"),
        listOf(listOf(3, 4, 5, 6))
    ),
    POWER(
        5, { operands ->
            if (operands.size != 2) Double.NaN
            else PlatformSpecificMethods.power(operands.first().getValue(), operands.last().getValue())
        }, Regex("((($doubleNumberRegex)|($operandRegex))\\^(($doubleNumberRegex)|($operandRegex)))"),
        listOf(listOf(4, 5, 6, 7), listOf(10, 11, 12, 13))
    ),
    //todo sin cos tn tan combinations
    SQUARE_ROOT(5, { operands ->
        if (operands.size != 1) Double.NaN
        else sqrt(operands.first().getValue())
    }, Regex("(v(($doubleNumberRegex)|($operandRegex)))"), listOf(listOf(4, 5, 6, 7))),
    LOGARITHM_BASE_10(4, { operands ->
        if (operands.size != 1) Double.NaN
        else log10(operands.first().getValue())
    }, Regex("(log(($doubleNumberRegex)|($operandRegex)))"), listOf(listOf(4, 5, 6, 7))),
    LOGARITHM_BASE_E(4, { operands ->
        if (operands.size != 1) Double.NaN
        else ln(operands.first().getValue())
    }, Regex("(ln(($doubleNumberRegex)|($operandRegex)))"), listOf(listOf(4, 5, 6, 7))),
    SINUS(4, { operands ->
        if (operands.size != 1) Double.NaN
        else sin(operands.first().getValue())
    }, Regex("(sin(($doubleNumberRegex)|($operandRegex)))"), listOf(listOf(4, 5, 6, 7))),
    COSINUS(4, { operands ->
        if (operands.size != 1) Double.NaN
        else kotlin.math.cos(operands.first().getValue())
    }, Regex("(cos(($doubleNumberRegex)|($operandRegex)))"), listOf(listOf(4, 5, 6, 7))),
    TANGENT(4, { operands ->
        if (operands.size != 1) Double.NaN
        else kotlin.math.tan(operands.first().getValue())
    }, Regex("((tg|tan)(($doubleNumberRegex)|($operandRegex)))"), listOf(listOf(5, 6, 7, 8))),
    MULTIPLY(
        3, { operands ->
            if (operands.size != 2) Double.NaN
            else operands.first().getValue() * operands.last().getValue()
        }, Regex("((($doubleNumberRegex)|($operandRegex))[*x](($doubleNumberRegex)|($operandRegex)))"),
        listOf(listOf(4, 5, 6, 7), listOf(10, 11, 12, 13))
    ),
    MULTIPLY_WITHOUT_SIGN(
        3,

        { operands ->
            if (operands.size != 2) Double.NaN
            else operands.first().getValue() * operands.last().getValue()
        },
        Regex("((($doubleNumberRegex)($operandRegex))|(($operandRegex)($doubleNumberRegex))|(($operandRegex)($operandRegex)))"),
        listOf(listOf(4, 5, 6, 9, 15), listOf(7, 11, 12, 13, 16))
    ),
    DIVIDE(
        3, { operands ->
            if (operands.size != 2) Double.NaN
            else operands.first().getValue() / operands.last().getValue()
        }, Regex("((($doubleNumberRegex)|($operandRegex))/(($doubleNumberRegex)|($operandRegex)))"),
        listOf(listOf(4, 5, 6, 7), listOf(10, 11, 12, 13))
    ),
    INVERSE(
        2, { operands ->
            if (operands.size != 1) Double.NaN
            else (0.0 - operands.first().getValue())
        }, Regex("(^-(($doubleNumberRegex)|($operandRegex)))"),
        listOf(listOf(4, 5, 6, 7))
    ),
    MINUS(
        2, { operands ->
            if (operands.size != 2) Double.NaN
            else operands.first().getValue() - operands.last().getValue()
        }, Regex("((($doubleNumberRegex)|($operandRegex))-(($doubleNumberRegex)|($operandRegex)))"),
        listOf(listOf(4, 5, 6, 7), listOf(10, 11, 12, 13))
    ),
    PLUS(
        2, { operands ->
            if (operands.size != 2) Double.NaN
            else operands.first().getValue() + operands.last().getValue()
        }, Regex("((($doubleNumberRegex)|($operandRegex))\\+(($doubleNumberRegex)|($operandRegex)))"),
        listOf(listOf(4, 5, 6, 7), listOf(10, 11, 12, 13))
    ),
    NUMBER_FALLBACK(1, { operands ->
        if (operands.size != 1) Double.NaN
        else operands.first().getValue()
    }, Regex("(($doubleNumberRegex)|($operandRegex))"), listOf(listOf(3, 4, 5, 6)));


    fun findMatch(string: String): OperatorMatch {
        val matchResult = regex.find(string) ?: return OperatorMatch.notFound()

        val groups = matchResult.groups

        val operandsStrings = operandsGroupIds.map { operandGroupIds ->
            val operandGroup = operandGroupIds.asSequence().map { id -> groups[id] }.filterNotNull().first()
            operandGroup.value
        }
        val matchGroup = groups[fullMatchGroupId]!!

        return OperatorMatch(matchGroup.value, operandsStrings)
    }

    companion object {
        val orderedByPriority = values().sortedByDescending { it.priorityGroup }
    }
}
