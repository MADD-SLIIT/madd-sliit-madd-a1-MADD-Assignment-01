package com.example.usedmart.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.usedmart.Helper.ChangeNumberItemsListener
import com.example.usedmart.Helper.ManagementCart
import com.example.usedmart.Model.ItemsModel
import com.example.usedmart.databinding.ViewholderCartBinding

class CartAdapter(
    private val listItemsSelected: ArrayList<ItemsModel>, context: Context,
    var changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderCartBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val binding =
            ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    private val managementCart = ManagementCart(context)
    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val item = listItemsSelected[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachitem.text = "$${item.price}"
        holder.binding.totalEachitem.text = "$${Math.round(item.numberInCart * item.price)}"
        holder.binding.numberitemTxt.text = item.numberInCart.toString()

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.pic)

        holder.binding.plusCartBtn.setOnClickListener {
            managementCart.plusItem(
                listItemsSelected,
                position,
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                })
        }

        holder.binding.minusCartBtn.setOnClickListener {
            managementCart.minusItem(
                listItemsSelected,
                position,
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                })
        }
    }

    override fun getItemCount(): Int = listItemsSelected.size


}