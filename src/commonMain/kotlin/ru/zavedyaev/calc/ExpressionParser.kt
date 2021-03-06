package ru.zavedyaev.calc

object ExpressionParser {
    fun parse(input: String): Operand {
        val withoutSpaces = input.replace(" ", "").replace("_", "").replace(",", "").toLowerCase()
        if (!bracketsCountIsRight(withoutSpaces)) return NumberOperand(
            Double.NaN
        )

        val operandById = HashMap<Int, Operand>()

        val withConstantsReplaced =
            replaceConstants(withoutSpaces, operandById)

        /**
         * find the deepest brackets parse into operand and replace it in the string. For example:
         * input: (1-4)+(4*4(5-6(7)))
         * step 1: (1-4)+(4*4(5-6~~1~~))
         * step 2: (1-4)+(4*4~~2~~)
         * step 3: ~~3~~+(4*4~~2~~)
         * step 4: ~~3~~+~~4~~
         * step 5: ~~5~~
         * step 6: exit
         */

        var operandResult: Operand? = null
        var remainedStr = withConstantsReplaced
        var deepestBrackets: DeepestBrackets? =
            findDeepestBrackets(remainedStr)

        while (deepestBrackets != null) {
            val withoutBrackets =
                substringWithoutBrackets(
                    remainedStr,
                    deepestBrackets
                )

            val parsed =
                parseStringWithoutBrackets(
                    withoutBrackets,
                    operandById
                )

            if (deepestBrackets.index == 0 && remainedStr.length == deepestBrackets.length) {
                //this is the last operation
                operandResult = parsed
                deepestBrackets = null
            } else {
                //replace brackets by ~~123~~
                val index = nextIndex(operandById)
                operandById[index] = parsed
                val firstPart = if (deepestBrackets.index != 0) remainedStr.substring(0, deepestBrackets.index) else ""
                val lastPart =
                    if (deepestBrackets.index + deepestBrackets.length != remainedStr.length) remainedStr.substring(
                        deepestBrackets.index + deepestBrackets.length,
                        remainedStr.length
                    ) else ""

                remainedStr = firstPart + generateOperandReplacement(index) + lastPart
                deepestBrackets = findDeepestBrackets(remainedStr)
            }
        }
        return operandResult ?: NumberOperand(Double.NaN)
    }

    private fun nextIndex(operandById: HashMap<Int, Operand>): Int {
        val maxCounter = operandById.keys.max() ?: -1
        return maxCounter + 1
    }

    private fun replaceConstants(input: String, operandById: HashMap<Int, Operand>): String {
        var withConstantsReplaced = input
        Constant.values().forEach { constant ->
            if (withConstantsReplaced.contains(constant.label)) {
                val index = nextIndex(operandById)
                operandById[index] = constant.operand
                withConstantsReplaced =
                    withConstantsReplaced.replace(
                        constant.label,
                        generateOperandReplacement(index)
                    )
            }
        }

        return withConstantsReplaced
    }

    private fun parseStringWithoutBrackets(input: String, operandsById: HashMap<Int, Operand>): Operand {
        var remainedString: String = input

        OperatorsGroup.orderedByPriority.forEach { operatorsGroup ->
            /**
             * for operator groups like sin, cos, etc we need to repeat group parsing to parse the following cases:
             * cos sin cos log pi
             */
            do {
                var groupMatched = false
                operatorsGroup.operators.forEach { operator ->
                    var match = operator.findMatch(remainedString)
                    while (match.found) {
                        groupMatched = true

                        val operandsStrings = match.operandsStrings
                        val operands = operandsStrings.map { operandStr: String ->
                            val numOrNull = operandStr.toDoubleOrNull()
                            if (numOrNull != null) {
                                NumberOperand(numOrNull)
                            } else {
                                val operandId = operandStr.replace(REPLACED_OPERAND_MARK, "").toInt()
                                operandsById.getValue(operandId)
                            }
                        }

                        val newOperand = OperationOperand(
                            Operation(
                                operator,
                                operands
                            )
                        )

                        val index = nextIndex(operandsById)
                        val operandReplacement = generateOperandReplacement(index)
                        operandsById[index] = newOperand

                        remainedString = remainedString.replace(match.fullMatch, operandReplacement)
                        match = operator.findMatch(remainedString)

                        if (operator == Operator.NUMBER_FALLBACK) break //leave infinite circle
                    }
                }
            } while (operatorsGroup.repeatParsing && groupMatched)
        }

        //at this moment we should see something like ~~123~~
        val operandId = extractOperandId(remainedString)
        return operandsById.getValue(operandId)
    }

    private fun bracketsCountIsRight(input: String): Boolean {
        val open = input.count { c -> c == '(' }
        val close = input.count { c -> c == ')' }
        return open == close
    }

    private fun findDeepestBrackets(input: String): DeepestBrackets {
        var currentCount = 0
        var maxCount = 0
        var maxIndex = 0
        var length = input.length

        input.forEachIndexed { index, c ->
            if (c == '(') {
                currentCount++
                if (currentCount >= maxCount) {
                    maxCount = currentCount
                    maxIndex = index
                }
            }

            if (c == ')') {
                if (currentCount == maxCount) {
                    length = 1 + index - maxIndex
                }
                currentCount--
            }
        }

        return DeepestBrackets(maxIndex, length)
    }

    private fun substringWithoutBrackets(input: String, deepestBrackets: DeepestBrackets): String {
        return if (deepestBrackets.index == 0 && input.length == deepestBrackets.length && !input.startsWith("(")) {
            input
        } else {
            input.substring(deepestBrackets.index + 1, deepestBrackets.index + deepestBrackets.length - 1)
        }
    }
}

data class DeepestBrackets(
    val index: Int,
    val length: Int
)