package com.project.veganlife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.project.veganlife.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    companion object {
        const val BASE_URL = "https://dev.konggogi.store/api/v1/"
    }

    val permissionList =
        arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        requestPermissions(permissionList, 0)
        initNavController()
        handleOnBackPressed()

    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main_containerview) as NavHostFragment
        val navController = navHostFragment.navController

        // 바텀 네비게이션의 표시 여부를 한 번에 관리
        activityMainBinding.bnvMainNavigation.apply {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.homeFragment,
                    R.id.communityHomeFragment,
                    R.id.lifeCheckHomeFragment,
                    R.id.mypageHomeFragment,
                    R.id.recipeHomeFragment,
                    -> {
                        activityMainBinding.bnvMainNavigation.apply {
                            alpha = 0f
                            visibility = View.VISIBLE
                            // 바텀 네비게이션 UI가 갑자기 나타나고 사라지는 현상을 부드럽게 처리하기 위한 애니메이션
                            animate().alpha(1f).setDuration(100).start()
                        }
                    }

                    else -> {
                        activityMainBinding.bnvMainNavigation.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun handleOnBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main_containerview) as NavHostFragment
        val navController =
            navHostFragment.navController

        val callback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (navController.currentDestination?.id) {
                        R.id.homeFragment,
                        R.id.recipeHomeFragment,
                        R.id.communityHomeFragment,
                        R.id.mypageHomeFragment,
                        R.id.lifeCheckHomeFragment,
                        -> finish()

                        else ->
                            if (navController.popBackStack().not()) {
                                isEnabled = false
                            }
                    }
                }
            }

        onBackPressedDispatcher.addCallback(this, callback)
    }
}
