package decisionTree

import DataPoint

class Tree {

    fun build(dataPoints: List<DataPoint>): Node {
        return build(dataPoints, 0)
    }

    private fun build(dataPoints: List<DataPoint>, depth: Int): Node {
        val labelDistribution = dataPoints.groupBy { it.label }
        for ( i  in labelDistribution.entries)
            if (i.value.size == dataPoints.size) return Leaf(i.key.toString(), depth)
        val attributes = dataPoints.map { it.map.keys }.flatten().distinct()
        if (attributes.size == 1) return getMajorityLabel(dataPoints, depth)
        val attribute = findHighestGain(attributes, dataPoints)
        val subSet = buildSubSet(attribute!!, dataPoints)
        return DecisionNode(attribute.first, subSet.map { Pair(it.first.toString(), build(it.second, depth+1)) }.toMap(), depth)
    }

    private fun findHighestGain(attributes: List<String>, dataPoints: List<DataPoint>): Pair<String, Double>? {
        return attributes.map { attr ->
            Pair(attr, Utils.informationGain(dataPoints) {
                Triple(attr, it.map.values.elementAt(attributes.indexOf(attr)), it.label)
            })
        }.maxByOrNull { it.second }
    }

    private fun getMajorityLabel(dataPoints: List<DataPoint>, depth: Int): Leaf {
        val labels = dataPoints.groupingBy { it.label }.eachCount()
        return Leaf(labels.keys.elementAt(
                labels.values.indexOf(
                        labels.values.maxOf { it }
                )
        ).toString(), depth)
    }

    private fun buildSubSet(attribute: Pair<String, Double>, dataPoints: List<DataPoint>): List<Pair<Any?, List<DataPoint>>> {
        return dataPoints.groupBy { it.map[attribute.first] }.entries.map { sample ->
            Pair(sample.key, sample.value.map { dataPoint ->
                DataPoint(dataPoint.map.filterKeys {
                    it != attribute.first
                } as MutableMap<String, Any?>, dataPoint.label)
            })
        }
    }
}