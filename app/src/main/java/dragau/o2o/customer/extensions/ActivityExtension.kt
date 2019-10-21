package dragau.o2o.customer.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dragau.o2o.customer.presentation.presenter.dialogs.ErrorDialogPresenter
import dragau.o2o.customer.presentation.presenter.dialogs.ProductExistDialogPresenter
import dragau.o2o.customer.presentation.presenter.dialogs.ProgressDialogPresenter
import dragau.o2o.customer.ui.activity.product.ScanActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.lang.Exception

inline fun AppCompatActivity.showProgressAlertDialog(func: ProgressDialogPresenter.() -> Unit): AlertDialog =
    ProgressDialogPresenter(this).apply {
        func()
    }.create(null)

//inline fun AppCompatActivity.showConfirmAletDialog(func: ConfirmDialogPresenter.() -> Unit, title: String?, message: String?): AlertDialog =
//    ConfirmDialogPresenter(this, title, message).apply{
//    func()
//}.create(null)
inline fun AppCompatActivity.showErrorAlertDialog(func: ErrorDialogPresenter.() -> Unit, title: String?, message: String?): AlertDialog =
    ErrorDialogPresenter(this, title, message).apply {
        func()
    }.create(null)

fun Uri.encodeImage(): String? {
//    val imageStream = context.contentResolver.openInputStream(this)
//    val imageBitmap = BitmapFactory.decodeStream(imageStream)
//    val baos = ByteArrayOutputStream()
//    Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
//    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//    val b = baos.toByteArray()
//    return Base64.encodeToString(b, Base64.DEFAULT)

    val imageFile = File(this.path)
    try {
        val fis = FileInputStream(imageFile)
        var bm = BitmapFactory.decodeStream(fis)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }catch (ex: Exception){
        ex.printStackTrace()
    }
    return null
}

inline fun ScanActivity.showExistsAlertDialog(func: ProductExistDialogPresenter.() -> Unit): AlertDialog? =
    ProductExistDialogPresenter(this).apply{
        func()
    }.create(null)