package com.dixon.app.kmmsample.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : TabInfo> TabContainer(
    tabs: List<T>,
    title: @Composable T.() -> Unit,
    icon: @Composable T.() -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
    ) { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    Column {
        // ViewPager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) { page ->
            tabs[page].tabContent()
        }
        // TabRow
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color(0xFFF0F0F0)
        ) {
            tabs.forEachIndexed { index, tabInfo ->
                Tab(
                    text = { title(tabInfo) },
                    icon = { icon(tabInfo) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }
    }
}

interface TabInfo {

    val tabContent: @Composable () -> Unit
}
