package app.phhuang.training.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import app.phhuang.training.databinding.FragPlantDetailBinding
import com.bumptech.glide.Glide

class PlantDetailFragment : Fragment() {
    private lateinit var plantViewModel: PlantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        plantViewModel = activity?.run { ViewModelProviders.of(this).get(PlantViewModel::class.java) } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragPlantDetailBinding.inflate(inflater, container, false).apply {
            plant = plantViewModel
        }.root
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageUrl(imageView: ImageView, imgUrl: String?) {
            Glide.with(imageView).load(imgUrl).into(imageView)
        }
    }
}