package dragau.o2o.customer.models.shared

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target
import java.util.*
import android.animation.AnimatorListenerAdapter
import android.animation.Animator
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import dragau.o2o.customer.App
import dragau.o2o.customer.R
import com.theartofdev.edmodo.cropper.CropImageView
import dragau.o2o.customer.di.modules.GlideApp
import dragau.o2o.customer.extensions.shortDateDiff
import dragau.o2o.customer.extensions.shortSecDiff


object Utils {
    var screenWidth = 600
    var screenHeight = 600
    var animationDuration = 500L

    init {
        val wm: WindowManager = App.appComponent.context().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display : Display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        screenHeight = size.y

    }

    @JvmStatic
    @BindingAdapter("uri")
    fun setUri(view: CropImageView, uri: Uri?)
    {
        if (uri == null)
        {
            return
        }

        view.setImageUriAsync(uri)
    }

    @JvmStatic
    @BindingAdapter("secDiff")
    fun formatSecDiff(view: View, date: Date?) {
        val textView: TextView = view as TextView
        if (date != null) {
            textView.text = date.shortSecDiff()
        }
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        if (url == null)
        {
            return
        }
        GlideApp.with(view.context)
            .load(url)
            .override(screenWidth, Target.SIZE_ORIGINAL)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(700))
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("RawImageUrl")
    fun loadRawImage(view: ImageView, url: String?) {
        if (url == null)
        {
            return
        }

        GlideApp.with(view.context)
            .load(url)
            .centerCrop()
            .into(view)
    }


    @JvmStatic
    @BindingAdapter("visibilityFade")
    fun bindFadeVisibility(view: View, visible: Boolean) {
        if (view.visibility == VISIBLE && visible
            || view.visibility != VISIBLE && !visible) {
            return
        }

        if (visible) viewFadeVisibleAnimator(view) else viewFadeGoneAnimator(view)
    }

    private fun viewFadeGoneAnimator(view: View) {
        view.alpha = 1f
        view.animate()
            .alpha(0f)
            .setDuration(animationDuration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.clearAnimation()
                    view.visibility = View.GONE
                }
            })

    }
    private fun viewFadeVisibleAnimator(view: View) {
        view.alpha = 0f
        view.visibility = VISIBLE

        view.animate()
            .alpha(1f)
            .setDuration(animationDuration)
            .setListener(null)
    }

    @JvmStatic
    @BindingAdapter("visibleTransition")
    fun bindVisibleVisibility(view: View, visible: Boolean) {
        if (view.visibility == VISIBLE && visible
            || view.visibility != VISIBLE && !visible) {
            return
        }

        if (visible) viewVisibleAnimator(view) else viewGoneAnimator(view)
    }

    @JvmStatic
    @BindingAdapter("invisible")
    fun bindInvisibleVisibility(view: View, invisible: Boolean) {
        view.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
    }

    private fun viewGoneAnimator(view: View) {
        view.alpha = 1f

        view.animate()
            .translationY(view.height.toFloat())
            .alpha(0f)
            .setDuration(animationDuration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.clearAnimation()
                    view.visibility = View.GONE
                }
            })

    }
    private fun viewVisibleAnimator(view: View) {
        view.alpha = 0f
        view.visibility = VISIBLE
        view.translationY = view.height.toFloat()

        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(animationDuration)
            .setListener(null)
    }


    fun colorIntFromAttribute(attrId: Int, context: Context): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attrId, typedValue, true)
        return typedValue.data
    }

    fun getMonthFromResource(month: Int?, context: Context): String{
        return when(month) {
            1 -> context.getString(R.string.january)
            2 -> context.getString(R.string.february)
            3 -> context.getString(R.string.march)
            4 -> context.getString(R.string.april)
            5 -> context.getString(R.string.may)
            6 -> context.getString(R.string.june)
            7 -> context.getString(R.string.jule)
            8 -> context.getString(R.string.august)
            9 -> context.getString(R.string.september)
            10 -> context.getString(R.string.october)
            11 -> context.getString(R.string.november)
            else -> context.getString(R.string.december)
        }
    }

    fun getMonthFromResource(month: String?, context: Context): String? {
        month?:let {
            return context.getString(R.string.month)
        }

        return try {
            getMonthFromResource(month = month?.toInt(), context = context)
        }catch (ex: NumberFormatException){
            month
        }
    }

    /*@JvmStatic
    @BindingAdapter("entries", "layout")
    fun <T> setEntries(
        viewGroup: ViewGroup,
        entries: List<T>?, layoutId: Int
    ) {
        viewGroup.removeAllViews()
        if (entries != null) {
            val inflater = viewGroup.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            for (i in entries.indices) {
                val entry = entries[i]
                val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, viewGroup, true)
                binding.setVariable(BR.data, entry)
            }
        }
    }*/

    @JvmStatic
    @BindingAdapter("onEditorEnterAction")
    fun EditText.onEditorEnterAction(f: Function1<String, Unit>?) {

        if (f == null) setOnEditorActionListener(null)
        else setOnEditorActionListener { v, actionId, event ->

            val imeAction = when (actionId) {
                EditorInfo.IME_ACTION_DONE,
                EditorInfo.IME_ACTION_SEND,
                EditorInfo.IME_ACTION_GO -> true
                else -> false
            }

            val keydownEvent = event?.keyCode == KeyEvent.KEYCODE_ENTER
                    && event.action == KeyEvent.ACTION_DOWN

            if (imeAction or keydownEvent)
                true.also  {
                f(v.editableText.toString())
                val inputManager: InputMethodManager =  context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.toggleSoftInput(0, 0)
            }
            else false
        }
    }

    @JvmStatic
    @BindingAdapter("shortDateDiff")
    fun formatShortDateDiff(view: View, date: Date?) {
        val textView: TextView = view as TextView
        if (date != null) {
            textView.text = date.shortDateDiff()
        }
    }

    /*@JvmStatic
    @BindingAdapter("showPhones")
    fun showPhones(text: TextView, list: List<String>){
        val phones = StringBuilder()
        list.forEach{
            phones.append(it).append("\n\n")
        }
        text.text = phones.trim()
        text.autoLinkMask = Linkify.PHONE_NUMBERS
        text.linksClickable = true
        text.movementMethod = LinkMovementMethod.getInstance()
        text.paint.isUnderlineText = false
        text.setLinkTextColor(ContextCompat.getColor(text.context, R.color.blueTextColor))
    }*/

    @JvmStatic
    @BindingAdapter("openLink")
    fun openLink(view: View, link: String?){
        if (link.isNullOrEmpty()) {

            return
        }
        val clickListener = View.OnClickListener {
            val openLinkIntent = Intent(Intent.ACTION_VIEW)
            openLinkIntent.data = Uri.parse(link)
            try {
                view.context.startActivity(openLinkIntent)
            } catch (ex: ActivityNotFoundException) {
                openLinkIntent.data = Uri.parse("http://$link")
                view.context.startActivity(openLinkIntent)
            }
        }
        view.setOnClickListener(clickListener)
    }

    @JvmStatic
    @BindingAdapter("widthConstraintPercent")
    fun setWidthConstraintPercent(guideline: Guideline, value: Int) {
        val params: ConstraintLayout.LayoutParams  = guideline.layoutParams as ConstraintLayout.LayoutParams
        params.guidePercent = value / 1000f
        guideline.layoutParams = params
    }
}