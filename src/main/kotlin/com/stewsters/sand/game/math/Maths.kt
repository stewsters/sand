package com.stewsters.sand.game.math

// Random Math functions

// Linear Interpolate
fun lerp(percentage: Double, one: Double, two: Double): Double =
        one + (two - one) * percentage

fun Double.limit(low: Double, high: Double): Double =
        if (this < low)
            low
        else if (this > high)
            high
        else this
