package kz.dragau.larek.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dragau.o2o.customer.R
import dragau.o2o.customer.models.enums.ParameterType
import dragau.o2o.customer.models.objects.BaseParameter

import java.util.ArrayList
import java.util.Objects.compare

class RecyclerBindingAdapter<T>(
    private val holderLayout: Int,
    private val variableId: Int,
    private val context: Context
) : RecyclerView.Adapter<RecyclerBindingAdapter.BindingHolder>() {
    private var items: ArrayList<T> = ArrayList()
    private var onItemClickListener: OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerBindingAdapter.BindingHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return BindingHolder(v)
    }

    init {
        //items.addOnListChangedCallback(ObservableListCallback())
    }

    override fun onBindViewHolder(holder: RecyclerBindingAdapter.BindingHolder, position: Int) {
        val item = items[position]

        holder.binding!!.root.setOnClickListener { v ->
            if (onItemClickListener != null)
                onItemClickListener!!.onItemClick(position, item)
        }
        holder.binding.setVariable(variableId, item)
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        if (holderLayout == R.layout.product_parameter_item && item is BaseParameter)
        {
            when (item.type)
            {
                ParameterType.DECIMAL -> return R.layout.parameter_doube_item
                ParameterType.DATE -> return R.layout.parameter_date_item
                ParameterType.BOOL -> return R.layout.parameter_bool_item
                ParameterType.INT -> return R.layout.parameter_int_item
                ParameterType.BARCODE -> return R.layout.parameter_barcode_item
                ParameterType.FOOTER -> return  R.layout.parameter_footer_item
            }
        }
        return holderLayout
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener<T> {
        fun onItemClick(position: Int, item: T)
    }

    class BindingHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(v)

        fun bind(varId: Int, obj: Any) {
            binding?.setVariable(varId, obj)
        }
    }

    fun setItems(items: ObservableArrayList<T>?)
    {
        if (this.items.isEmpty() && items != null)
        {
            val list = ArrayList<T>()
            list.addAll(items.toMutableList())
            this.items = list

            notifyDataSetChanged()
        }
        else if (this.items.isNotEmpty() && (items == null || items.isEmpty()))
        {
            this.items.clear()
            notifyDataSetChanged()
        }
        else {
            if (items != null) {
                val diffResult = calculateDiff(this.items, items)
                this.items.clear()
                this.items.addAll(items)
                diffResult.dispatchUpdatesTo(this)
                notifyDataSetChanged() //под вопросом, может стоит удалить
            }
        }

    }

    fun calculateDiff(oldList: ArrayList<T>, newList: ArrayList<T>): DiffUtil.DiffResult {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            {
                val old = oldList[oldItemPosition]
                val new = newList[newItemPosition]
                /*if (old is Game)
                {
                    return (old as? Game)?.id == (new as? Game)?.id
                }*/
                return old == new
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val old = oldList[oldItemPosition]
                val new = newList[newItemPosition]
                /*if (old is Game)
                {
                    return (old as? Game)?.id == (new as? Game)?.id && (old as? Game)?.hashkey == (new as? Game)?.hashkey
                            && (old as? Game)?.isPlayerTurn == (new as? Game)?.isPlayerTurn
                            && (old as? Game)?.currentRound?.stage == (new as? Game)?.currentRound?.stage
                            && (new as? Game)?.partner_mission_id == null
                }
                else
                {
                    return old == new
                }*/
                return old == new
            }

            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size
        })

        return diff
    }


    private inner class ObservableListCallback :
        ObservableList.OnListChangedCallback<ObservableArrayList<T>>() {

        override fun onChanged(sender: ObservableArrayList<T>) {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(sender: ObservableArrayList<T>, positionStart: Int, itemCount: Int) {
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(sender: ObservableArrayList<T>, positionStart: Int, itemCount: Int) {
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeMoved(
            sender: ObservableArrayList<T>,
            fromPosition: Int,
            toPosition: Int,
            itemCount: Int
        ) {
            notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(sender: ObservableArrayList<T>, positionStart: Int, itemCount: Int) {
            notifyItemRangeRemoved(positionStart, itemCount)
        }
    }
}
