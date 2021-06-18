package com.example.gallerylitepro.Activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.example.gallerylitepro.R
import com.example.gallerylitepro.Utils.Utils
import com.example.gallerylitepro.databinding.ActivityFrameBinding
import com.lyrebirdstudio.croppylib.Croppy
import com.lyrebirdstudio.croppylib.main.CropRequest
import com.lyrebirdstudio.croppylib.main.CroppyTheme
import com.lyrebirdstudio.croppylib.util.file.FileCreator
import com.lyrebirdstudio.croppylib.util.file.FileOperationRequest

class FrameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFrameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_frame)

        startCroppy()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_CROP_IMAGE) {
            data?.data?.let {
//                Log.e("TEST", it.toString())
                Utils.mEditedURI=it;
                Utils.IsFramed=true;
//                binding.imageViewCropped.setImageURI(it)
                Utils.CaptureImage(it,applicationContext)
            }
        }
        else{
            Utils.IsFramed=false;
        }
        val `in` = Intent(applicationContext, EditingActivity::class.java)
        startActivity(`in`)
        finish()
    }

    override fun onBackPressed() {

        Utils.IsFramed=false;
        val `in` = Intent(applicationContext, EditingActivity::class.java)
        startActivity(`in`)
        finish()

    }

    fun startCroppy() {

        val destinationUri =
                FileCreator
                        .createFile(FileOperationRequest.createRandom(), application.applicationContext)
                        .toUri()

        val themeCropRequest = CropRequest.Manual(
                sourceUri = Utils.mEditedURI,
                destinationUri = destinationUri,
                requestCode = RC_CROP_IMAGE,
                croppyTheme = CroppyTheme(R.color.blue)
        )

        Croppy.start(this, themeCropRequest)
    }
    
    companion object {
        private const val RC_CROP_IMAGE = 102

    }

}