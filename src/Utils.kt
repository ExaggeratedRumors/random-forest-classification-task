import java.nio.file.Paths
import kotlin.math.ln

class Utils {
    companion object{
        val trainingPath = Paths.get("").toAbsolutePath().toString()+"/src/data/iris.csv"
        val testPath = Paths.get("").toAbsolutePath().toString()+"/src/data/iristest.csv"
        const val hasID = true

        private fun log2(value: Double, numberOfSamples: Double) = if (value == 0.0) 1.0
        else ln(value / numberOfSamples) / ln(2.0)

        private fun entropy(label: ArrayList<Int>, numberOfInstances: Int) : Double {
            var entropy = .0
            val numberOfAttributes = label.sum()
            for (value in label)
                entropy -= (value.toDouble() / numberOfInstances * log2(value.toDouble(), numberOfAttributes.toDouble()))
            return entropy
        }

        private fun sum(array: HashMap<String, ArrayList<Int>>) : Double {
            var sum = .0
            var numberOfInstances = 0
            for (label in array.values)
                numberOfInstances += label.sum()
            for (label in array.values)
                sum += entropy(label, numberOfInstances)
            return sum
        }

        fun informationGain(dataPoints: List<DataPoint>, extractor: (DataPoint) -> Triple<String, Any?, Any?>): Double {
            val series = dataPoints.map { extractor(it) }.groupBy { it.third }
            val distribution = ArrayList<Int>()
            for(labelGroup in series)
                distribution.add(labelGroup.value.size)

            val attributes = dataPoints.map { extractor(it) }.groupBy { it.first }.map {
                it -> val featureDistribution = HashMap<String, ArrayList<Int>>()
                for(labelGroup in it.value.groupBy{ it.second }) {
                    val attributeBias = ArrayList<Int>()
                    for (subArray in labelGroup.value.groupBy { it.third })
                        attributeBias.add(subArray.value.size)
                    featureDistribution[labelGroup.key.toString()] = attributeBias
                }
                featureDistribution
            }

            return (entropy(distribution, distribution.sum()) + attributes.map { -sum(it) }.sum())
        }
    }
}