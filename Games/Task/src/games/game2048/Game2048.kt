package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        for (i in 1..board.width) {
            for (j in 1..board.width) {
                board[Cell(i, j)] = null
            }
        }
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val (cell, value) = initializer.nextValue(this)?: return
    this[cell] = value
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    if (rowOrColumn.mapNotNull { this[it] }.isEmpty()) return false
    val a = rowOrColumn.map { cell -> this[cell] }
    val merged = rowOrColumn.map { this[it] }.moveAndMergeEqual { it*2 }
    rowOrColumn.forEach { cell -> this[cell] = null }
    for ((index, elem) in merged.withIndex()) {
        this[rowOrColumn[index]] = elem
    }
    return merged != a.subList(0, merged.size)
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game .
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    val workList = when (direction){
        Direction.UP -> List(width) { getColumn(1..width, it+1)}
        Direction.DOWN -> List(width) { getColumn(width downTo 1, it+1)}
        Direction.LEFT -> List(width) { getRow(it+1, 1..width)}
        Direction.RIGHT -> List(width) { getRow(it+1, width downTo 1)}
    }
    val bools = workList.map {row -> this.moveValuesInRowOrColumn(row) }
    return bools.any { it }
}