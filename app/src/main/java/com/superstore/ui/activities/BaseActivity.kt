package com.superstore.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.superstore.R

//activity to define functions and members used in all activities
//activity inherits AppCompatActivity class to replace AppCompatActivityin other classeswith BaseActivity
open class BaseActivity : AppCompatActivity() {


        //snackbar function for success or error messages

        fun showErrorSnackBar(message: String, errorMessage: Boolean) {
            val snackBar =
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
            val snackBarView = snackBar.view

            if (errorMessage) {
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@BaseActivity,
                        R.color.colorSnackBarError
                    )
                )
            }else{
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@BaseActivity,
                        R.color.colorSnackBarSuccess
                    )
                )
            }
            snackBar.show()
        }
}
