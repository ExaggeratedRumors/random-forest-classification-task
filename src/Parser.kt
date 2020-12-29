import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

class Parser {

    @Throws(IOException::class)
    fun getData(path: String, hasID: Boolean): DataSet {
        println("read dataset from $path")
        val shift = if (hasID) 1 else 0
        BufferedReader(InputStreamReader(FileInputStream(path))).use { `data` ->
            val dataSet: MutableList<DataPoint> = ArrayList()
            var input = `data`.readLine()
            val attributes = input.split(",".toRegex()).toTypedArray()
            while (`data`.readLine().also { input = it } != null) {
                val samples = input!!.split(",".toRegex()).toTypedArray()
                val values = HashMap<String, Any?>()
                for (i in shift until samples.size - 1)
                    values[attributes[i]] = samples[i]
                dataSet.add(DataPoint(values,samples[samples.size-1]))
            }
            return DataSet(dataSet)
        }
    }
}

