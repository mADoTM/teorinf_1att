import kotlin.math.log2

fun main() {
    val ansambl = arrayOf(
        arrayOf(0.013, 0.04, 0.065, 0.075),
        arrayOf(0.03, 0.076, 0.07, 0.112),
        arrayOf(0.053, 0.045, 0.025, 0.396)
    )

    for (i in ansambl.indices) {
        for (j in ansambl[i].indices) {
            print("p(x${i + 1},y${j + 1})=${ansambl[i][j]} ")
        }
    }
    println()
    val ansamblX = getX(ansambl)
    val ansamblY = getY(ansambl)
    displayVariable(ansamblX, "X")
    displayVariable(ansamblY, "Y")

    checkIndependent(ansamblX, ansamblY, ansambl)
    println()
    val conditionalX = conditionalProbabilityX(ansamblY, ansambl)
    println()
    val conditionalY = conditionalProbabilityY(ansamblX, ansambl)

    println()

    val entropyHNotIndexedX = entropyNotIndexed(ansamblX)
    println("H(X)=$entropyHNotIndexedX")
    val entropyHNotIndexedY = entropyNotIndexed(ansamblY)
    println("H(Y)=$entropyHNotIndexedY")

    println()
    val yOnXIndexed = entropyYOnXIndexed(conditionalX)
    println()
    val xOnYIndexed = entropyXOnYIndexed(conditionalY)

    println()
    val entropyYOnX = entropyFullVariable(ansamblY, yOnXIndexed)
    println("Hy(X)=${entropyYOnX}")

    val entropyXOnY = entropyFullVariable(ansamblX, xOnYIndexed)
    println("Hx(Y)=${entropyXOnY}")

    println()
    entropyFullAllVariables(entropyYOnX, entropyXOnY, entropyHNotIndexedX, entropyHNotIndexedY)
}

fun entropyFullAllVariables(
    entropyYOnX: Double,
    entropyXOnY: Double,
    entropyNotIndexedX: Double,
    entropyNotIndexedY: Double
) {
    println("H(XY) = H(X) + Hx(Y)\t= $entropyNotIndexedX + $entropyXOnY = ${entropyNotIndexedX + entropyXOnY} = \n" +
            "Hy(X) + H(Y)\t= $entropyYOnX + $entropyNotIndexedY = ${entropyNotIndexedY + entropyYOnX} = \n" +
            "H(X) + H(Y)\t= $entropyNotIndexedX + $entropyNotIndexedY = ${entropyNotIndexedX + entropyNotIndexedY}")
}

fun entropyFullVariable(ansambl: Array<Double>, yOnXIndexed: Array<Double>): Double {
    var result = 0.0

    for (j in ansambl.indices) {
        result += (ansambl[j] * 1000) * (yOnXIndexed[j] * 1000) / (1000 * 1000)
    }

    return result
}

fun entropyXOnYIndexed(probabilityY: Array<Array<Double>>): Array<Double> {
    val result = Array(probabilityY.size) { 0.0 }

    for (i in probabilityY.indices) {
        for (j in probabilityY[0].indices) {
            result[i] += (probabilityY[i][j] * 1000) * log2(probabilityY[i][j]) / 1000
        }
        result[i] *= -1.0
        println("Hx${i + 1}(Y)=${result[i]}")
    }

    return result
}

fun entropyYOnXIndexed(probabilityX: Array<Array<Double>>): Array<Double> {
    val result = Array(probabilityX[0].size) { 0.0 }

    for (j in probabilityX[0].indices) {
        for (i in probabilityX.indices) {
            result[j] += (probabilityX[i][j] * 1000) * log2(probabilityX[i][j]) / 1000
        }
        result[j] *= -1.0
        println("Hy${j + 1}(X)=${result[j]}")
    }

    return result
}

fun entropyNotIndexed(ansamblVariable: Array<Double>): Double {
    var result = 0.0

    ansamblVariable.forEach {
        result += (it * 1000) * log2(it) / 1000
    }
    result *= -1.0
    return result
}

fun conditionalProbabilityX(y: Array<Double>, ansambl: Array<Array<Double>>): Array<Array<Double>> {
    val result = Array(ansambl.size) { Array(ansambl[0].size) { 0.0 } }

    for (i in ansambl.indices) {
        for (j in ansambl[i].indices) {
            result[i][j] = (ansambl[i][j] * 1000) / (y[j] * 1000)
            println("p(x${i + 1}|y${j + 1}) = ${result[i][j]}")
        }
    }

    return result
}

fun conditionalProbabilityY(x: Array<Double>, ansambl: Array<Array<Double>>): Array<Array<Double>> {
    val result = Array(ansambl.size) { Array(ansambl[0].size) { 0.0 } }

    for (j in ansambl[0].indices) {
        for (i in ansambl.indices) {
            result[i][j] = (ansambl[i][j] * 1000) / (x[i] * 1000)
            println("p(y${j + 1}|x${i + 1}) = ${result[i][j]}")
        }
    }

    return result
}

fun checkIndependent(x: Array<Double>, y: Array<Double>, ansambl: Array<Array<Double>>) {
    var isIndependent = true

    for (i in ansambl.indices) {
        for (j in ansambl[i].indices) {
            val xOnY = (x[i] * 1000) * (y[j] * 1000)
            println("p(x${i + 1})p(y${j + 1})=${xOnY / 1000000} p(x${i + 1},y${j + 1})=${ansambl[i][j]}")
            if (xOnY / 10000 != ansambl[i][j]) {
                isIndependent = false
            }
        }
    }

    if (isIndependent) println("Ансамбли X и Y независимы") else println("Ансамбли X и Y зависимы")
}

fun displayVariable(array: Array<Double>, variable: String) {
    print("$variable = [")
    for (i in array.indices) {
        print(" ${array[i]} ")
    }
    println("]")
}

fun getX(ansambl: Array<Array<Double>>): Array<Double> {
    val result = Array(ansambl.size) { 0.0 }

    for (i in ansambl.indices) {
        var sum = 0.0

        for (j in ansambl[i].indices) {
            sum += ansambl[i][j] * 10000
        }
        result[i] = sum / 10000
    }

    return result
}

fun getY(ansambl: Array<Array<Double>>): Array<Double> {
    val result = Array(ansambl[0].size) { 0.0 }

    for (i in ansambl[0].indices) {
        var sum = 0.0

        for (j in ansambl.indices) {
            sum += ansambl[j][i] * 10000
        }
        result[i] = sum / 10000
    }

    return result
}