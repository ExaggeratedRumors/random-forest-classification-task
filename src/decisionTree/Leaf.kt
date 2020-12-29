package decisionTree

import DataPoint

class Leaf (private val label: String): Node() {

    override fun classify(dataPoint: DataPoint): String {
        return label
    }
}