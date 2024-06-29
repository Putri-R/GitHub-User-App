package com.myapp.githubuser.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapp.githubuser.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private lateinit var followViewModel: FollowViewModel
    private var position: Int = 0
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followViewModel = ViewModelProvider(this).get(FollowViewModel::class.java)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME) ?: ""
        }

        val adapter = UserAdapter()
        binding.rvFollowAccounts.adapter = adapter

        binding.rvFollowAccounts.layoutManager = LinearLayoutManager(requireContext())

        if (position == 1) {
            followViewModel.findAccountFollowers(username)
            followViewModel.accountFollowers.observe(viewLifecycleOwner) { followers ->
                adapter.submitList(followers)
            }
        } else {
            followViewModel.findAccountFollowing(username)
            followViewModel.accountFollowing.observe(viewLifecycleOwner) { following ->
                adapter.submitList(following)
            }
        }

        followViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}