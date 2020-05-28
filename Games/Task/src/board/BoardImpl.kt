package board

import board.Direction.*
import java.lang.Exception
import kotlin.math.max
import kotlin.math.min

open class BoardImpl (val widt: Int): SquareBoard {
    override val width = widt


    val skeleton = List<List<Cell>>(this.width) { i -> List(width) { j -> Cell(i + 1, j + 1)} }

    override fun getAllCells(): Collection<Cell> {
        return skeleton.flatten()
    }

    override fun getCell(i: Int, j: Int): Cell {
        return skeleton[i-1][j-1]
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return skeleton.getOrNull(i-1)?.getOrNull(j-1)
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        if (i == 0) return emptyList()
        val (low, hi) = min(jRange.first, jRange.last) to max(jRange.first, jRange.last)
        val a = min(skeleton.size, hi)
        val b = max(0, low-1)
        val res = skeleton[i-1].subList(b, a)
        return if (jRange.step>0) res else res.reversed()

    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        if (j == 0) return emptyList()
        val (low, hi) = min(iRange.first, iRange.last) to max(iRange.first, iRange.last)
        val b = max(0, low-1)
        val a = min(skeleton.size, hi)
        val res = skeleton.map { it[j - 1] }.subList(b, a)
        return if (iRange.step>0) res else res.reversed()
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            DOWN -> this@BoardImpl.getCellOrNull(this.i + 1, this.j)
            UP -> this@BoardImpl.getCellOrNull(this.i - 1, this.j)
            RIGHT -> this@BoardImpl.getCellOrNull(this.i, this.j + 1)
            LEFT -> this@BoardImpl.getCellOrNull(this.i, this.j - 1)
        }
    }
}
class GameBoardImpl <T> (val wid: Int): BoardImpl(wid), GameBoard<T> {

    val skeleton2: MutableMap<Cell, T?> = getAllCells().zip(List<T?>(wid*wid){null}).toMap().toMutableMap()


    override fun set(cell: Cell, value: T?) {
        skeleton2[cell] = value
    }
    override fun get(cell: Cell): T? = skeleton2[cell]

    fun toValues() = getAllCells().map { skeleton2[it] }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = skeleton2.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell? = skeleton2.filterValues(predicate).keys.firstOrNull()

    override fun any(predicate: (T?) -> Boolean): Boolean = toValues().any(predicate)

    override fun all(predicate: (T?) -> Boolean): Boolean = toValues().all(predicate)


}

fun createSquareBoard(width: Int): SquareBoard = BoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl<T>(width)