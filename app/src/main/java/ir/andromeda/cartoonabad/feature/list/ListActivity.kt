package ir.andromeda.cartoonabad.feature.list

import android.os.Bundle
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadActivity

class ListActivity : CartoonAbadActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }
}