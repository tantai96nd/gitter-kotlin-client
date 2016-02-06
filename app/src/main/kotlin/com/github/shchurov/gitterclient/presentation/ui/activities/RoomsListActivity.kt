package com.github.shchurov.gitterclient.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.presentation.presenters.implementations.RoomsListPresenterImpl
import com.github.shchurov.gitterclient.presentation.presenters.RoomsListPresenter
import com.github.shchurov.gitterclient.presentation.ui.RoomsListView

class RoomsListActivity : AppCompatActivity(), RoomsListView {

    private lateinit var presenter: RoomsListPresenter
    private lateinit var rvRooms: RecyclerView
    private lateinit var toolbar: Toolbar

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RoomsListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rooms_list_activity)
        initViews()
        setupRecyclerView()
        setupToolbar()
        presenter = RoomsListPresenterImpl(this)
        presenter.onCreate()
    }

    private fun initViews() {
        rvRooms = findViewById(R.id.rvRooms) as RecyclerView
        toolbar = findViewById(R.id.toolbar) as Toolbar
    }

    private fun setupRecyclerView() {
        rvRooms.layoutManager = LinearLayoutManager(this)
        rvRooms.setHasFixedSize(false)
    }

    private fun setupToolbar() {
        toolbar.setTitle(R.string.rooms)
    }

    override fun onRestart() {
        super.onRestart()
        presenter.onRestart()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun setRecyclerViewAdapter(
            adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
        rvRooms.adapter = adapter
    }

    override fun getContext() = this
}