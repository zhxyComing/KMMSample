package com.dixon.app.kmmsample.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import com.dixon.app.kmmsample.ui.home.HomeContainer
import com.dixon.app.kmmsample.ui.home.HomeMain
import com.dixon.app.kmmsample.ui.image.ImageDetail
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator


data class Page(
    val route: String,
    val content: @Composable AnimatedContentScope.(BackStackEntry) -> Unit
)

const val PAGE_HOME_MAIN_ROUTE = "/home_main"
const val PAGE_IMAGE_DETAIL_ROUTE = "/home_demo"
const val PAGE_HOME_CONTAINER_ROUTE = "/home_container"

val pages = listOf(
    Page(PAGE_HOME_CONTAINER_ROUTE) {
        HomeContainer()
    },
    Page(PAGE_HOME_MAIN_ROUTE) {
        HomeMain()
    },
    Page(PAGE_IMAGE_DETAIL_ROUTE) { backStackEntry ->
        val resource: String? = backStackEntry.query("resource")
        ImageDetail(resource)
    }
)

/*
 用PreCompose-SDK提供的query方法编译不通过
 貌似是JVM编译版本的问题，我想使用自己的JVM版本，所以这里把代码拷了出来
 */
inline fun <reified T> BackStackEntry.query(name: String, default: T? = null): T? {
    val value = queryString?.map?.get(name)?.firstOrNull() ?: return default
    return convertValue(value)
}

inline fun <reified T> convertValue(value: String): T? {
    return when (T::class) {
        Int::class -> value.toIntOrNull()
        Long::class -> value.toLongOrNull()
        String::class -> value
        Boolean::class -> value.toBooleanStrictOrNull()
        Float::class -> value.toFloatOrNull()
        Double::class -> value.toDoubleOrNull()
        else -> throw NotImplementedError()
    } as T
}

fun Navigator.Builder(route: String) = NavigatorBuilder(this, route)

fun NavigatorBuilder.put(key: String, value: Any?) = this.apply {
    value?.let {
        params[key] = it
    }
    // 为空则直接忽略 传xx=null时，backStackEntry.query会获取到字符串null，导致一些问题
}

fun NavigatorBuilder.put(params: Map<String, Any>) = this.apply {
    this.params.putAll(params)
}

fun NavigatorBuilder.options(options: NavOptions) = this.apply {
    this.options = options
}

fun NavigatorBuilder.build() {
    var realRoute = route
    if (params.isNotEmpty()) {
        realRoute += "?"
        params.forEach {
            realRoute += "${it.key}=${it.value}&"
        }
        realRoute.substring(0, realRoute.length - 1)
    }
    navigator.navigate(realRoute, options)
}


class NavigatorBuilder(val navigator: Navigator, val route: String) {
    val params: MutableMap<String, Any> = mutableMapOf()
    var options: NavOptions? = null
}