package decisionTree

import DataPoint

abstract class Node() {
    val depth = 0
    abstract fun classify(dataPoint: DataPoint): String
}