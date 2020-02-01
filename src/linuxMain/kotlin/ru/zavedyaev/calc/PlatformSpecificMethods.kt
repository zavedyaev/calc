package ru.zavedyaev.calc

import platform.posix.pow

actual object PlatformSpecificMethods {
    actual fun power(x: Double, y: Double): Double = pow(x, y)
}