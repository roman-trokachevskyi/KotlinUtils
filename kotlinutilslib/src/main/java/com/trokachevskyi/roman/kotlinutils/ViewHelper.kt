package com.trokachevskyi.roman.kotlinutils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import java.util.*

/**
 * Created by duke0808 on 24.11.16.
 */

class ViewHelper {
    interface SelectorCallback {
        fun selected(selected: String)
    }

    companion object {
        fun createSelector(context: Context, stringList: List<String>, selected: String, callback: SelectorCallback, title: String, iconRes: Int) {
            var selected = selected
            if (TextUtils.isEmpty(selected)) {
                selected = ""
            }
            val builder = AlertDialog.Builder(context)
            val strings = arrayOfNulls<String>(2)
            strings[0] = "NAME"
            strings[1] = "SELECTED"
            val ints = IntArray(2)
            ints[0] = R.id.tv_text_office_name_item_offices
            ints[1] = R.id.rb_choose_office_item_offices
            val simpleAdapter = SimpleAdapter(context, createListForChooserAdapter(stringList, selected), R.layout.item_list_stages, strings, ints)
            val listener = DialogInterface.OnClickListener { dialogInterface, i ->
                callback.selected(stringList[i])
                dialogInterface.dismiss()
            }
            builder.setSingleChoiceItems(simpleAdapter, 0, listener)
            builder.setTitle(title)
            builder.setCancelable(true)
            val alertDialog = builder.create()
            if (iconRes != -1) {
                val customTitle = getCustomTitle(title, iconRes, true, context)
                alertDialog.setCustomTitle(customTitle)
                customTitle.findViewById(R.id.close_iv).setOnClickListener { alertDialog.dismiss() }
            }
            val i1 = pxFromDp(context, 400f).toInt()
            alertDialog.show()

            val lp = WindowManager.LayoutParams()

            lp.copyFrom(alertDialog.window!!.attributes)

            lp.height = i1

            alertDialog.window!!.attributes = lp

            if (stringList.contains(selected)) {
                val i = stringList.indexOf(selected)
                alertDialog.listView.smoothScrollToPosition(i)
            }
        }

        fun createListForChooserAdapter(strings: List<String>, selected: String): List<Map<String, *>> {
            val result = ArrayList<HashMap<String, Any>>()
            for (s in strings) {
                val e = HashMap<String, Any>()
                e.put("NAME", s)
                e.put("SELECTED", s != null && s.equals(selected, ignoreCase = true))
                result.add(e)
            }
            return result
        }

        fun getCustomTitle(title: String, iconRes: Int, withCloseCross: Boolean, context: Context): View {
            val inflate = LayoutInflater.from(context).inflate(R.layout.dialog_title_layout, null)
            (inflate.findViewById(R.id.title_text) as TextView).text = title
            Glide.with(context).load(iconRes).into(inflate.findViewById(R.id.image_title) as ImageView)
            return inflate
        }


        fun createInputDialog(context: Context, titleId: Int, prevValue: String, callback: SelectorCallback, ok_text: String = " Okay", no_text: String = "No") {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(titleId)

            // Set up the input
            val input = EditText(context)
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

            input.inputType = InputType.TYPE_CLASS_TEXT
            input.setText(if (TextUtils.isEmpty(prevValue)) "" else prevValue)
            builder.setView(input)

            // Set up the buttons
            builder.setPositiveButton(ok_text, DialogInterface.OnClickListener { dialog, which ->
                callback.selected(input.text.toString().trim { it <= ' ' })
                dialog.dismiss()
            })
            builder.setNegativeButton(no_text, DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()
        }

        fun dpFromPx(context: Context, px: Float): Float {
            return px / context.resources.displayMetrics.density
        }

        fun pxFromDp(context: Context, dp: Float): Float {
            return dp * context.resources.displayMetrics.density
        }


        fun getActionBarSize(context: Context): Int {
            val tv = TypedValue()
            var actionBarHeight = 0
            if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)

            return actionBarHeight
        }
    }
}
