package dragau.o2o.customer.ui.activity.product

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil

import com.arellomobile.mvp.presenter.InjectPresenter
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dragau.o2o.customer.R
import dragau.o2o.customer.databinding.ActivityAddProductBinding
import dragau.o2o.customer.di.modules.GlideApp
import dragau.o2o.customer.presentation.presenter.product.AddProductPresenter
import dragau.o2o.customer.presentation.view.product.AddProductView
import dragau.o2o.customer.ui.activity.BaseActivity


const val ASPECT_RATIO_X = 1
const val ASPECT_RATIO_Y = 1
const val REQUESTED_WIDTH = 1000
const val REQUESTED_HEIGHT = 1000

class AddProductActivity : BaseActivity(), AddProductView {

    companion object {
        const val TAG = "AddProductActivity"
        fun getIntent(context: Context): Intent = Intent(context, AddProductActivity::class.java)
    }

    @InjectPresenter
    lateinit var mAddProductPresenter: AddProductPresenter

    lateinit var binding: ActivityAddProductBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product)
        binding.presenter = mAddProductPresenter
    }


    @SuppressLint("NewApi")
    override fun pickPhoto() {
        if (CropImage.isExplicitCameraPermissionRequired(this)) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE)
        } else {
            CropImage.startPickImageActivity(this)
        }
    }

    private fun startCropImageActivity(imageUri: Uri){
        CropImage
            .activity(imageUri)
            .setRequestedSize(REQUESTED_WIDTH, REQUESTED_HEIGHT, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
            .setAspectRatio(ASPECT_RATIO_X, ASPECT_RATIO_Y)
            .start(this)
    }

    override fun showLoadedImage(image: Bitmap) {
        GlideApp.with(this)
            .load(image)
            .fitCenter()
            .into(binding.photoImg)

        binding.photoImg.visibility = View.VISIBLE
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(this, data)

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE
                )
            } else {
                // no permissions required or already granted, can start crop image activity

                startCropImageActivity(imageUri)
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = CropImage.getActivityResult(data).uri
            val croppedBitmap = BitmapFactory.decodeFile(uri.encodedPath)
            mAddProductPresenter.uploadImage(croppedBitmap)
        }
    }
}
