import java.io.File
import java.io.InputStream

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val mirrorsArray = inputStream.bufferedReader().use { it.readText().split("\r\n\r\n") }

    var sum = (0).toLong()
    for (mirror in mirrorsArray) {
        val mirrorArray = mirror.split("\r\n")
        var result = identicalRow(mirrorArray)
        if (result > 0) {
            sum += 100 * result
            continue
        }
        val mirrorArray2 = transform(mirrorArray)
        result = identicalRow(mirrorArray2)
        sum += result
    }
    println(sum)
}

private fun identicalRow(mirrorArray: List<String>): Int {
    var result = -1
    for (rowIndex in mirrorArray.indices) {
        val index = rowIndex-1
        var fixed = false
        if (index >= 0 && (mirrorArray[rowIndex] == mirrorArray[index] || difference(mirrorArray[rowIndex], mirrorArray[index]))) {
            if (mirrorArray[rowIndex] != mirrorArray[index]){
                fixed = true
            }
            var indexCopy = index - 1
            var rowIndexCopy = rowIndex + 1
            var found = true
            while (indexCopy >= 0 && rowIndexCopy <= mirrorArray.lastIndex) {
                if (mirrorArray[rowIndexCopy] == mirrorArray[indexCopy]) {
                    indexCopy--
                    rowIndexCopy++
                } else if (!fixed && difference(mirrorArray[rowIndexCopy], mirrorArray[indexCopy])) {
                    fixed = true
                    indexCopy--
                    rowIndexCopy++
                } else {
                    found = false
                    break
                }
            }
            if (found && fixed) {
                result = rowIndex
                break
            }
        }
    }
    return result
}

private fun difference(first: String, second: String): Boolean {
    var notFixed = true
    for (i in first.indices) {
        if (first[i] == second[i])
            continue
        if (notFixed)
            notFixed = false
        else
            return false
    }
    return true
}

private fun transform(mirrorArray: List<String>): List<String> {
    val output = mutableListOf<String>()
    for (i in mirrorArray[0].indices) {
        output.add("")
        for (j in mirrorArray.indices) {
            output[i] = output[i] + mirrorArray[j][i]
        }
    }
    return output.toList()
}