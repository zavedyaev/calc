package ru.zavedyaev.calc

import platform.posix.pow

object PlatformSpecificMethods {
    fun power(x: Double, y: Double) = pow(x, y)
}