package jp.digitalrabbit.reduxsample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import jp.digitalrabbit.reduxsample.R
import jp.digitalrabbit.reduxsample.MainViewModel
import jp.digitalrabbit.reduxsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewModel 生成 and DataBinding 設定
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                viewModel = ViewModelProviders.of(this@MainActivity).get(MainViewModel::class.java)
                lifecycleOwner = this@MainActivity
            }
    }
}
