package com.dixon.app.kmmsample.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

/*
    navigator 使用：
    Navigator.navigate(route: String, options: NavOptions? = null)
    Navigator.goBack()
    Navigator.canGoBack: Boolean
 */


val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("Navigator not provided")
}

@Composable
fun App() {
    PreComposeApp {
        // your app's content goes here
        val navigator = rememberNavigator()
        CompositionLocalProvider(LocalNavigator provides navigator) {
            NavHost(
                // Assign the navigator to the NavHost
                navigator = navigator,
                // Navigation transition for the scenes in this NavHost, this is optional
                navTransition = NavTransition(),
                // The start destination
                initialRoute = PAGE_HOME_CONTAINER_ROUTE,
            ) {
                pages.forEach {
                    scene(
                        // Scene's route path
                        route = it.route,
                        // Navigation transition for this scene, this is optional
                        navTransition = NavTransition(),
                        content = it.content
                    )
                }
            }
        }
    }
}
