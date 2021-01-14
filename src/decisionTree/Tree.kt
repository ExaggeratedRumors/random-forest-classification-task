package decisionTree

import DataPoint

class Tree {

    fun build(dataPoints: List<DataPoint>): Node {
        val labelDistribution = dataPoints.groupBy { it.label }
        for ( i  in labelDistribution.entries)
            if (i.value.size == dataPoints.size) return Leaf(i.key.toString())
        val attributes = dataPoints.map { it.map.keys }.flatten().distinct()

        if(attributes.size == 1) {
            val labels = dataPoints.groupingBy { it.label }.eachCount()
            return Leaf(labels.keys.elementAt(
                    labels.values.indexOf(
                            labels.values.maxOf { it }
                    )
            ).toString())
        }

        val attribute = attributes.map { attr ->
            Pair(attr, Utils.informationGain(dataPoints) { Triple(attr, it.map.values.elementAt(attributes.indexOf(attr)), it.label) })
        }.maxByOrNull { it.second }

        val remaining = dataPoints.groupBy { it.map[attribute!!.first] }
        val filteredRemaining = remaining.entries.map {
            entry ->
            Pair(entry.key, entry.value.map { dataPoint ->
                DataPoint(dataPoint.map.filterKeys { it != attribute!!.first } as MutableMap<String, Any?>, dataPoint.label)
            })
        }

        val children = filteredRemaining.map { Pair(it.first.toString(), build(it.second)) }.toMap()
        return DecisionNode(attribute!!.first, children)
    }
}