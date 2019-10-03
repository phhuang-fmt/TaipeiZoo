package app.phhuang.training.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.phhuang.training.R
import app.phhuang.training.model.ApiResult
import app.phhuang.training.model.Result
import app.phhuang.training.model.ResultX
import app.phhuang.training.model.TaipeiDataService
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private val rvAdapter = DataAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run { ViewModelProviders.of(this).get(MainViewModel::class.java) } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<RecyclerView>(R.id.rvList)?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
        }

        if (rvAdapter.itemCount == 0) {
            queryApi()
        }
    }

    private fun queryApi() {
        Retrofit.Builder().baseUrl("https://data.taipei/").addConverterFactory(GsonConverterFactory.create()).build()
            .create(TaipeiDataService::class.java).listArea().enqueue(object: Callback<ApiResult> {
                override fun onResponse(call: Call<ApiResult>, response: Response<ApiResult>) {
                    response.body()?.result?.let {
                        onApiResult(it)
                    }
                }

                override fun onFailure(call: Call<ApiResult>, t: Throwable) {}
            })
    }

    private fun onApiResult(result: Result) {
        val resultList = result.results
        viewModel.setAreaList(resultList)
        rvAdapter.setData(resultList) { index -> gotoDetailPage(resultList[index])}
    }

    private fun gotoDetailPage(areaInfo: ResultX) {
        val action = MainFragmentDirections.actionMainFragmentToAreaDetailFragment(areaId = areaInfo._id, title = areaInfo.E_Name)
        findNavController().navigate(action)
    }

    private data class AdapterData(val imgUrl: String, val name: String, val info: String, val memo: String, val actionListener: () -> Unit)

    private class AreaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivImage = view.findViewById<ImageView>(R.id.ivImage)
        private val tvName = view.findViewById<TextView>(R.id.tvName)
        private val tvInfo = view.findViewById<TextView>(R.id.tvInfo)
        private val tvMemo = view.findViewById<TextView>(R.id.tvMemo)

        fun bindData(data: AdapterData) {
            Glide.with(ivImage).load(data.imgUrl).into(ivImage)
            tvName.text = data.name
            tvInfo.text = data.info
            tvMemo.text = data.memo
            itemView.setOnClickListener { data.actionListener() }
        }
    }

    private class DataAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val dataList = mutableListOf<AdapterData>()

        fun setData(resultList: List<ResultX>, actionListener: (index: Int) -> Unit) {
            val newDataList = mutableListOf<AdapterData>()

            resultList.forEachIndexed { index, result ->
                newDataList.add(AdapterData(result.E_Pic_URL, result.E_Name, result.E_Info, result.E_Memo) {
                    actionListener(index)
                })
            }

            this.dataList.clear()
            this.dataList.addAll(newDataList)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
                = AreaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_area, parent, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = (holder as AreaViewHolder).bindData(dataList[position])

        override fun getItemCount(): Int = dataList.size
    }
}
