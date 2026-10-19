package ait.hu.minesweeper.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf

import kotlin.random.Random

data class BoardCell(val row: Int, val col: Int)
data class Field(
    var type: FieldType, var minesAround: Int, private val initialClickStatus: Boolean, private val initialFlagged: Boolean) {
    var wasClicked by mutableStateOf(initialClickStatus)
    var flagged by mutableStateOf(initialFlagged)}

enum class FieldType{
    Bomb, Empty, Number
}

class MinesweeperViewModel : ViewModel() {

    var boardSize by mutableIntStateOf(0)
    var bombs by mutableIntStateOf(0)
    var board by mutableStateOf(
        Array(boardSize) { Array(boardSize) { null as Field? } })

    var gameEnd by mutableStateOf(false)
    var flagMode by mutableStateOf(false)

    // creating new board for mode changes & resets
    fun setNewBoard(size: Int) {
        gameEnd = false
        flagMode = false
        boardSize = size
        bombs = boardSize - Random.nextInt(3)
        board = Array(boardSize){ Array(boardSize) {null as Field? } }
        setBombs(bombs, board, boardSize)
        setCells(board, boardSize)
    }

    // placing bombs
    fun setBombs(bombs: Int, board: Array<Array<Field?>>, boardSize: Int) {
        var i = 0
        var row: Int
        var col: Int
        while (i < bombs) {
            row = Random.nextInt(boardSize)
            col = Random.nextInt(boardSize)

            if (board[row][col]?.type == FieldType.Bomb) {
                continue
            } else {
                board[row][col] = Field(FieldType.Bomb, 0,
                    initialClickStatus = false,
                    initialFlagged = false
                )
            }
            i += 1
        }
    }

    // adding all cells
    fun setCells(board: Array<Array<Field?>>, boardSize: Int) {
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                val cell = BoardCell(row, col)
                if (board[row][col]?.type == FieldType.Bomb) {
                    board[row][col] = Field(FieldType.Bomb,checkMines(board, cell, boardSize),
                        initialClickStatus = false,false
                    )
                } else if (checkMines(board, cell, boardSize ) > 0){
                    board[row][col] = Field(FieldType.Number,
                        checkMines(board, cell, boardSize),
                        initialClickStatus = false, false)
                } else {
                    val cell = BoardCell(row, col)
                    board[row][col] = Field(FieldType.Empty,
                        checkMines(board, cell, boardSize),
                        initialClickStatus = false, false )
                }

            }
        }
    }

    // checking number of mines per cell
    fun checkMines(board: Array<Array<Field?>>, boardCell: BoardCell, boardSize: Int) : Int {
        var mine = 0

        for (row in boardCell.row - 1..boardCell.row + 1) {
            if (row !in 0..<boardSize) {
                continue
            }
            for (col in boardCell.col - 1 .. boardCell.col + 1) {
                if (col !in 0..<boardSize) {
                    continue
                } else if (board[row][col] == boardCell) {
                    continue
                } else {
                    if (board[row][col]?.type == FieldType.Bomb) {
                        mine += 1
                    }
                }
            }
        }

        return mine
    }

    // function for what to do if cell is being clicked
    fun onCellClicked(cell: BoardCell, boardSize: Int, flagmode: Boolean) {

        if (board[cell.row][cell.col]?.wasClicked == true) {
            return
        }

        if(gameEnd || checkWin()) {
            return
        }

        if (flagmode) {

            if (board[cell.row][cell.col] != null && board[cell.row][cell.col]?.wasClicked == false)  {
                board[cell.row][cell.col]?.flagged?.let { board[cell.row][cell.col]?.flagged = !it }
            }
            if (board[cell.row][cell.col]?.type == FieldType.Bomb) {
                bombs--
            } else {
                bombs ++
            }

        } else {

            if (board[cell.row][cell.col]?.flagged == true) {
                return
            }

            if (board[cell.row][cell.col]?.type == FieldType.Bomb) {
                board[cell.row][cell.col]?.wasClicked = true
                gameEnd = true
                return
            }

            board[cell.row][cell.col]?.wasClicked = true

            if (board[cell.row][cell.col]?.minesAround == 0) {
                for (row in cell.row - 1..cell.row + 1) {
                    for (col in cell.col - 1..cell.col + 1) {
                        if (row in 0 until boardSize && col in 0 until boardSize && board[row][col]?.type != FieldType.Bomb) {
                            val cell = BoardCell(row, col)
                            onCellClicked(cell, boardSize, flagmode)
                        }

                    }
                }
            }
        }
    }

    // checks win
     fun checkWin(): Boolean {
        return bombs == 0
    }

    // resets
    fun startOver() {
        setNewBoard(boardSize)
    }





}