package com.example.mon_calendrier_sante_et_fitness.ui.historique

import java.util.Locale
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

class GraphiqueView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    enum class Type { LIGNE, BARRES }

    var type: Type = Type.LIGNE
        set(value) {
            field = value
            invalidate()
        }

    var valeurs: List<Float> = emptyList()
        set(value) {
            field = if (value.isEmpty()) listOf(0f) else value
            invalidate()
        }

    private val paintGrid = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E6E6E6")
        strokeWidth = 2f
    }

    private val paintGreen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#6CC56C")
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private val paintFillGreen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#6CC56C")
        style = Paint.Style.FILL
    }

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#777777")
        textSize = 20f
        textAlign = Paint.Align.CENTER
    }

    private val paintYText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#777777")
        textSize = 20f
        textAlign = Paint.Align.LEFT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val left = 110f
        val right = width - 20f
        val top = 20f
        val bottom = height - 45f

        val graphWidth = right - left
        val graphHeight = bottom - top

        val data = if (valeurs.isEmpty()) listOf(0f) else valeurs

        val minValue: Float
        val maxValue: Float

        if (type == Type.BARRES) {
            minValue = 0f
            maxValue = max(data.maxOrNull() ?: 1f, 1f)
        } else {
            // Aide avec IA (ChatGPT)
            // Pour m'aider à faire en sorte que l'axe Y du graphique s'ajuste automatiquement selon les données enregistrées par l'utilisateur.
            val minData = data.minOrNull() ?: 0f
            val maxData = data.maxOrNull() ?: 1f

            minValue = floor(minData - 1f)
            maxValue = ceil(maxData + 1f)
        }

        val range = max(maxValue - minValue, 1f)

        repeat(5) { i ->
            val y = top + i * graphHeight / 4
            canvas.drawLine(left, y, right, y, paintGrid)

            val value = maxValue - i * range / 4

            val label = if (type == Type.BARRES) {
                "${value.toInt()} min"
            } else {
                String.format(Locale.CANADA_FRENCH, "%.1f kg", value)
            }

            canvas.drawText(label, 10f, y + 7f, paintYText)
        }

        if (type == Type.LIGNE) {
            val path = Path()

            data.forEachIndexed { index, valeur ->
                val x = if (data.size == 1) {
                    left + graphWidth / 2
                } else {
                    left + index * graphWidth / (data.size - 1)
                }

                // Aide avec IA (ChatGPT)
                // Pour comprendre comment calculer la position des points et des barres dans un graphique personnalisé Android.
                val y = top + graphHeight - ((valeur - minValue) / range * graphHeight)

                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)

                canvas.drawCircle(x, y, 5f, paintFillGreen)
                canvas.drawText("S${index + 1}", x, height - 12f, paintText)
            }

            canvas.drawPath(path, paintGreen)

        } else {
            val space = graphWidth / data.size
            val barWidth = space * 0.35f

            data.forEachIndexed { index, valeur ->
                val x = left + index * space + space / 2 - barWidth / 2
                val barHeight = if (maxValue == 0f) 0f else valeur / maxValue * graphHeight
                val y = bottom - barHeight

                canvas.drawRoundRect(
                    x,
                    y,
                    x + barWidth,
                    bottom,
                    10f,
                    10f,
                    paintFillGreen
                )

                canvas.drawText("S${index + 1}", x + barWidth / 2, height - 12f, paintText)
            }
        }
    }
}