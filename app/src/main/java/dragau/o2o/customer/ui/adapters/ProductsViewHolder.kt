package dragau.o2o.customer.ui.adapters

import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dragau.o2o.customer.R
import dragau.o2o.customer.models.objects.Product
import kotlinx.android.synthetic.main.item_product.view.*

class ProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(product: Product?, onItemClickListener: ProductsListAdapter.OnItemClickListener<Product>) {
        if (product != null) {
            itemView.productNameTv.text = product.name
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(product)
//                println("Hello")
            }

//            if(product.productThumbnails?.peek()?.body.isNullOrEmpty()) {
//                Glide.with(context).clear(this)
//                this.setImageDrawable(null)
////        this.setImageDrawable(resources.getDrawable(R.drawable.progress_animation, context.theme))
////        this.setImageDrawable(resources.getDrawable(R.drawable.ic_groceries, context.theme))
//                return
//            }

//    if (){

            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform()

//        val circularProgressDrawable = CircularProgressDrawable(context)
//        circularProgressDrawable.strokeWidth = 5f
//        circularProgressDrawable.centerRadius = 30f
//        circularProgressDrawable.start()
//
//        val thumbnailRequest = Glide.with(this)
//            .load("https://picsum.photos/50/50?image=0")
            val imageAsBytes = Base64.decode(product.productThumbnails?.peek()?.body?.toByteArray(), Base64.DEFAULT)
            Glide.with(itemView.context)
                .load(imageAsBytes)
                .apply(options)
//            .placeholder(circularProgressDrawable)
                .into(itemView.productIconIv)
//            Glide.with(itemView.context)
//                .load(product.productThumbnails?.peek())
//                .into(itemView.productIconIv)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ProductsViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product, parent, false)
            return ProductsViewHolder(view)
        }
    }
}