package games.gameOfFifteen


/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */
fun MutableList<Int>.swap(index1: Int, index2: Int): MutableList<Int> {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
    return this
}

fun isEven(permutation: List<Int>): Boolean {
    var counter = 0
    val default = if (permutation.min() == 0) permutation.map { it+1 }.toMutableList()
        else permutation.toMutableList()
    while (default != List(default.size){it+1}) {
         default.forEachIndexed { index, t ->
            if (t != index + 1) {
                default.swap(t - 1, index)
                ++counter
            }
        }
    }
    return (counter) % 2 == 0
}
