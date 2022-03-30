package com.superstore.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.superstore.R
import com.superstore.firestore.FirestoreClass
import com.superstore.models.User
import com.superstore.utils.Constants
import com.superstore.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

/*User profile activity where user enters data and uses radio button*/
@Suppress("DEPRECATION")
class UserProfileActivity : BaseActivity(), View.OnClickListener  {

    //Retrieve the User details from intent extra
    // Create a instance of the User model class.
    private lateinit var mUserDetails: User

    //a global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null
    //global variable for image url
    private var mUserProfileImageURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        //After receiving the user details from intent set it to the UI
        // Here, the some of the edittext components are disabled because it is added at a time of Registration.
        //therefore firstname/lastname/and email are non changeable
        et_first_name.isEnabled = false
        et_first_name.setText(mUserDetails.firstName)

        et_last_name.isEnabled = false
        et_last_name.setText(mUserDetails.lastName)

        et_email.isEnabled = false
        et_email.setText(mUserDetails.email)

        // Assign the on click event to the user profile photo.
        iv_user_photo.setOnClickListener(this@UserProfileActivity)

        // Assign the on click event to the SAVE button.
        btn_save.setOnClickListener(this@UserProfileActivity)

    }

    //on click listener when user photo is clicked
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_user_photo -> {

                    // Here we will check if the permission is already allowed or we need to request for it.
                    // First of all we will check the READ_EXTERNAL_STORAGE permission and if it is not allowed we will request for the same.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {

                        // call image selection function when the user has read the read storage permission
                        Constants.showImageChooser(this@UserProfileActivity)
                    } else {

                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }

                }
                //onclick listner for save button
                R.id.btn_save ->{
                    if (validateUserProfileDetails()) {
                        // Show the progress dialog.
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (mSelectedImageFileUri != null) {

                            FirestoreClass().uploadImageToCloudStorage(
                                this@UserProfileActivity,
                                mSelectedImageFileUri
                            )
                        } else {
                            //Call the user update details function.
                            updateUserProfileDetails()
                        }
                    }
                }

            }
        }
    }

    /*
    This function will identify the result of runtime permission after the user allows or deny permission based on the unique code.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // call image selection function when the user has read the read storage permission
                Constants.showImageChooser(this@UserProfileActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        // The uri of selected image from phone storage.
                         mSelectedImageFileUri = data.data!!

                        GlideLoader(this@UserProfileActivity).loadUserPicture(
                            mSelectedImageFileUri!!,
                            iv_user_photo
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    //A function to validate the input entries for profile details.
    //for the save button
    private fun validateUserProfileDetails(): Boolean {
        return when {

            // We have kept the user profile picture is optional.
            // The FirstName, LastName, and Email Id are not editable when they come from the login screen.
            // The Radio button for Gender always has the default selected value.

            // Check if the mobile number is not empty as it is mandatory to enter.
            TextUtils.isEmpty(et_mobile_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }


    /**
     * A function to notify the success result and proceed further accordingly after updating the user details.
     */
    fun userProfileUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()


        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()
    }

    /*
    * function notifies that a profile uploaded to file storage
    *  @param imageURL After successful upload the Firebase Cloud returns the URL.*/
    fun imageUploadSuccess(imageURL: String) {

        mUserProfileImageURL = imageURL

        //Call the user update details function.
        updateUserProfileDetails()

    }

    /*function update user profile details to the firestore*/

    private fun updateUserProfileDetails(){
            //create hashmap for userdetails
            val userHashMap = HashMap<String, Any>()

            // Here the field which are not editable needs no update. So, we will update user Mobile Number and Gender for now.

            // Here we get the text from editText and trim the space
            val mobileNumber = et_mobile_number.text.toString().trim { it <= ' ' }

            val gender = if (rb_male.isChecked) {
                Constants.MALE
            } else {
                Constants.FEMALE
            }
            //update profile image field if the image URL is not empty
            if (mUserProfileImageURL.isNotEmpty()) {
                userHashMap[Constants.IMAGE] = mUserProfileImageURL
            }

            if (mobileNumber.isNotEmpty()) {
                //eg: key: mobile value: 123456788
                userHashMap[Constants.MOBILE] = mobileNumber.toLong()
            }
            //eg: key: Gender value: male
            userHashMap[Constants.GENDER] = gender

            //user profile is complete:1
            //user profile incomplete:0
            userHashMap[Constants.COMPLETE_PROFILE] = 1


        // call the registerUser function of FireStore class to make an entry in the database.
            FirestoreClass().updateUserProfileData(
                this@UserProfileActivity,
                userHashMap
            )

    }


}