package com.example.apphoctap.view.student.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentDashboardStudentBinding
import com.example.apphoctap.utils.UiState
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentDashboardStudentBinding? = null
    private val binding get() = _binding!!
    private val viewModel : HomeViewModel by viewModels()

    private lateinit var bannerAdapter: BannerAdapter
    private val bannerImages = listOf(
        R.drawable.banner,
        R.drawable.banner2,
        R.drawable.banner3,
        R.drawable.banner5
    )

    private var currentPage = 0
    private val scrollDelay = 4000L
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var bannerRunnable: Runnable
    private var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardStudentBinding.inflate(inflater, container, false)
        val root : View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bannerAdapter = BannerAdapter(bannerImages)
        binding.promotionSlider.adapter = bannerAdapter

        TabLayoutMediator(binding.sliderIndicator, binding.promotionSlider) { _, _ -> }.attach()

        binding.promotionSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                isScrolling = state != ViewPager2.SCROLL_STATE_IDLE
            }
        })

        bannerRunnable = object : Runnable {
            override fun run() {
                if (!isScrolling && bannerImages.isNotEmpty()) {
                    val nextPage = (currentPage + 1) % bannerImages.size
                    binding.promotionSlider.setCurrentItem(nextPage, true)
                }
                handler.postDelayed(this, scrollDelay)
            }
        }

        handler.postDelayed(bannerRunnable, scrollDelay)


        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.classes.observe(viewLifecycleOwner){ resource ->
            when(resource){
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvNearbyClasses.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvNearbyClasses.visibility = View.VISIBLE
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvNearbyClasses.visibility = View.GONE
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(bannerRunnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(bannerRunnable, scrollDelay)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
