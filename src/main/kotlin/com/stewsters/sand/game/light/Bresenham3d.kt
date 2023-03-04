package com.stewsters.sand.game.light

import kaiju.math.Vec3
import kotlin.math.abs

object Bresenham3d {

    fun open(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int, evaluator3d: Evaluator3d): Boolean {

        var i: Int
        val dx: Int
        val dy: Int
        val dz: Int
        val l: Int
        val m: Int
        val n: Int
        val x_inc: Int
        val y_inc: Int
        val z_inc: Int
        var err_1: Int
        var err_2: Int
        val dx2: Int
        val dy2: Int
        val dz2: Int
        val last_point = IntArray(3)
        last_point[0] = x1
        last_point[1] = y1
        last_point[2] = z1

        val point = IntArray(3)
        point[0] = x1
        point[1] = y1
        point[2] = z1
        dx = x2 - x1
        dy = y2 - y1
        dz = z2 - z1
        x_inc = if (dx < 0) -1 else 1
        l = abs(dx)
        y_inc = if (dy < 0) -1 else 1
        m = abs(dy)
        z_inc = if (dz < 0) -1 else 1
        n = abs(dz)
        dx2 = l shl 1
        dy2 = m shl 1
        dz2 = n shl 1

        if (l >= m && l >= n) {
            err_1 = dy2 - l
            err_2 = dz2 - l
            i = 0
            while (i < l) {
                if (point[0] == x2 && point[1] == y2 && point[2] == z2)
                    return false
                if (!evaluator3d.isGood(last_point[0], last_point[1], last_point[2], point[0], point[1], point[2]))
                    return false

                if (err_1 > 0) {
                    last_point[1] = point[1]
                    point[1] += y_inc
                    err_1 -= dx2
                }
                if (err_2 > 0) {
                    last_point[2] = point[2]
                    point[2] += z_inc
                    err_2 -= dx2
                }
                err_1 += dy2
                err_2 += dz2
                last_point[0] = point[0]
                point[0] += x_inc
                i++
            }
        } else if (m >= l && m >= n) {
            err_1 = dx2 - m
            err_2 = dz2 - m
            i = 0
            while (i < m) {
                if (point[0] == x2 && point[1] == y2 && point[2] == z2)
                    return false
                if (!evaluator3d.isGood(last_point[0], last_point[1], last_point[2], point[0], point[1], point[2]))
                    return false


                if (err_1 > 0) {
                    last_point[0] = point[0]
                    point[0] += x_inc
                    err_1 -= dy2
                }
                if (err_2 > 0) {
                    last_point[2] = point[2]
                    point[2] += z_inc
                    err_2 -= dy2
                }
                err_1 += dx2
                err_2 += dz2
                last_point[1] = point[1]
                point[1] += y_inc
                i++
            }
        } else {
            err_1 = dy2 - n
            err_2 = dx2 - n
            i = 0
            while (i < n) {
                if (point[0] == x2 && point[1] == y2 && point[2] == z2)
                    return false
                if (!evaluator3d.isGood(last_point[0], last_point[1], last_point[2], point[0], point[1], point[2]))
                    return false


                if (err_1 > 0) {
                    last_point[1] = point[1]
                    point[1] += y_inc
                    err_1 -= dz2
                }
                if (err_2 > 0) {
                    last_point[0] = point[0]
                    point[0] += x_inc
                    err_2 -= dz2
                }
                err_1 += dy2
                err_2 += dx2
                point[2] += z_inc
                i++
            }
        }

        return (point[0] == x2 && point[1] == y2 && point[2] == z2) || evaluator3d.isGood(
            last_point[0],
            last_point[1],
            last_point[2],
            point[0],
            point[1],
            point[2]
        )
    }

    fun getArray(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): ArrayList<Vec3> {
        val line = ArrayList<Vec3>()

        var i: Int
        val dx: Int
        val dy: Int
        val dz: Int
        val l: Int
        val m: Int
        val n: Int
        val x_inc: Int
        val y_inc: Int
        val z_inc: Int
        var err_1: Int
        var err_2: Int
        val dx2: Int
        val dy2: Int
        val dz2: Int
        val point = IntArray(3)

        point[0] = x1
        point[1] = y1
        point[2] = z1
        dx = x2 - x1
        dy = y2 - y1
        dz = z2 - z1
        x_inc = if (dx < 0) -1 else 1
        l = abs(dx)
        y_inc = if (dy < 0) -1 else 1
        m = abs(dy)
        z_inc = if (dz < 0) -1 else 1
        n = abs(dz)
        dx2 = l shl 1
        dy2 = m shl 1
        dz2 = n shl 1

        if (l >= m && l >= n) {
            err_1 = dy2 - l
            err_2 = dz2 - l
            i = 0
            while (i < l) {
                line.add(Vec3(point[0], point[1], point[2]))

                if (err_1 > 0) {
                    point[1] += y_inc
                    err_1 -= dx2
                }
                if (err_2 > 0) {
                    point[2] += z_inc
                    err_2 -= dx2
                }
                err_1 += dy2
                err_2 += dz2
                point[0] += x_inc
                i++
            }
        } else if (m >= l && m >= n) {
            err_1 = dx2 - m
            err_2 = dz2 - m
            i = 0
            while (i < m) {
                line.add(Vec3(point[0], point[1], point[2]))

                if (err_1 > 0) {
                    point[0] += x_inc
                    err_1 -= dy2
                }
                if (err_2 > 0) {
                    point[2] += z_inc
                    err_2 -= dy2
                }
                err_1 += dx2
                err_2 += dz2
                point[1] += y_inc
                i++
            }
        } else {
            err_1 = dy2 - n
            err_2 = dx2 - n
            i = 0
            while (i < n) {
                line.add(Vec3(point[0], point[1], point[2]))

                if (err_1 > 0) {
                    point[1] += y_inc
                    err_1 -= dz2
                }
                if (err_2 > 0) {
                    point[0] += x_inc
                    err_2 -= dz2
                }
                err_1 += dy2
                err_2 += dx2
                point[2] += z_inc
                i++
            }
        }
        line.add(Vec3(point[0], point[1], point[2]))

        return line
    }

}