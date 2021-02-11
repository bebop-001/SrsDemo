/* This is an java/android implementation of the SuperMemory
 * Spaced Repetition Algorithm.
 *
 * ref:http://www.supermemo.com/english/ol/sm2source.htm
 * Explanation:
 * ref:http://www.supermemo.com/english/ol/sm2.htm
 * Algorithm with pencil/paper.
 * ref:http://www.supermemo.com/articles/paper.htm
 */
package com.example.srsdemo

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class SRS  // creator
    () {
    private var creationDate = Date()
    private var viewDate = creationDate
    private var nextInterval // in days.
            = 0
    private var EF = 1.3f // Efficiency factor.
    private var repetitions // number of successful times recognized.
            = 0
    private var assuredness = 0

    // update record based on assuredness of recognition.
    fun update(assuredness: Int): SRS {
        viewDate = Date()
        this.assuredness = assuredness
        if (assuredness >= 3.0) {
            nextInterval =
                if (repetitions == 0) 1 else if (repetitions == 1) FIRST_INTERVAL_INCREMENT else {
                    Math.ceil((nextInterval * EF).toDouble()).toInt()
                }
            repetitions++
        } else if (assuredness > 1) {
            repetitions = 0
            nextInterval = 1
        } else {
            repetitions = 0
            nextInterval = 0
        }
        EF = EF + (0.1
                - (ASSUREDNESS_MAX - assuredness)
                * (0.8 + (ASSUREDNESS_MAX - assuredness) * 0.2)).toFloat()
        if (EF < EF_MIN) EF = EF_MIN
        return this
    }

    fun clone(): SRS {
        val rv = SRS()
        rv.assuredness = assuredness
        rv.creationDate = creationDate.clone() as Date
        rv.EF = EF
        rv.nextInterval = nextInterval
        rv.repetitions = repetitions
        rv.viewDate = viewDate.clone() as Date
        return rv
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToString(dateIn: Date): String {
        val timeFormat = SimpleDateFormat("EEE LL/dd/y kk:mm z")
        // timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return timeFormat.format(dateIn)
    }

    override fun toString(): String {
        var f = ""
        for (s in format) f += "$s, "
        return String.format(
            f,
            dateToString(creationDate), dateToString(viewDate),
            assuredness, EF, repetitions, nextInterval
        )
    }

    fun toText(): String {
        var f = ""
        for (s in format) f += """
     $s
     
     """.trimIndent()
        return String.format(
            f,
            dateToString(creationDate), dateToString(viewDate),
            assuredness, EF, repetitions, nextInterval
        )
    }

    companion object {
        private const val FIRST_INTERVAL_INCREMENT = 6 //days.

        // quality selection range: 1..5
        private const val EF_MIN = 1.3f

        // Grade or assuredness of recognition.  1 = huh?? 5 = for sure.
        private const val ASSUREDNESS_MIN = 1.0f
        private const val ASSUREDNESS_MAX = 5.0f
        private val format = arrayOf(
            "Date: %s",
            "Last Viewed: %s",
            "Assuredness: %d",
            "EF: %5.3f",
            "Repetitions: %2d",
            "Next view: %d days"
        )
    }
}