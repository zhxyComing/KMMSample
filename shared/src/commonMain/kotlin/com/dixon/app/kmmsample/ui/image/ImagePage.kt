package com.dixon.app.kmmsample.ui.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.dixon.app.kmmsample.core.base.Logger
import com.dixon.app.kmmsample.ui.LocalNavigator

@Composable
fun ImageDetail(resource: String?) {
    Logger.i("ImageDetail") { "Composable Root : ImageDetail $resource" }
    val navigator = LocalNavigator.current
    if (resource.isNullOrEmpty()) {
        Logger.i("ImageDetail") { "Composable Root : ImageDetail ErrorTipPage" }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 68.dp) // TODO 优化StatueBar
        ) {
            Text(
                text = "Error",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(20.dp))
            Text(text = "Please request image first！")
            Spacer(Modifier.height(20.dp))
            Button(onClick = {
                navigator.goBack()
            }) {
                Text("返回")
            }
        }
    } else {
        Logger.i("ImageDetail") { "Composable Root : ImageDetail ContentPage" }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 20.dp, vertical = 48.dp) // TODO 优化StatueBar
        ) {
            /*
             or ImageRequest、Painter
             */
            AsyncImage(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                    .clip(RoundedCornerShape(10.dp)),
                model = resource,
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Button(onClick = {
                navigator.goBack()
            }) {
                Text("Back")
            }
        }
    }
}