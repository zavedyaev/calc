package ru.zavedyaev.calc

import kotlin.test.Test
import kotlin.test.assertEquals

class ParserTests {
    @Test
    fun testMultiplyWithoutBrackets() {
        assertEquals(5.0 * 6.0, Parser.parseAndCalc("5(6)"))
        assertEquals(5.0 * 6.0, Parser.parseAndCalc("(6)5"))
        assertEquals(5.0 * 6.0, Parser.parseAndCalc("(5+1)5"))
        assertEquals(5.0 * 6.0, Parser.parseAndCalc("5(5+1)"))
    }
}