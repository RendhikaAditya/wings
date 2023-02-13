package co.dh.wings.view.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.dh.wings.databinding.RowCheckoutBinding
import co.dh.wings.network.response.Transactions
import co.dh.wings.util.helper
import kotlin.collections.ArrayList

class CheckoutAdapter(
    var datas: ArrayList<Transactions>,
    var listener: OnAdapterListener
) : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RowCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    @Override
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = datas[position]
        val helpers = helper()

        holder.binding.namaProduk.setText(model.name)
        holder.binding.jmlItem.setText("${model.quantity}")
        holder.binding.harga.setText(helpers.formatRupiah(model.price.toDouble()*model.quantity.toDouble()))
        holder.binding.satuan.setText(model.unit)

        var jml = 1

        holder.binding.btnKurang.setOnClickListener {
            jml = holder.binding.jmlItem.text.toString().toInt()-1
            listener.onTextChange(jml.toString(), model)
        }

        holder.binding.btnTambah.setOnClickListener {
            jml = holder.binding.jmlItem.text.toString().toInt() + 1
            listener.onTextChange(jml.toString(), model)
        }



    }

    override fun getItemCount() = datas.size

    class ViewHolder(val binding: RowCheckoutBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnAdapterListener {
        fun onClick(result: Transactions)
        fun onTextChange(mlItem: String, model: Transactions)
    }

    fun setData(data: List<Transactions>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }
}