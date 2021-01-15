import decisionTree.Tree

class TestModule (private val trainingSet: DataSet, private val testSet: DataSet) {

    fun runTest(){
        trainingSet.prepareSetForTraining()
        testSet.prepareSetForTest(trainingSet)

        println("num of features: "+ trainingSet.numberOfFeatures+" \nnum of Instances: " + trainingSet.numberOfInstance)
        println(trainingSet.getInformationGain().toString())

        val tree = Tree()
        val root = tree.build(trainingSet.data)

        for(testData in testSet.data)
            println("prediction: " + root.classify(testData) + " ; label: " + testData.label)
    }
}