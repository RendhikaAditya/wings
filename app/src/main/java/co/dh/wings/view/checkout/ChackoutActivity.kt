package co.dh.wings.view.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.dh.wings.databinding.ActivityChackoutBinding
import co.dh.wings.network.ApiService
import co.dh.wings.network.Repository
import co.dh.wings.network.response.Transactions
import co.dh.wings.util.SharedPreferences
import co.dh.wings.util.helper
import co.dh.wings.view.home.MainViewModel
import co.dh.wings.view.home.MainViewModelFactory
import co.dh.wings.view.home.TransactionsViewModel
import co.id.tesmandiritmdb.network.Resource

class ChackoutActivity : AppCompatActivity() {
    lateinit var binding: ActivityChackoutBinding
    private lateinit var transactionsViewModel: TransactionsViewModel
    private lateinit var adapter: CheckoutAdapter
    private lateinit var preferences: SharedPreferences
    var helpers : helper = helper()
    var totalHarga = 0.0
    var documenNumber = "001"



    private val api by lazy { ApiService.getClient() }
    private lateinit var viewModel: CheckoutViewModel
    private lateinit var repository: Repository
    private lateinit var viewModelFactory: CheckoutViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChackoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SharedPreferences(this)

        setupViewModel()
        setupObserver()
        setupListener()
    }

    private fun setupListener() {
        adapter = CheckoutAdapter(
            arrayListOf(),
            object : CheckoutAdapter.OnAdapterListener {
                override fun onClick(result: Transactions) {
                }

                override fun onTextChange(jmlItem: String, model: Transactions) {
                    if (jmlItem.equals("0")){
                        transactionsViewModel.delete(model)
                    }else {
                        transactionsViewModel.updateTransactionByProductCode(
                            jmlItem.toInt(),
                            model.product_code
                        )
                    }
                }


            }
        )
        adapter.notifyDataSetChanged()
        binding.rcChackout.adapter = adapter
        binding.btnConfirm.setOnClickListener {
            if (totalHarga.equals(0.0)){
                Toast.makeText(this, "Keranjang kosong!", Toast.LENGTH_SHORT).show()
            }else {
                viewModel.fetchTransaction(
                    "TRX",
                    "1",
                    "${preferences.getString("name")}",
                    totalHarga.toInt()
                )
            }
        }
    }

    private fun setupObserver() {
        transactionsViewModel.transactions.observe(this, Observer {
            Log.d("TAG", "setupObserver:: $it ")
            totalHarga = 0.0
            repeat(it.size){i->
                totalHarga = totalHarga+(it.get(i).price.toDouble()*it.get(i).quantity.toDouble())
            }
            binding.hargaTotal.setText("${helpers.formatRupiah(totalHarga)}")
            adapter.setData(it)
        })

        viewModel.base.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.d("Transaction Header", " :: Loading")
                }
                is Resource.Success -> {
                    Log.d("Transaction Header", " :: response :: ${it.data!!}")
                    if (it.data.kode){
                        documenNumber = it.data.number
                        transactionsViewModel.transactions.observe(this, Observer { data->
                            data.forEach {
                                Log.d("TAG", "setupObserver:: ulang $it")
                                viewModel.fetchTransactionDetail(
                                    "TRX",
                                    "$documenNumber",
                                    "${it.product_code}",
                                    "${it.price}",
                                    "${it.quantity}",
                                    "${it.unit}",
                                    "${it.sub_total}",
                                    "${it.currency}"
                                )
                                transactionsViewModel.delete(it)
                            }
                            finish()
                        })
                    }
                }
                is Resource.Error -> {
                    Log.d("Transaction Header", " :: Error ")

                }
            }
        })

        viewModel.baseDetail.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.d("Transaction Detail", " :: Loading")
                }
                is Resource.Success -> {
                    Log.d("Transaction Detail", " :: response :: ${it.data!!}")

                }
                is Resource.Error -> {
                    Log.d("Transaction Detail", " :: Error ")

                }
            }
        })


    }

    private fun setupViewModel() {
        transactionsViewModel = ViewModelProvider(this).get(TransactionsViewModel::class.java)
        repository = Repository(api)
        viewModelFactory = CheckoutViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CheckoutViewModel::class.java)
    }
}