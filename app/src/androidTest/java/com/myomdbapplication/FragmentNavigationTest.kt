package com.myomdbapplication

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.myomdbapplication.ui.SplashFragment
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLooper

@RunWith(AndroidJUnit4::class)
class FragmentNavigationTest {

    @Test
    fun testNavigationToHomeScreen() {
        // Set navigation graph
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.app_nav)

        val splashFragment = launchFragmentInContainer<SplashFragment>()

        splashFragment.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.moviesHomeFragment)
    }
}