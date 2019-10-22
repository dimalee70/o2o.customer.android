package dragau.o2o.customer.models.objects

import com.google.zxing.BarcodeFormat

data class ProductBarcode (
    val barcode: String,
    val barcodeFormat: BarcodeFormat
)
