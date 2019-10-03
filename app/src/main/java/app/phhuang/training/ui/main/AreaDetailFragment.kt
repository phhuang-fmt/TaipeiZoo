package app.phhuang.training.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.phhuang.training.R
import app.phhuang.training.model.*
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AreaDetailFragment : Fragment() {
    private val args: AreaDetailFragmentArgs by navArgs()
    private lateinit var viewModel: MainViewModel
    private val rvAdapter = DataAdapter()

    private lateinit var plantViewModel: PlantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run { ViewModelProviders.of(this).get(MainViewModel::class.java) } ?: throw Exception("Invalid Activity")
        viewModel.areaList.observe(this, Observer { areaList ->
            val area = areaList.find { it._id == args.areaId } ?: return@Observer
            rvAdapter.setBanner(area) { id -> if (id == R.id.tvLink) startBrowserActivity(area.E_URL) }
        })

        plantViewModel = activity?.run { ViewModelProviders.of(this).get(PlantViewModel::class.java) } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.area_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)

        if (rvAdapter.itemCount == 0) {
            queryApi()
        }
    }

    private fun initViews(view: View) {
        view.findViewById<RecyclerView>(R.id.rvList).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
            setHasFixedSize(true)
        }
    }

    private fun queryApi() {
        Retrofit.Builder().baseUrl("https://data.taipei/").addConverterFactory(GsonConverterFactory.create()).build()
            .create(TaipeiDataService::class.java).listPlant().enqueue(object: Callback<ApiPlantResult> {
                override fun onResponse(call: Call<ApiPlantResult>, response: Response<ApiPlantResult>) {
                    response.body()?.result?.let {
                        onApiResult(it)
                    }
                }

                override fun onFailure(call: Call<ApiPlantResult>, t: Throwable) {}
            })
    }

    private fun onApiResult(result: ResultXX) {
        val resultList = result.results

        rvAdapter.setData(resultList) { index ->
            val selectedResult = resultList[index]
            gotoPlantPage(selectedResult._id, selectedResult.F_Name_Ch ?: "")

            plantViewModel.imgUrl.value = selectedResult.F_Pic01_URL
            plantViewModel.name.value = selectedResult.F_Name_Ch
            plantViewModel.nameLatin.value = selectedResult.F_Name_Latin
            plantViewModel.alsoKnown.value = selectedResult.F_AlsoKnown
            plantViewModel.brief.value = selectedResult.F_Brief
            plantViewModel.feature.value = selectedResult.F_Feature
            plantViewModel.function.value = selectedResult.F_Function_Application
            plantViewModel.update.value = selectedResult.F_Update
        }
    }

    private fun startBrowserActivity(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun gotoPlantPage(plantId: Int, title: String) {
        val action = AreaDetailFragmentDirections.actionAreaDetailFragmentToPlantDetailFragment(plantId = plantId, title = title)
        findNavController().navigate(action)
    }

    private data class Data(
        val viewType: Int = VIEW_TYPE_ITEM,
        val imgUrl: String = "",
        val name: String = "",
        val info: String = "",
        val memo: String = "",
        val category: String = "",
        val linkUrl: String = "",
        val index: Int = 0,
        val actionListener: (id: Int) -> Unit = {}
    )

    private class DataAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val dataList = mutableListOf<Data>()

        fun setBanner(areaInfo: ResultX, actionListener: (id: Int) -> Unit) {
            dataList.clear()
            dataList.add(Data(viewType = VIEW_TYPE_BANNER, imgUrl = areaInfo.E_Pic_URL, info = areaInfo.E_Info, memo = areaInfo.E_Memo, category = areaInfo.E_Category, linkUrl = areaInfo.E_URL, actionListener = actionListener))
            dataList.add(Data(viewType = VIEW_TYPE_DIVIDER))

            dataList.add(Data(viewType = VIEW_TYPE_LOADING))

            notifyDataSetChanged()
        }

        fun setData(plantList: List<ResultXXX>, actionListener: (index: Int) -> Unit) {
            val loadingData = dataList.find { it.viewType == VIEW_TYPE_LOADING }
            loadingData?.let {
                val position = dataList.indexOf(it)
                dataList.remove(it)
                notifyItemRemoved(position)
            }

            val newDataList = mutableListOf<Data>()
            newDataList.add(Data(viewType = VIEW_TYPE_TITLE))
            plantList.forEachIndexed { index, plant ->
                newDataList.add(
                    Data(viewType = VIEW_TYPE_ITEM, imgUrl = plant.F_Pic01_URL ?: "", name = plant.F_Name_Ch ?: "", info = plant.F_AlsoKnown ?: "") {
                        actionListener(index)
                    }
                )
            }
            newDataList.add(Data(viewType = VIEW_TYPE_DIVIDER))

            dataList.addAll(newDataList)
            notifyItemRangeInserted(dataList.size - newDataList.size, newDataList.size)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                VIEW_TYPE_BANNER -> BannerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_banner, parent, false))
                VIEW_TYPE_DIVIDER -> object: RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_divider, parent, false)) {}
                VIEW_TYPE_TITLE -> object: RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_title, parent, false)) {}
                VIEW_TYPE_ITEM -> PlantViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_area, parent, false))
                VIEW_TYPE_LOADING -> object: RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)) {}
                else -> object: RecyclerView.ViewHolder(View(parent.context)) {}
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is BannerViewHolder -> holder.bindData(dataList[position])
                is PlantViewHolder -> holder.bindData(dataList[position])
                else -> {}
            }
        }

        override fun getItemViewType(position: Int): Int = dataList[position].viewType
        override fun getItemCount(): Int = dataList.size

        private class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val ivImage = view.findViewById<ImageView>(R.id.ivImage)
            private val tvInfo = view.findViewById<TextView>(R.id.tvInfo)
            private val tvMemo = view.findViewById<TextView>(R.id.tvMemo)
            private val tvCategory = view.findViewById<TextView>(R.id.tvCategory)
            private val tvLink = view.findViewById<TextView>(R.id.tvLink)

            fun bindData(data: Data) {
                Glide.with(ivImage).load(data.imgUrl).into(ivImage)
                tvInfo.text = data.info
                tvMemo.text = data.memo
                tvCategory.text = data.category
                tvLink.setOnClickListener { data.actionListener(it.id) }
            }
        }

        private class PlantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val ivImage = view.findViewById<ImageView>(R.id.ivImage)
            private val tvName = view.findViewById<TextView>(R.id.tvName)
            private val tvInfo = view.findViewById<TextView>(R.id.tvInfo)

            fun bindData(data: Data) {
                Glide.with(ivImage).load(data.imgUrl).into(ivImage)
                tvName.text = data.name
                tvInfo.text = data.info
                itemView.setOnClickListener { data.actionListener(it.id) }
            }
        }
    }

    companion object {
        const val VIEW_TYPE_BANNER = 1
        const val VIEW_TYPE_TITLE = 2
        const val VIEW_TYPE_ITEM = 3
        const val VIEW_TYPE_DIVIDER = 4
        const val VIEW_TYPE_LOADING = 5
    }
}