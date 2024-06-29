package com.myapp.githubuser.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.myapp.githubuser.R
import com.myapp.githubuser.data.response.DetailUserResponse
import com.myapp.githubuser.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailViewModel: UserDetailViewModel

    companion object {
        const val DETAIL_USER = "detail_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.account_followers_tab,
            R.string.account_following_tab
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(this).get(UserDetailViewModel::class.java)

        val username = intent.getStringExtra(DETAIL_USER)

        if (username != null) {
            detailViewModel.searchUserDetail(username)
        }
        detailViewModel.accountDetail.observe(this) {
            setAccountData(it)
        }
        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username.toString()
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.view?.setBackgroundColor(ContextCompat.getColor(this@DetailUserActivity, R.color.dark_blue))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.view?.setBackgroundColor(ContextCompat.getColor(this@DetailUserActivity, R.color.blue))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        val followersTabLayout = tabs.getTabAt(0)
        followersTabLayout?.select()
    }

    @SuppressLint("SetTextI18n")
    private fun setAccountData(account: DetailUserResponse) {
        binding.apply {
            tvAccountName.text = account.name ?: "-"
            tvAccountLogin.text = account.login
            tvAccountFollowing.text = "${account.following.toString()} following"
            tvAccountFollower.text = "${account.followers.toString()} followers"
            Glide.with(this@DetailUserActivity)
                .load(account.avatarUrl)
                .into(ivPic)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}