package com.dixon.app.kmmsample.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dixon.app.kmmsample.MR
import com.dixon.app.kmmsample.core.base.Logger
import com.dixon.app.kmmsample.logic.home.HomeViewModel
import com.dixon.app.kmmsample.ui.Builder
import com.dixon.app.kmmsample.ui.LocalNavigator
import com.dixon.app.kmmsample.ui.PAGE_IMAGE_DETAIL_ROUTE
import com.dixon.app.kmmsample.ui.build
import com.dixon.app.kmmsample.ui.put
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import moe.tlaster.precompose.viewmodel.viewModel

/**
 * Navigator与传统Activity不同，跨页面要使用ViewModel缓存数据，否则返回页面会重新请求
 */
@Composable
fun HomeMain() {
    Logger.i("HomeMain") { "Composable Root : HomeMain" }
    val navigator = LocalNavigator.current
    val homeVM = viewModel(
        modelClass = HomeViewModel::class,
        creator = {
            HomeViewModel()
        })
    val data by homeVM.data
    Column(
        // TODO 优化StatueBar
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 68.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(MR.strings.welcome_message),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "本地图展示",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(20.dp))
        Image(
            modifier = Modifier.clip(RoundedCornerShape(10.dp)),
            painter = painterResource(MR.images.demo),
            contentDescription = null
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "网络功能及网图展示",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            homeVM.fetchData()
        }) {
            Text("点击获取随机图片数据")
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = data?.toString() ?: "点击发送请求!",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            navigator.Builder(PAGE_IMAGE_DETAIL_ROUTE)
                .put("resource", data?.url)
                .build()
        }) {
            Text("展示网图")
        }
    }
}


