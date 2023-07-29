package com.example.websocket_upbit.view.base

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object BindingAdapter {
    private fun Boolean?.gone(): Int {
        return when {
            this == true -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun Boolean?.invisible(): Int {
        return when {
            this == true -> View.VISIBLE
            else -> View.INVISIBLE
        }
    }

    fun Boolean?.visibilityByNullableBoolean(): Int {
        return when {
            this == true -> View.VISIBLE
            this == false -> View.INVISIBLE
            else -> View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("selected")
    fun selected(v: View, value: Boolean) {
        v.isSelected = value
    }

    @JvmStatic
    @BindingAdapter("enable")
    fun enable(v: View, value: Boolean) {
        v.isEnabled = value
    }

    @JvmStatic
    @BindingAdapter("gone")
    fun View.isGone(visibility: Boolean?) {
        this.visibility = visibility.gone()
    }

    @JvmStatic
    @BindingAdapter("invisible")
    fun View.isInvisible(visibility: Boolean?) {
        this.visibility = visibility.invisible()
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun bindAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
    }

//    @JvmStatic
//    @BindingAdapter("items")
//    fun bindItems(recyclerView: RecyclerView, data: List<*>) {
//        recyclerView.adapter?.let {
//            (it as BaseListAdapter<*, *>).setData(data as List<Nothing>)
//        }
//    }

//    @JvmStatic
//    @BindingAdapter("resource")
//    fun setAppCompatImageResource(imageView: AppCompatImageView, resource: Int) {
//        if (resource > 0) {
//            imageView.setImageResource(resource)
//        } else {
//            imageView.setImageResource(R.color.transparent)
//        }
//    }

    @JvmStatic
    @BindingAdapter("resource")
    fun setAppCompatImageResource(imageView: AppCompatImageView, drawable: Drawable?) {
        imageView.setImageDrawable(drawable)
    }

    @JvmStatic
    @BindingAdapter("textStyle")
    fun bindTextStyle(v: TextView, isSelected: Boolean) {
        v.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
    }

    @JvmStatic
    @BindingAdapter("text")
    fun bindText(v: TextView, text: String?) {
        if (text.isNullOrEmpty()) return
        if (text == v.text.toString()) return
        v.text = toHtml(text)
    }

    @JvmStatic
    @BindingAdapter("textPrice22")
    fun bindTextPrice22(v: TextView, text: String?) {
        if (text != null) {
            if (text.length > 18) {
                v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
            } else if (text.length > 16) {
                v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
            } else {
                v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22f)
            }
            v.text = text
        } else {
            v.text = ""
        }
    }

    private fun toHtml(text: String): Spanned {
        val value = text.replace("&amp;", "&")
            .replace("\n", "<br>")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&apos;", "'")
            .replace("&quot;", "\"")
            .replace("&nbsp;", " ")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(value)
        }
    }

//    @JvmStatic
//    fun circularProgressDrawable(context: Context): CircularProgressDrawable {
//        val progress = CircularProgressDrawable(context)
//        progress.setColorSchemeColors(R.color.red_500)
//        progress.strokeWidth = 5f
//        progress.centerRadius = 30f
//        progress.start()
//        return progress
//    }
}