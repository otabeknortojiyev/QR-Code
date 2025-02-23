package uz.gita.otabek.hashcode

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class SquaresView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var squareWeightTemp: Float = 0f
    private var squareHeightTemp: Float = 0f
    private val scope = CoroutineScope(Dispatchers.Default)

    var matrixTemp: Array<IntArray>? = null

    var weightCount = 1
    var heightCount = 1
    var squareCount: Int = 0
        set(value) {
            field = value
            invalidate()
        }
    private val paintWhite = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val paintBlack = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLACK
    }
    private val paintBlue = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLUE
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val squareWeight = (width / weightCount).toFloat()
        squareWeightTemp = squareWeight
        val squareHeight = (height / heightCount).toFloat()
        squareHeightTemp = squareHeight

        if (matrixTemp == null) {
            matrixTemp = generateMatrix(heightCount, weightCount)
        }

        for (i in 0 until heightCount) {
            for (j in 0 until weightCount) {
                val left = j * squareWeight
                val top = i * squareHeight
                val right = left + squareWeight
                val bottom = top + squareHeight
                val paint = when (matrixTemp!![i][j]) {
                    0 -> paintWhite
                    1 -> paintBlack
                    else -> paintBlue
                }
                canvas.drawRect(left, top, right, bottom, paint)
            }
        }
    }

    private fun generateMatrix(m: Int, n: Int): Array<IntArray> {
        val matrix = Array(m) { IntArray(n) }
        for (i in 0 until m) {
            for (j in 0 until n) {
                matrix[i][j] = Random.nextInt(2)
            }
        }
        return matrix
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y

            val col = (x / squareWeightTemp).toInt()
            val row = (y / squareHeightTemp).toInt()

            if (row in matrixTemp!!.indices && col in matrixTemp!![row].indices && matrixTemp!![row][col] != 1 && matrixTemp!![row][col] != 2) {
                changeColor(matrixTemp!!, row, col)
            }
        }
        return true
    }

    private fun changeColor(grid: Array<IntArray>, x: Int, y: Int) {
        val m = grid.size
        val n = grid[0].size
        val visited = Array(m) { BooleanArray(n) }
        for (i in 0 until m) {
            for (j in 0 until n) {
                visited[i][j] = grid[i][j] != 0
            }
        }
        findLands(grid, x, y, visited)
    }


    private fun findLands(mat: Array<IntArray>, x: Int, y: Int, arr: Array<BooleanArray>) {
        if (x < 0 || y < 0 || x >= mat.size || y >= mat[0].size || mat[x][y] == 1 || mat[x][y] == 2 || arr[x][y]) return
        if (mat[x][y] == 0) arr[x][y] = true
        matrixTemp!![x][y] = 2
        mat[x][y] = 2

        scope.launch {
            delay(500)
            invalidate()
            findLands(mat, x + 1, y, arr)
            findLands(mat, x, y + 1, arr)
            findLands(mat, x - 1, y, arr)
            findLands(mat, x, y - 1, arr)
        }
    }
}