package com.superstore.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

/*object file to declare constant info in a single file. Values can be used in the entire application
* */
//https://stackoverflow.com/questions/44038721/constants-in-kotlin-whats-a-recommended-way-to-create-them#:~:text=You%20don't%20need%20a,the%20constants%20inside%20the%20file.
object Constants {

    //firebase collection "users"
    const val USERS: String = "users"
    /*constant variables for Android SharedPreferences and username key*/
    const val MYSHOPPAL_PREFERENCES: String = "MyShopPalPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    // Intent extra constants.
    const val EXTRA_USER_DETAILS: String = "extra_user_details"

    //A unique code for asking the Read Storage Permission using this we will be check and identify in the method onRequestPermissionsResult in the Base Activity.
    const val READ_STORAGE_PERMISSION_CODE = 2
    // A unique code of image selection from Phone Storage.
    const val PICK_IMAGE_REQUEST_CODE = 2

    // Constant variables for Gender
    // END
    const val MALE: String = "Male"
    const val FEMALE: String = "Female"

    // Firebase database field names
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    //constant variable for userProfile image
    const val USER_PROFILE_IMAGE:String = "User_Profile_Image"
    //constant variable for profile image

    const val IMAGE: String = "image"
    //constant database field for completedProfile
    const val COMPLETE_PROFILE: String = "profileCompleted"



    //request the intent to select the image using the unique code
    // a function for user profile image selection from phone storage
    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /*A function to get the image file extension of the selected image.
    * @param activity Activity reference.
    * @param uri Image file uri*/

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}