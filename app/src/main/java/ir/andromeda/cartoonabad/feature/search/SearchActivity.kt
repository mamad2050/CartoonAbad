package ir.andromeda.cartoonabad.feature.search

import android.os.Bundle
import ir.andromeda.cartoonabad.common.CartoonAbadActivity
import ir.andromeda.cartoonabad.databinding.ActivitySearchBinding

class SearchActivity : CartoonAbadActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}