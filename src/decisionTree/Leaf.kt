package decisionTree

import DataPoint

class Leaf (private val label: String, depth: Int): Node() {
    override fun classify(dataPoint: DataPoint): String {
        return label
    }
}