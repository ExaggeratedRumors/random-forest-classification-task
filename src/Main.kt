class Main {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val parser = Parser()
            val trainingSet = parser.getData(Utils.trainingPath, Utils.hasID)
            val testSet = parser.getData(Utils.testPath, Utils.hasID)
            val testModule = TestModule(trainingSet, testSet)
            testModule.runTest()
        }
    }
}