import decisionTree.Tree

class Main {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val parser = Parser()
            val trainingSet = parser.getData(Utils.trainingPath, Utils.hasID)
            trainingSet.prepareSetForTraining()
            val testSet = parser.getData(Utils.testPath, Utils.hasID)
            testSet.prepareSetForTest(trainingSet)

            println("num of features: "+ trainingSet.numberOfFeatures+" \nnum of Instances: " + trainingSet.numberOfInstance)
            println(trainingSet.getInformationGain().toString())

            val tree = Tree()
            val root = tree.build(trainingSet.data)

            println(root.classify(testSet.data[0]))
        }
    }
}