package org.dbtools.android.work.ux.monitor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkManagerStatusScreen(
    onBack: (() -> Unit)? = null,
) {
    val viewModel: WorkManagerStatusViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "WorkManager Monitor"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack?.invoke() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    // Wrapping content so that the action icons have the same color as the navigation icon and title.
                    CompositionLocalProvider(
                        content = {
                            IconButton(onClick = { viewModel.refresh() }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                )
                            }
                            val overflowMenuItems = listOf(
                                OverflowItem({ "Prune" }) { viewModel.onPrune() },
                                OverflowItem({ "Cancel All" }) { viewModel.cancelAll() }
                            )
                            OverflowMenu(overflowMenuItems)
                        }
                    )
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            val workSpecList by viewModel.workSpecListFlow.collectAsState()

            workSpecList?.let { list ->
                WorkManagerStatusContent(list, onCancelItem = { workSpecId -> viewModel.onCancelWorker(workSpecId) })
            }
        }
    }
}

@Composable
private fun WorkManagerStatusContent(listItems: List<ListItemData>, onCancelItem: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(listItems) { listItem ->
            ListItem(
                headlineContent = {
                    Text(listItem.title)

                },
                supportingContent = {
                    Text(listItem.content)
                },
                trailingContent = {
                    val menuItems = listOf(
                        OverflowItem({ "Cancel" }) { onCancelItem(listItem.workSpecId) }
                    )
                    OverflowMenu(menuItems)
                }
            )
        }
    }
}

@Composable
private fun OverflowMenu(menuItems: List<OverflowItem>) {
    if (menuItems.isEmpty()) {
        return
    }

    val expanded = remember { mutableStateOf(false) }

    IconButton(onClick = {
        expanded.value = true
    }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null
        )
    }
    DropdownMenu(
        expanded = expanded.value,
        offset = DpOffset((-40).dp, (-40).dp),
        onDismissRequest = { expanded.value = false }) {

        // determine if there are any icons in the list... if so, make sure text without icons are all indented
        val menuItemsWithIconCount = menuItems.count { it.icon != null }
        val textWithoutIconPadding = if (menuItemsWithIconCount > 0) 36.dp else 0.dp // 36.dp == 24.dp (icon size) + 12.dp (gap)

        menuItems.forEach { menuItem ->
            val menuText = menuItem.text()
            DropdownMenuItem(
                onClick = {
                    menuItem.action()
                    expanded.value = false
                },
                text = {
                    if (menuItem.icon != null) {
                        Row {
                            Icon(
                                imageVector = menuItem.icon,
                                contentDescription = menuText,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Text(
                                text = menuText,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    } else {
                        Text(
                            text = menuText,
                            modifier = Modifier.padding(start = textWithoutIconPadding)
                        )
                    }
                },
                modifier = Modifier.defaultMinSize(minWidth = 175.dp)
            )
        }
    }
}

private class OverflowItem(
    val text: @Composable () -> String,
    val icon: ImageVector? = null,
    val action: () -> Unit
)
