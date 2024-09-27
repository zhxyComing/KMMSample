package com.dixon.app.kmmsample.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dixon.app.kmmsample.MR
import com.dixon.app.kmmsample.ui.common.TabContainer
import com.dixon.app.kmmsample.ui.common.TabInfo
import com.dixon.app.kmmsample.ui.record.RecordPage
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

data class HomeTabInfo(
    val title: String,
    val icon: ImageResource,
    override val tabContent: @Composable () -> Unit,
) : TabInfo

private val HOME_TABS = listOf(
    HomeTabInfo(
        title = "Record",
        icon = MR.images.home,
        tabContent = { RecordPage() }
    ),
    HomeTabInfo(
        title = "Record",
        icon = MR.images.home,
        tabContent = { RecordPage() }
    ),
    HomeTabInfo(
        title = "Record",
        icon = MR.images.home,
        tabContent = { RecordPage() }
    ),
    HomeTabInfo(
        title = "Record",
        icon = MR.images.home,
        tabContent = { RecordPage() }
    )
)

@Composable
fun HomeContainer() {
    TabContainer(
        tabs = HOME_TABS,
        title = {
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Black
            )
        },
        icon = {
            Image(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                painter = painterResource(icon),
                contentDescription = null
            )
        })
}

