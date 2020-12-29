class Main {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val parser = Parser()
            val dataSet = parser.getData(Utils.path, Utils.hasID)

            println("num of features: "+ dataSet.numberOfFeatures+" \nnum of Instances: " + dataSet.numberOfInstance)
            println(dataSet.getInformationGain().toString())

        }
    }
}