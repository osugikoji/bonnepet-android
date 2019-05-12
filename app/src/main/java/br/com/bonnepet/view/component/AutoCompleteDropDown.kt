package br.com.bonnepet.view.component

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import br.com.bonnepet.R

class AutoCompleteDropDown : AppCompatAutoCompleteTextView {
    private var isPopup: Boolean = false

    val position: Int
        get() = ListView.INVALID_POSITION

    constructor(context: Context) : super(context)

    constructor(arg0: Context, arg1: AttributeSet) : super(arg0, arg1)

    constructor(arg0: Context, arg1: AttributeSet, arg2: Int) : super(arg0, arg1, arg2)

    override fun enoughToFilter(): Boolean {
        return true
    }

    override fun onFocusChanged(
        focused: Boolean, direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            if (this.text.isNotEmpty()) {
                performFiltering("", 0)
            }
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
            keyListener = null
            dismissDropDown()
        } else {
            isPopup = false
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (isPopup) {
                    dismissDropDown()
                } else {
                    requestFocus()
                    showDropDown()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun showDropDown() {
        super.showDropDown()
        isPopup = true
    }

    override fun dismissDropDown() {
        super.dismissDropDown()
        isPopup = false
    }

    override fun setCompoundDrawablesWithIntrinsicBounds(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ) {
        var customRight = right
        val dropdownIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_down_black_24dp)
        if (dropdownIcon != null) {
            customRight = dropdownIcon
            customRight.mutate().alpha = 66
        }
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, customRight, bottom)
    }
}
