package com.example.apphoctap.view.classdetail.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apphoctap.databinding.FragmentMaterialBinding
import com.example.apphoctap.model.ClassMaterial
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MaterialsFragment: Fragment() {

    private var _binding : FragmentMaterialBinding? = null
    private val binding get() = _binding!!
    private lateinit var materialAdapter: MaterialsAdapter
    private val viewModel : MaterialsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaterialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val classID = arguments?.getString("classID").toString()  // Gán classID

        viewModel.getMaterials(classID)
        materialAdapter = MaterialsAdapter(
            emptyList(),
            onClickListener = {
                downloadMaterial(it)
            }
        )
        binding.rvMaterial.adapter = materialAdapter
        binding.rvMaterial.layoutManager = LinearLayoutManager(requireContext())
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.material.observe(viewLifecycleOwner) {
            materialAdapter.updateList(it)
        }
    }

    private fun downloadMaterial(it: ClassMaterial) {

    }

}