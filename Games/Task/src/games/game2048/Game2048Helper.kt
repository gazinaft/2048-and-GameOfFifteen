package games.game2048

/*
 * This function moves all the non-null elements to the beginning of the list
 * (by removing nulls) and merges equal elements.
 * The parameter 'merge' specifies the way how to merge equal elements:
 * it returns a new element that should be present in the resulting list
 * instead of two merged elements.
 *
 * If the function 'merge("a")' returns "aa",
 * then the function 'moveAndMergeEqual' transforms the input in the following way:
 *   a, a, b -> aa, b
 *   a, null -> a
 *   b, null, a, a -> b, aa
 *   a, a, null, a -> aa, a
 *   a, null, a, a -> aa, a
 *
 * You can find more examples in 'TestGame2048Helper'.
*/
fun <T : Any> List<T?>.moveAndMergeEqual(merge: (T) -> T): List<T> {
    val start = this.filterNotNull().toMutableList()
    if (start.isEmpty()) return emptyList()
    val res = mutableListOf<T>()
    for (i in start.indices){
        if (i == start.size-1) {
            res += start[i]
            break
        }
        if (start[i] == start[i + 1]) {
            res += merge(start[i])
            start.removeAt(i+1)
            if (i + 1 == start.size) break
        }
        else {
            res += start[i]
        }
    }
    return res
}
fun main() {
    val list = listOf<Int?>(5, 5,null, 2, 3, null,  3, 3).moveAndMergeEqual { it*2 }
    println(list)
}
