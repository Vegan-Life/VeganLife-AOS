package com.project.veganlife

import android.Manifest
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.project.veganlife.databinding.ActivityMainBinding
import com.project.veganlife.splash.SplashFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    val permissionList = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ✅ Android 15 Edge-to-Edge 대응
        enableEdgeToEdge()

        // ✅ 시스템 바 영역을 직접 처리하도록 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        styleBottomNav(activityMainBinding.bnvMainNavigation)

        requestPermissions(permissionList, 0)
        initNavController()
        handleOnBackPressed()
        initSplash()

    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main_containerview) as NavHostFragment
        val navController = navHostFragment.navController

        activityMainBinding.bnvMainNavigation.selectedItemId = R.id.homeFragment

        // -> 방향
        val rightNavOptions =
            NavOptions.Builder().setEnterAnim(R.anim.slide_in_right) // 화면 진입 애니메이션
                .setExitAnim(R.anim.slide_out_left)   // 화면 나가는 애니메이션
                .setPopEnterAnim(android.R.anim.slide_in_left) // 백스택에서 화면으로 돌아올 때 애니메이션
                .setPopExitAnim(android.R.anim.slide_out_right)
                //.setPopUpTo(R.id.item_walk, false)// 백스택에서 화면으로 돌아올 때 애니메이션
                .build()

        // <- 방향
        val leftNavOptions = NavOptions.Builder().setEnterAnim(android.R.anim.slide_in_left)
            .setExitAnim(android.R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_in_right)
            .setPopExitAnim(R.anim.slide_out_left)
            //.setPopUpTo(R.id.item_walk, false)
            .build()

        activityMainBinding.bnvMainNavigation.setOnItemSelectedListener { item ->
            // 현재 탭 ID -> 현재 목적지 ID를 가져오고 현재 목적지를 찾지 못하면 -1
            val currentId = navController.currentDestination?.id ?: -1

            // 다음으로 이동할 탭 ID
            val nextDestinationId = item.itemId

            val options = when (currentId) {
                R.id.lifeCheckHomeFragment -> {
                    rightNavOptions
                }

                R.id.recipeHomeFragment -> {
                    if (nextDestinationId == R.id.lifecheck_graph) {
                        leftNavOptions
                    } else {
                        rightNavOptions
                    }
                }

                R.id.homeFragment -> {
                    if (nextDestinationId == R.id.lifecheck_graph || nextDestinationId == R.id.recipe_graph) {
                        leftNavOptions
                    } else {
                        rightNavOptions
                    }
                }

                R.id.communityHomeFragment -> {
                    if (nextDestinationId == R.id.mypage_graph) {
                        rightNavOptions
                    } else {
                        leftNavOptions
                    }
                }

                R.id.mypageHomeFragment -> {
                    leftNavOptions
                }

                else -> null
            }

            options?.let {
                navController.navigate(nextDestinationId, null, it)
                true
            } == true
        }

        activityMainBinding.bnvMainNavigation.setOnItemReselectedListener {

        }

        // 바텀 네비게이션의 표시 여부를 한 번에 관리
        activityMainBinding.bnvMainNavigation.apply {
//            setupWithNavController(navController)

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
        val navController = navHostFragment.navController

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (navController.currentDestination?.id) {
                    R.id.homeFragment,
                    R.id.recipeHomeFragment,
                    R.id.communityHomeFragment,
                    R.id.mypageHomeFragment,
                    R.id.lifeCheckHomeFragment,
                    -> finish()

                    else -> if (navController.popBackStack().not()) {
                        isEnabled = false
                    }
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }

    fun resetBottomNavigationToHome() {
        activityMainBinding.bnvMainNavigation.selectedItemId = R.id.homeFragment
    }

    private fun initSplash() {
        activityMainBinding.apply {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(flSplashContainer.id, SplashFragment())
                .commit()
        }
    }

    private fun styleBottomNav(bnv: BottomNavigationView) {
        val r = resources.getDimensionPixelSize(R.dimen.bottom_nav_radius_28).toFloat()
        val shape = ShapeAppearanceModel.Builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, r)
            .setTopRightCorner(CornerFamily.ROUNDED, r)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .build()

        val bg = MaterialShapeDrawable(shape).apply {
            fillColor = ColorStateList.valueOf(ContextCompat.getColor(bnv.context, android.R.color.white))
            elevation = resources.getDimension(R.dimen.bottom_nav_elevation_8)
            shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
            initializeElevationOverlay(bnv.context)
        }

        bnv.background = bg
        bnv.elevation = resources.getDimension(R.dimen.bottom_nav_elevation_8)

        (bnv.parent as? ViewGroup)?.apply {
            clipToPadding = false
            clipChildren = false
        }
    }


}
