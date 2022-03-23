package com.superstore.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.superstore.R
import kotlinx.android.synthetic.main.dialog_progress.*

//activity to define functions and members used in all activities
//activity inherits AppCompatActivity class to replace AppCompatActivity in other classes with BaseActivity
open class BaseActivity : AppCompatActivity() {

        //global instance of progress dialog intialized in activities like register/login/etc

    private lateinit var mProgressDialog: Dialog

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

    //function to show progress diolog
    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.tv_progress_text.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    //function to hide progress dialog
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

}
