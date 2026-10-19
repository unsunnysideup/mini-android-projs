package ait.hu.minesweeper.ui.screen
import ait.hu.minesweeper.R
import android.os.SystemClock
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun MinesweeperScreen(modifier: Modifier, viewModel: MinesweeperViewModel = viewModel()) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var mode by remember { mutableStateOf("No mode selected") }
        var resetCounter by remember { mutableIntStateOf(0) }
        var gameStatus by remember { mutableStateOf("N/A")}

        // game status
        if (viewModel.checkWin() && mode != "No mode selected") {
            gameStatus = stringResource(R.string.win_message)
        } else if (viewModel.gameEnd && !viewModel.checkWin())  {
            gameStatus = stringResource(R.string.lose_message)
        }

        Text(text = stringResource(R.string.game_title), fontSize = 40.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 15.dp))

        Row{
            Text(text = stringResource(R.string.info_panel, mode, gameStatus), fontSize = 15.sp)
            Stopwatch(mode, resetCounter)
        }


        Row(modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth(.7f),
            Arrangement.SpaceBetween){
            Button(
                onClick = {
                    mode = "Easy"
                    viewModel.setNewBoard(5)
                    gameStatus = "In Progress"
                    resetCounter += 1

                },
                colors = ButtonDefaults.buttonColors(Color(0xff112c4b))
            ) {
                Text(stringResource(R.string.mode_easy))
            }

            Button(
                onClick = {
                    mode = "Medium"
                    viewModel.setNewBoard(7)
                    gameStatus = "In Progress"
                    resetCounter += 1
                },
                colors = ButtonDefaults.buttonColors(Color(0xff112c4b))
            ) {
                Text(stringResource(R.string.mode_medium))
            }

            Button(
                onClick = {
                    mode = "Hard"
                    viewModel.setNewBoard(9)
                    gameStatus = "In Progress"
                    resetCounter += 1
                },
                colors = ButtonDefaults.buttonColors(Color(0xff112c4b))
            ) {
                Text(stringResource(R.string.mode_hard))
            }
        }

        MinesweeperBoard(
            board = viewModel.board,
            onCellClicked = {
                viewModel.onCellClicked(it, boardSize = viewModel.boardSize, viewModel.flagMode)
            },
            boardSize = viewModel.boardSize,
        )


        Row(modifier = Modifier
            .padding(top = 15.dp)
            .fillMaxWidth(.8f),
            Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    viewModel.flagMode = !viewModel.flagMode},
                colors = if (viewModel.flagMode) ButtonDefaults.buttonColors(containerColor = Color(0xff588157)) else ButtonDefaults.buttonColors(containerColor = Color(0xff112c4b))
            ) {
                Text("⛳\uFE0F", fontSize = 20.sp)
            }

            Button(
                onClick = {
                    viewModel.startOver()
                    gameStatus = "In Progress"
                    resetCounter += 1},
                colors = ButtonDefaults.buttonColors(Color(0xff112c4b))

            ) {
                Text(stringResource(R.string.restart), fontSize = 20.sp)
            }

        }


    }
}

@Composable
fun MinesweeperBoard(
    board: Array<Array<Field?>>,
    boardSize: Int,
    onCellClicked: (BoardCell) -> Unit
) {
    val textMeasurer = rememberTextMeasurer()


    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(1.0f)
            .pointerInput(boardSize) {
                detectTapGestures { offSet ->

                    val row = (offSet.y / (size.height / boardSize)).toInt()
                    val col = (offSet.x / (size.width / boardSize)).toInt()
                    onCellClicked(BoardCell(row, col))

                }


            }
    ) {
        val gridSize = size.minDimension
        val cellSize = gridSize / boardSize

        for (i in 0..boardSize ) {
            drawLine(
                color = Color.Black,
                strokeWidth = 5f,
                start = Offset(cellSize * i, 0f),
                end = Offset(cellSize * i, gridSize)
            )

            drawLine(
                color = Color.Black,
                strokeWidth = 5f,
                start = Offset(0f, cellSize * i),
                end = Offset(gridSize, cellSize * i)
            )
        }


        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                val player = board[row][col]

                    val numberColor = when (player?.minesAround) {
                        1 -> Color.Blue
                        2 -> Color(0xFF3DB252)
                        3 -> Color(0xffD94207)
                        4 -> Color(0xff341539)
                        5 -> Color.Red
                        else -> Color.Black
                    }

                    val symbol = when {
                        player?.type == FieldType.Bomb && player.wasClicked-> "\uD83D\uDCA3"
                        player?.type == FieldType.Number && player.wasClicked-> "${player.minesAround}"
                        player?.type == FieldType.Empty && player.wasClicked -> "\uD83C\uDF31"
                        player?.flagged == true -> "⛳\uFE0F"
                        else -> ""
                    }

                val textLayoutResult: TextLayoutResult =
                    textMeasurer.measure(
                        text = symbol,
                        style = TextStyle(
                                fontSize = (cellSize * .75).roundToInt().toSp(),
                                fontWeight = FontWeight.Bold,
                                color = numberColor
                            )
                        )
                val textSize = textLayoutResult.size

                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                            x  = (cellSize/2 + col*cellSize) - textSize.width/2,
                            y = (cellSize/2 + row*cellSize) - textSize.height/2
                        )
                    )

            } }

        }
    }


// stopwatch
@Composable
fun Stopwatch(mode: String, resetCounter: Int, viewModel: MinesweeperViewModel = viewModel()) {

    var isRunning by remember { mutableStateOf(false) }
    var baseMs by remember { mutableLongStateOf(0L) }
    var elapsedMs by remember { mutableLongStateOf(0L) }


    if(mode != "No mode selected") {
        baseMs = SystemClock.elapsedRealtime() - elapsedMs
        isRunning = true
    }

    if(viewModel.gameEnd || viewModel.checkWin()) {
        isRunning = false
    }

    LaunchedEffect(resetCounter) {
        elapsedMs = 0L
        baseMs = SystemClock.elapsedRealtime()
        isRunning = false


    }

    LaunchedEffect(isRunning, baseMs) {
        if (!isRunning) return@LaunchedEffect
        while (true) {
            val now = SystemClock.elapsedRealtime()
            elapsedMs = now - baseMs
            delay(.5.milliseconds) // ~60 FPS update rate

        }
    }

    fun formatTime(ms: Long): String {
        val minutes = ms / 60000
        val seconds = (ms / 1000) % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    Text(formatTime(elapsedMs))

}



