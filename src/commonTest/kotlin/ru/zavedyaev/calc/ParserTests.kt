package ru.zavedyaev.calc

import kotlin.test.Test
import kotlin.test.assertEquals

class ParserTests {
    @Test
    fun testMultiplyWithoutBrackets() {
        assertEquals(5.0 * 6.0, ExpressionParser.parse("5(6)").getValue())
        assertEquals(5.0 * 6.0, ExpressionParser.parse("(6)5").getValue())
        assertEquals(5.0 * 6.0, ExpressionParser.parse("(5+1)5").getValue())
        assertEquals(5.0 * 6.0, ExpressionParser.parse("5(5+1)").getValue())
    }
}