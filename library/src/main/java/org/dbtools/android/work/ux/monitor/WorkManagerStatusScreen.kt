package org.dbtools.android.work.ux.monitor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

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
                navigationIcon ={
                    Icon(
                        modifier = Modifier
                            .clickable { onBack?.invoke() }
                            .padding(start = 12.dp),
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                    )
                },
                actions = {
                    // Wrapping content so that the action icons have the same color as the navigation icon and title.
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                        content = {
                            IconButton(onClick = { viewModel.refresh() }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                )
                            }
                            val overflowMenuItems = listOf(
                                OverflowItem("Prune") { viewModel.onPrune() },
                                OverflowItem("Cancel All") { viewModel.cancelAll() }
                            )
                            OverflowMenu(overflowMenuItems)
                        }
                    )
                },
            )
        },

    ) {
        val workSpecList by viewModel.workSpecListFlow.collectAsState()

        workSpecList?.let { list ->
            WorkManagerStatusContent(list, onCancelItem = { workSpecId -> viewModel.onCancelWorker(workSpecId) })
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WorkManagerStatusContent(listItems: List<ListItemData>, onCancelItem: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(listItems) { listItem ->
            ListItem(
                text = {
                    Text(listItem.title)

                },
                secondaryText = {
                    Text(listItem.content)
                },
                trailing = {
                    val menuItems = listOf(
                        OverflowItem("Cancel") { onCancelItem(listItem.workSpecId) }
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
        menuItems.forEach { menuItem ->
            DropdownMenuItem(onClick = {
                menuItem.action()
                expanded.value = false
            }) {
                Text(text = menuItem.text)
            }
        }
    }
}

private class OverflowItem(val text: String, val action: () -> Unit)
