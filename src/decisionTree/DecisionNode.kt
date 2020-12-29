package decisionTree

import DataPoint

class DecisionNode (private val key: String, private val children: Map<String, Node>?) : Node() {

    override fun classify(dataPoint: DataPoint): String {
        val value = dataPoint.map[key]
        val subtree = children?.get(value)!!
        return subtree.classify(dataPoint)
    }
}