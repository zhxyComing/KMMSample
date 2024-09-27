package com.dixon.app.kmmsample.ui.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RecordPage() {
    Column(modifier = Modifier.fillMaxSize().padding(top = 20.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
        ) {

        }
    }
}