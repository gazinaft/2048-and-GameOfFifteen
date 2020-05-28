package games.gameOfFifteen

import board.*
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(val initializer: GameOfFifteenInitializer): Game {
    lateinit var skeleton: GameBoard<Int>
    override fun initialize() {
        skeleton = createGameBoard<Int>(4)
        for (i in 1..4) {
            for (j in 1..4) {
                if (i == 4 && j == 4) skeleton[Cell(i, j)] = null
                else skeleton[Cell(i, j)] = initializer.initialPermutation[ 4*i - 4 + j - 1]
            }
        }
    }

    override fun canMove(): Boolean = true

    override fun get(i: Int, j: Int): Int? = skeleton[Cell(i, j)]

    override fun processMove(direction: Direction) {
        val cell = skeleton.find { it == null } ?: return
        val swap: Cell = with(skeleton) {
            cell.getNeighbour(direction.reversed())
        } ?: return
        val temp = skeleton[cell]
        skeleton[cell] = skeleton[swap]
        skeleton[swap] = temp
    }

    override fun hasWon(): Boolean = skeleton.getAllCells().map { skeleton[it] }.take(15) == List<Int>(15){it+1}

}