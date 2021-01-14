import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.collections.ArrayList

data class DataSet(val data: List<DataPoint>) {
    var numberOfInstance: Int = data.size
    var numberOfFeatures: Int = data[0].map.keys.size
    private var discreetValuesOfAttributes: ArrayList<Pair<String, ArrayList<Double>>>? = null

    fun prepareSetForTraining(){
        val continuousValues = getContinuousDataValues()
        findDiscreetValues(continuousValues)
        setDiscreetValues()
    }

    fun prepareSetForTest(trainingSet: DataSet) {
        this.discreetValuesOfAttributes = trainingSet.discreetValuesOfAttributes
        setDiscreetValues()
    }

    fun getInformationGain() : ArrayList<Pair<String, Double>> {
        val informationGain = ArrayList<Pair<String, Double>>()
        for (iterator in data[0].map.keys.indices)
            informationGain.add(Pair(data[0].map.keys.elementAt(iterator)
                    , Utils.informationGain(data) {
                Triple(it.map.keys.elementAt(iterator),it.map.values.elementAt(iterator), it.label!!) }))
        return informationGain
    }

    private fun getContinuousDataValues() : ArrayList<Pair<String, ArrayList<Pair<Double,Any>>>> {
        val continuousSets = ArrayList<Pair<String, ArrayList<Pair<Double,Any>>>>()
        for (attribute in data[1].map.values) {
            val index = data[1].map.values.indexOf(attribute)
            when {
                attribute.toString().toIntOrNull() is Int || attribute.toString().toDoubleOrNull() is Double-> {
                    val values = ArrayList<Pair<Double,Any>>()
                    for (instance in data)
                        values.add(Pair(instance.map.values.elementAt(index)!!.toString().toDouble(), instance.label!!))
                    continuousSets.add(Pair(data[1].map.keys.elementAt(index), values))
                }
            }
        }
        return continuousSets
    }

    private fun findDiscreetValues(continuousSets: ArrayList<Pair<String, ArrayList<Pair<Double,Any>>>>?) {
        discreetValuesOfAttributes = ArrayList()
        for (set in continuousSets!!){
            val newValues = ArrayList<Double>()
            set.second.sortBy { it.first }
            var currentLabel = set.second[0].second
            for (index in 0 until set.second.size - 1){
                if(set.second[index + 1].second != currentLabel && set.second[index].first != set.second[index + 1].first) {
                    currentLabel = set.second[index + 1].second
                    newValues.add(BigDecimal(
                            0.5 * (set.second[index].first + set.second[index + 1].first)
                    ).setScale(2, RoundingMode.HALF_EVEN).toDouble())
                }
            }
            newValues.add(BigDecimal(set.second.last().first).setScale(2, RoundingMode.HALF_EVEN).toDouble())
            discreetValuesOfAttributes!!.add(Pair(set.first, newValues))
        }
    }

    private fun setDiscreetValues() {
        var iterator = 0
        for (attribute in data[0].map.keys) {
            val index = data[0].map.keys.indexOf(attribute)
            if (attribute == discreetValuesOfAttributes!![iterator].first) {
                for (dataPoint in data) {
                    val x = dataPoint.map.values.elementAt(index).toString().toDouble()
                    dataPoint.map[attribute] = ">" + discreetValuesOfAttributes!![iterator].second.first { it >= x }
                }
                iterator++
            }
        }
    }
}

data class DataPoint(val map: MutableMap<String, Any?>, val label: Any?)
