package com.quikliq.quikliqdriver.utilities


import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.quikliq.quikliqdriver.R

class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    AppCompatImageView(context, attrs, defStyle) {
    var name: String? = null

    init {
        if (attrs != null) {
            val array = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.CircleImageView
            )

            this.name = array.getString(R.styleable.CircleImageView_name1)
            array.recycle()
        }
    }

}
