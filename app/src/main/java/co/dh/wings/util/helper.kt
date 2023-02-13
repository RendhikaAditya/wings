package co.dh.wings.util

import android.graphics.Paint
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.widget.TextView
import java.text.NumberFormat
import java.util.*

class helper {
    fun formatRupiah(number:Double): String {

        val locale = Locale("id", "ID")
        val formatter = NumberFormat.getCurrencyInstance(locale)
        val hargaFormat = formatter.format(number)
        return hargaFormat;
    }

    fun addStrikethrough(textView: TextView, text: String) {
        val spannableString = SpannableString(text)
        spannableString.setSpan(StrikethroughSpan(), 0, text.length, 0)
        textView.text = spannableString
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun calculateDiscount(originalPrice: Double, discountPercentage: Double): Double {
        return originalPrice - (originalPrice * (discountPercentage / 100))
    }
}