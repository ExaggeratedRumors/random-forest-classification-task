import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.collections.ArrayList

data class DataSet(val trainingData: List<DataPoint>) {
    var numberOfInstance: Int = trainingData.size
    var numberOfFeatures: Int = trainingData[0].map.keys.size

    init {
        val continuousValues = getContinuousDataValues()
        val discreetValues = getDiscreetValues(continuousValues)
        setDiscreetValues(discreetValues)
    }


    fun getInformationGain() : ArrayList<Pair<String, Double>> {
        val informationGain = ArrayList<Pair<String, Double>>()
        for (iterator in trainingData[0].map.keys.indices)
            informationGain.add(Pair(trainingData[0].map.keys.elementAt(iterator)
                    , Utils.informationGain(trainingData) {
                Triple(it.map.keys.elementAt(iterator),it.map.values.elementAt(iterator), it.label!!) }))
        return informationGain
    }

    private fun getContinuousDataValues() : ArrayList<Pair<String, ArrayList<Pair<Double,Any>>>>? {
        val continuousSets = ArrayList<Pair<String, ArrayList<Pair<Double,Any>>>>()
        for (attribute in trainingData[1].map.values) {
            val index = trainingData[1].map.values.indexOf(attribute)
            when {
                attribute.toString().toIntOrNull() is Int || attribute.toString().toDoubleOrNull() is Double-> {
                    val values = ArrayList<Pair<Double,Any>>()
                    for (instance in trainingData)
                        values.add(Pair(instance.map.values.elementAt(index)!!.toString().toDouble(), instance.label!!))
                    continuousSets.add(Pair(trainingData[1].map.keys.elementAt(index), values))
                }
            }
        }
        return continuousSets
    }

    private fun getDiscreetValues(continuousSets: ArrayList<Pair<String, ArrayList<Pair<Double,Any>>>>?) : ArrayList<Pair<String,ArrayList<Double>>>? {
        val newSets = ArrayList<Pair<String,ArrayList<Double>>>()
        for (set in continuousSets!!){
            val newValues = ArrayList<Double>()
            set.second.sortBy { it.first }
            var currentLabel = set.second[0].second
            for (index in 0 until set.second.size - 1){
                if(set.second[index+1].second != currentLabel && set.second[index].first != set.second[index+1].first) {
                    currentLabel = set.second[index+1].second
                    newValues.add(BigDecimal(0.5*(set.second[index].first+set.second[index+1].first)).setScale(2, RoundingMode.HALF_EVEN).toDouble())
                }
            }
            newSets.add(Pair(set.first, newValues))
        }
        return newSets
    }

    private fun setDiscreetValues(discreetSets: ArrayList<Pair<String,ArrayList<Double>>>?) {
        var iterator = 0
        for (attribute in trainingData[0].map.keys) {
            val index = trainingData[0].map.keys.indexOf(attribute)
            if (attribute == discreetSets!![iterator].first) {
                discreetSets[iterator].second.add(.0)
                for (dataPoint in trainingData) {
                    val x = dataPoint.map.values.elementAt(index).toString().toDouble()
                    dataPoint.map[attribute] = ">" + discreetSets[iterator].second.first { it < x }
                }
                iterator++
            }
        }
    }
}

data class DataPoint(val map: MutableMap<String, Any?>, val label: Any?)
