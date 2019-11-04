package dragau.o2o.customer.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dragau.o2o.customer.R
import dragau.o2o.customer.models.enums.PagingState
import dragau.o2o.customer.models.objects.Product
import kotlinx.android.synthetic.main.item_list_footer.view.*

class ListFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: PagingState?, onItemClickListener: ProductsListAdapter.OnItemClickListener<Product>) {
        itemView.progress_bar.visibility = if (status == PagingState.LOADING) VISIBLE else View.INVISIBLE
        itemView.txt_error.visibility = if (status == PagingState.ERROR) VISIBLE else View.INVISIBLE
        itemView.setOnClickListener{
            onItemClickListener
        }
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_footer, parent, false)
            view.txt_error.setOnClickListener { retry() }
            return ListFooterViewHolder(view)
        }
    }
}