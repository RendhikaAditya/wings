package co.dh.wings.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.dh.wings.databinding.RowProductBinding
import co.dh.wings.network.response.ProductResponse
import co.dh.wings.util.helper
import kotlin.collections.ArrayList

class ProductAdapter(
    var datas: ArrayList<ProductResponse.Data>,
    var listener: OnAdapterListener
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RowProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    @Override
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = datas[position]
        val helpers = helper()

        holder.binding.namaProduk.setText(model.product_name)
        if (model.discount>0){
            holder.binding.diskonHarga.visibility = View.VISIBLE
            helpers.addStrikethrough(holder.binding.diskonHarga, helpers.formatRupiah(model.price!!.toDouble()))
            holder.binding.harga.setText(helpers.formatRupiah(helpers.calculateDiscount(model.price!!.toDouble(), model.discount.toDouble())))
        }else{
            holder.binding.diskonHarga.visibility = View.GONE
            holder.binding.harga.setText(helpers.formatRupiah(model.price!!.toDouble()))
        }


        holder.binding.btnBuy.setOnClickListener { listener.onClick(model) }
        holder.itemView.setOnClickListener { listener.onClickDetail(model) }

    }

    override fun getItemCount() = datas.size

    class ViewHolder(val binding: RowProductBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnAdapterListener {
        fun onClick(result: ProductResponse.Data)
        fun onClickDetail(result: ProductResponse.Data)
    }

    fun setData(data: List<ProductResponse.Data>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }
}