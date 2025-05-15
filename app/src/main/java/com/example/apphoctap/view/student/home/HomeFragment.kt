package com.example.apphoctap.view.student.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentDashboardStudentBinding
    import com.example.apphoctap.utils.UiState
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import com.example.apphoctap.utils.SessionManager
import com.example.apphoctap.view.classdetail.ClassDetailFragment
import com.example.apphoctap.view.student.StudentActivity

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentDashboardStudentBinding? = null
    private val binding get() = _binding!!
    private val viewModel : HomeViewModel by viewModels()
    private lateinit var adapter : NearbyClassAdapter


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
    private var studentID: String? = null
    private var userId: String? = null
    private var username: String? = null
    private var email: String? = null
    private var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(requireContext())
        val token = sessionManager.getAccessToken()

        // Lấy teacherID từ token
          studentID = sessionManager.getStudentID()
          username = token?.let { com.example.apphoctap.utils.JwtUtils.getUsernameFormToken(it) }

        // Lấy thông tin từ arguments nếu có
        arguments?.let {
            userId = it.getString("userID")
            email = it.getString("email")
            role = it.getString("role")
        }
    }
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
        
        binding.tvGreeting.text ="${binding.tvGreeting.text} $username"

        //Khởi tạo adapter cho banner
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


        adapter = NearbyClassAdapter(
            emptyList(),
            onClick = {item->
                viewModel.onClassClicked(item.classId)
                val myClassDetailFragment = ClassDetailFragment().apply {
                    arguments = bundleOf(
                        "classID" to item.classId,
                        "className" to item.className,
                        "teacherName" to item.teacherName,
                        "enrollmentKey" to item.enrollmentKey
                    )
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_container_student, myClassDetailFragment)
                    .addToBackStack(null)
                    .commit()

            }
        )

        binding.rvNearbyClasses.adapter = adapter
        binding.rvNearbyClasses.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        //Load nearby class
        viewModel.loadClasses()

        //quan sát dữ liệu
        observeViewModel()

        binding.tvSeeAllNearby.setOnClickListener({
            (activity as StudentActivity).loadAllClassFragment()
        })
    }

    //quan sát dữ liệu class
    private fun observeViewModel() {
        viewModel.classes.observe(viewLifecycleOwner){ resource ->
            when(resource){
                is UiState.Loading -> {
                    binding.shimmerContainer.visibility = View.VISIBLE
                    binding.rvNearbyClasses.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.shimmerContainer.visibility = View.GONE
                    //kiểm tra hiêển thị giao diện
                    resource.data.let { classes ->
                        if (classes.isEmpty()) {
                            binding.layoutEmptyCourses.visibility = View.VISIBLE
                            binding.rvNearbyClasses.visibility = View.GONE
                        } else {
                            binding.layoutEmptyCourses.visibility = View.GONE
                            binding.rvNearbyClasses.visibility = View.VISIBLE
                            adapter.updateList(classes)
                        }
                    }
                }
                is UiState.Error -> {
                    binding.shimmerContainer.visibility = View.GONE
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
