package org.dbtools.android.work.ux.monitor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import org.dbtools.android.work.R

class WorkManagerStatusFragment : Fragment() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(WorkManagerStatusViewModel::class.java) }
    private val adapter by lazy { WorkManagerStatusAdapter(requireContext(), viewModel) }
    private lateinit var workManagerRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.work_manager_status_fragment, container, false)
        workManagerRecyclerView = view.findViewById(R.id.workManagerRecyclerView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        activity?.actionBar?.title = "WorkManager Status"
        setupRecyclerView()

        viewModel.init(requireContext())
        viewModel.onWorkSpecListUpdated = {
            activity?.runOnUiThread {
                adapter.submitList(it)
            }
        }
        viewModel.refresh()
    }

    private fun setupRecyclerView() {
        workManagerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        workManagerRecyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_workmanager, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.refresh()
                true
            }

            R.id.menu_cancel_all -> {
                WorkManager.getInstance(requireContext()).cancelAllWork()
                true
            }
            R.id.menu_prune -> {
                WorkManager.getInstance(requireContext()).pruneWork()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

