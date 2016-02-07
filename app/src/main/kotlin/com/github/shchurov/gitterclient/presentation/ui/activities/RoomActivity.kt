package com.github.shchurov.gitterclient.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.presentation.presenters.RoomPresenter
import com.github.shchurov.gitterclient.presentation.presenters.implementations.RoomPresenterImpl
import com.github.shchurov.gitterclient.presentation.ui.RoomView
import com.github.shchurov.gitterclient.utils.MessagesItemDecoration
import com.github.shchurov.gitterclient.utils.PagingScrollListener

class RoomActivity : AppCompatActivity(), RoomView {

    companion object {
        private const val EXTRA_ROOM_ID = "room_id"
        private const val EXTRA_ROOM_NAME = "room_name"
        private const val PAGING_THRESHOLD = 10

        fun start(context: Context, roomId: String, roomName: String) {
            val intent = Intent(context, RoomActivity::class.java)
            intent.putExtra(EXTRA_ROOM_ID, roomId)
            intent.putExtra(EXTRA_ROOM_NAME, roomName)
            context.startActivity(intent)
        }
    }

    private lateinit var presenter: RoomPresenter
    private lateinit var toolbar: Toolbar
    private lateinit var rvMessages: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_activity)
        initViews()
        setupToolbar()
        setupRecyclerView()
        setupPresenter()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar) as Toolbar
        rvMessages = findViewById(R.id.rvMessages) as RecyclerView
        progressBar = findViewById(R.id.progressBar) as ProgressBar
    }

    private fun setupToolbar() {
        val title = intent.getStringExtra(EXTRA_ROOM_NAME)
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        with (rvMessages) {
            layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true)
            setHasFixedSize(true)
            addItemDecoration(MessagesItemDecoration())
            addOnScrollListener(pagingListener)
        }
    }

    private val pagingListener = object : PagingScrollListener(PAGING_THRESHOLD) {
        override fun onLoadMoreItems() {
            presenter.onLoadMoreItems()
        }
    }

    private fun setupPresenter() {
        presenter = RoomPresenterImpl(this)
        presenter.onCreate()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun setRecyclerViewAdapter(
            adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
        rvMessages.adapter = adapter
    }

    override fun getRoomId() = intent.getStringExtra(EXTRA_ROOM_ID)

    override fun showInitLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideInitLoading() {
        progressBar.visibility = View.GONE
    }

    override fun enablePagingListener() {
        pagingListener.enabled = true
    }

    override fun disablePagingListener() {
        pagingListener.enabled = false
    }
}