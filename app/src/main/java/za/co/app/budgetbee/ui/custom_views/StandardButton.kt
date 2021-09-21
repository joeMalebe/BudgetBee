package za.co.app.budgetbee.ui.custom_views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class StandardButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, dyfStyleAttr: Int = 0) : AppCompatButton(context, attrs, dyfStyleAttr) {
    init {
        typeface = Typeface.createFromAsset(context.assets, "fonts/CopperplateGothicBold.ttf")
    }
}