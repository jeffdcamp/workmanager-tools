package org.dbtools.android.work.ux.monitor

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.MenuRes
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.work.NetworkType
import androidx.work.impl.model.WorkSpec
import org.dbtools.android.work.R

class WorkManagerStatusAdapter(val viewModel: WorkManagerStatusViewModel) : ListAdapter<WorkSpec, WorkManagerStatusAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent).apply {
            setOnMenuClickListener(this, menuView, R.menu.menu_popup_worker_item) { position, menuItem ->
                return@setOnMenuClickListener when (menuItem.itemId) {
                    R.id.menu_popup_cancel -> {
                        viewModel.onCancelWorker(getItem(position))
                    }
                    else -> false
                }
            }
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workSpec = getItem(position)
        holder.nameTextView.text = formatName(workSpec)

        val info = if (workSpec.isPeriodic) {
            formatPeriodic(holder.itemView.context, workSpec)
        } else {
            formatOneTime(holder.itemView.context, workSpec)
        }

        holder.statusTextView.text = info
    }

    private fun formatName(workSpec: WorkSpec): String {
        return workSpec.workerClassName.substringAfterLast(".")
    }

    @SuppressLint("RestrictedApi")
    private fun formatOneTime(context: Context, workSpec: WorkSpec): String {
        val schedule = if (workSpec.scheduleRequestedAt > 0) formatDateTime(context, workSpec.scheduleRequestedAt) else ""
        val periodStart = formatDateTime(context, workSpec.periodStartTime)
        val nextRun = formatDateTime(context, workSpec.calculateNextRunTime())
        return """
            Type: OneTime
            State: ${workSpec.state}
            Constraints: ${formatConstraints(workSpec)}
            Schedule Requested At: $schedule
            Period Start: $periodStart
            Calc Next Run: $nextRun
        """.trimIndent()
    }

    @SuppressLint("RestrictedApi")
    private fun formatPeriodic(context: Context, workSpec: WorkSpec): String {
        val interval = formatInterval(workSpec.intervalDuration)
        val schedule = if (workSpec.scheduleRequestedAt > 0) formatDateTime(context, workSpec.scheduleRequestedAt) else ""
        val periodStart = formatDateTime(context, workSpec.periodStartTime)
        val nextRun = formatDateTime(context, workSpec.calculateNextRunTime())
        return """
            Type: Periodic
            State: ${workSpec.state}
            Constraints: ${formatConstraints(workSpec)}
            Interval: $interval
            Schedule Requested At: $schedule
            Period Start: $periodStart
            Calc Next Run: $nextRun
        """.trimIndent()

    }

    private fun formatConstraints(workSpec: WorkSpec): String {
        var constrantsText = ""
        if (workSpec.constraints.requiredNetworkType != NetworkType.NOT_REQUIRED) {
            constrantsText += "NETWORK[${workSpec.constraints.requiredNetworkType}] "
        }
        if (workSpec.constraints.requiresBatteryNotLow()) {
            constrantsText += "BATTERY_NOT_LOW "
        }
        if (workSpec.constraints.requiresCharging()) {
            constrantsText += "CHARGING "
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && workSpec.constraints.requiresDeviceIdle()) {
            constrantsText += "IDLE "
        }
        if (workSpec.constraints.requiresStorageNotLow()) {
            constrantsText += "STORAGE_NOT_LOW "
        }
        if (constrantsText.isBlank()) {
            constrantsText += "NONE"
        }

        return constrantsText
    }


    private fun formatDateTime(context: Context, ts: Long): String {
        return DateUtils.formatDateTime(context, ts, (DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME))
    }

    private fun formatInterval(length: Long): kotlin.String {
        val day = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(length)
        val hr = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(length)
        val min = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(length - java.util.concurrent.TimeUnit.HOURS.toMillis(hr))

        return when {
            day > 0 -> kotlin.String.format("%dd %dh %dm", day, hr, min)
            hr > 0 -> kotlin.String.format("%dh %dm", hr, min)
            min > 0 -> kotlin.String.format("%dm", min)
            else -> "$length ms"
        }
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.work_manager_status_list_item, parent, false)) {
        val nameTextView: TextView by lazy { itemView.findViewById<TextView>(R.id.nameTextView) }
        val statusTextView: TextView by lazy { itemView.findViewById<TextView>(R.id.statusTextView) }
        val menuView: View by lazy { itemView.findViewById<View>(R.id.menuView) }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<WorkSpec> = object : DiffUtil.ItemCallback<WorkSpec>() {
            override fun areItemsTheSame(oldItem: WorkSpec, newItem: WorkSpec): Boolean {
                return oldItem.id == oldItem.id
            }

            override fun areContentsTheSame(oldItem: WorkSpec, newItem: WorkSpec): Boolean {
                return false
            }
        }
    }

    /**
     *     var menuItemClickListener: (MyItem, MenuItem) -> Boolean = { _, _ -> false }
     *
     *     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
     *         return ViewHolder(parent).apply {
     *             setOnMenuClickListener(itemView.menuButton, R.menu.menu_popup_notebook) { position, menuItem -> menuItemClickListener(items[position], menuItem) }
     *         }
     *     }
     */
    private fun setOnMenuClickListener(viewHolder: RecyclerView.ViewHolder,
        view: View,
        @MenuRes menuResourceId: Int,
        onPreparePopupMenu: (position: Int, menu: Menu) -> Unit = { _, _ -> },
        block: (position: Int, menuItem: MenuItem) -> Boolean
    ) {
        view.setOnClickListener {
            executeOnValidPosition(viewHolder) { position ->
                val popupMenu = PopupMenu(view.context, view)
                val inflater = popupMenu.menuInflater
                val menu = popupMenu.menu

                inflater.inflate(menuResourceId, menu)

                // customization to menu
                onPreparePopupMenu(position, menu)

                // click listener
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    block(position, menuItem)
                }

                // show
                popupMenu.show()
            }
        }
    }

    private inline fun executeOnValidPosition(viewHolder: RecyclerView.ViewHolder, block: (position: Int) -> Unit) {
        if (viewHolder.adapterPosition != android.support.v7.widget.RecyclerView.NO_POSITION) {
            block(viewHolder.adapterPosition)
        }
    }

}
