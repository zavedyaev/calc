package sample

object Parser {
    fun parseAndCalc(input: String): Double {
        val withoutSpaces = input.replace(" ", "").replace("_", "").replace(",", "").toLowerCase()
        if(!bracesCountIsRight(withoutSpaces)) return Double.NaN

        println("withoutSpaces: $withoutSpaces")
        //todo replace constants

        var remainedStr = withoutSpaces
        var deepestBraces: SubString? = findDeepestBraces(remainedStr)

        println("remainedStr: $remainedStr , deepestBraces: $deepestBraces")

        val operandById = HashMap<Int, Operand>()
        var operandResult: Operand? = null

        while (deepestBraces != null) {
            val withoutBraces = substringWithoutBraces(remainedStr, deepestBraces)

            val parsed = parse(withoutBraces, operandById)

            if (deepestBraces.index == 0 && remainedStr.length == deepestBraces.length) {
                println("exit braces while")
                //this is the last operation
                operandResult = parsed
                deepestBraces = null
            } else {
                //replace braces by ~~123~~
                val maxCounter = operandById.keys.max() ?: -1
                val newCounter = maxCounter + 1

                operandById[newCounter] = parsed
                val firstPart = if (deepestBraces.index != 0) remainedStr.substring(0, deepestBraces.index) else ""
                val lastPart = if (deepestBraces.index + deepestBraces.length != remainedStr.length) remainedStr.substring(deepestBraces.index + deepestBraces.length, remainedStr.length) else ""

                remainedStr = firstPart + "~~$newCounter~~" + lastPart
                deepestBraces = findDeepestBraces(remainedStr)

                println("remainedStr: $remainedStr , deepestBraces: $deepestBraces")
            }
        }

        println("operandResult: $operandResult")
        return operandResult?.getValue() ?: Double.NaN
    }

    fun parse(strWithoutBraces: String, operandsById: HashMap<Int, Operand>): Operand {
        println("parse: strWithoutBraces: $strWithoutBraces , operandsById: $operandsById")

        var remainedString: String = strWithoutBraces
        println("remainedString: $remainedString")

        Operator.orderedByPriority.forEach { operator: Operator ->
            println("check operator: $operator")
            var match = operator.findMatch(remainedString)
            while(match.found) {
                val operandsStrings = match.operandsStrings
                val operands = operandsStrings.map { operandStr: String ->
                    val numOrNull = operandStr.toDoubleOrNull()
                    if(numOrNull != null) {
                        NumberOperand(numOrNull)
                    } else {
                        println("operandStr: $operandStr")
                        val operandId =  operandStr.replace("~~", "").toInt()
                        operandsById.getValue(operandId)
                    }
                }

                val newOperand = OperationOperand(
                    Operation(
                        operator,
                        operands
                    )
                )

                val maxCounter = operandsById.keys.max() ?: -1
                val newCounter = maxCounter + 1
                val operandReplacement = "~~$newCounter~~"
                operandsById[newCounter] = newOperand

                remainedString = remainedString.replace(match.fullMatch, operandReplacement)
                println("remainedString: $remainedString")
                match = operator.findMatch(remainedString)

                if (operator == Operator.NUMBER_FALLBACK) break //leave infinite circle
            }
        }
        println("remainedString: $remainedString")

        //at this moment we should see something like ~~123~~
        val operandId = remainedString.replace("~~", "").toInt()
        return operandsById.getValue(operandId)
    }

    private fun bracesCountIsRight(input: String): Boolean {
        val open = input.count{ c-> c == '('}
        val close = input.count{ c-> c == ')'}
        return open == close
    }

    private fun findDeepestBraces(input: String): SubString {
        var currentCount = 0
        var maxCount = 0
        var maxIndex = 0
        var length = input.length

        input.forEachIndexed { index, c ->
            if (c == '(') {
                currentCount ++
                if (currentCount >= maxCount) {
                    maxCount = currentCount
                    maxIndex = index
                }
            }

            if (c == ')') {
                if (currentCount == maxCount) {
                    length = 1 + index - maxIndex
                }
                currentCount --
            }
        }

        return SubString(maxIndex, length)
    }

    private fun substringWithoutBraces(input: String, deepestBraces: SubString): String {
        return if (deepestBraces.index == 0 && input.length == deepestBraces.length && !input.startsWith("(")) {
            input
        } else {
            input.substring(deepestBraces.index + 1, deepestBraces.index + deepestBraces.length - 1)
        }
    }
}

data class SubString(
    val index: Int,
    val length: Int
)