package com.superstore.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

//custom widget for radio button to apply custom font
//This class will be used for Custom font text using the Radio Button which inherits the AppCompatRadioButton clas
class MSPRadioButton(context: Context, attrs: AttributeSet) : AppCompatRadioButton(context, attrs)  {

    init {
        // Call the function to apply the font to the components.
        applyFont()
    }

    //apply font to radio button
    private fun applyFont() {

        // This is used to get the file from the assets folder and set it to the title textView.
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)
    }

}