package dragau.o2o.customer.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.journeyapps.barcodescanner.ViewfinderView
import android.R
import android.graphics.Paint





class CustomViewfinderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): ViewfinderView(context, attrs){

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        val frame = framingRect
//        val paint1 = Paint()
//        paint1.setColor(context.resources.getColor(R.color.black))
//        val frameLength = (32 * resources.displayMetrics.density + 0.5f).toInt()
//        val frameWidth = (3 * resources.displayMetrics.density + 0.5f).toInt()
//        canvas!!.drawRect(
//            frame!!.left.toFloat(),
//            frame.top.toFloat(), (frame.left + frameLength).toFloat(), (frame.top + frameWidth).toFloat(), paint1)
//        canvas.run {
//            drawRect(frame.left.toFloat(),
//                frame.top.toFloat(), (frame.left + frameLength).toFloat(), (frame.top + frameWidth).toFloat(), paint1)
//            drawRect(frame.left.toFloat(),
//                frame.top.toFloat(), (frame.left + frameWidth).toFloat(), (frame.top + frameLength).toFloat(), paint1)
//            drawRect(
//                frame.left.toFloat(),
//                (frame.bottom - frameLength).toFloat(), (frame.left + frameWidth).toFloat(), frame.bottom.toFloat(), paint1)
//            drawRect(
//                frame.left.toFloat(),
//                (frame.bottom - frameWidth).toFloat(), (frame.left + frameLength).toFloat(), frame.bottom.toFloat(), paint1)
//            drawRect((frame.right - frameWidth).toFloat(),
//                (frame.bottom - frameLength).toFloat(), frame.right.toFloat(), frame.bottom.toFloat(), paint1)
//            drawRect((frame.right - frameLength).toFloat(),
//                (frame.bottom - frameWidth).toFloat(), frame.right.toFloat(), frame.bottom.toFloat(), paint1)
//            drawRect(
//                (frame.right - frameWidth).toFloat(),
//                frame.top.toFloat(), frame.right.toFloat(), (frame.top + frameLength).toFloat(), paint1)
//            drawRect((frame.right - frameLength).toFloat(),
//                frame.top.toFloat(), frame.right.toFloat(), (frame.top + frameWidth).toFloat(), paint1)
//        }
//        super.onDraw(canvas)
        paint.color = if (resultBitmap != null) resultColor else maskColor
        canvas!!.drawRect(0f, 0f, 20f, 20f, paint)
        canvas.drawRect(0f, 20f, 20f, 20f, paint)
        canvas.drawRect(20f, 20f, 20f, 20f, paint)
        canvas.drawRect(0f, 20f, 20f, 20f, paint)
    }
}