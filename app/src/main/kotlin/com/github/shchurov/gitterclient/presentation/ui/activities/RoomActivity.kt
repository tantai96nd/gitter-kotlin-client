package com.github.shchurov.gitterclient.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.dagger.modules.RoomModule
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.presentation.presenters.RoomPresenter
import com.github.shchurov.gitterclient.presentation.ui.RoomView
import com.github.shchurov.gitterclient.presentation.ui.adapters.MessagesAdapter
import com.github.shchurov.gitterclient.utils.MessagesItemDecoration
import com.github.shchurov.gitterclient.utils.PagingScrollListener
import com.github.shchurov.gitterclient.utils.SimpleTextWatcher
import com.github.shchurov.gitterclient.utils.VisiblePositionsScrollListener
import javax.inject.Inject

class RoomActivity : AppCompatActivity(), RoomView, MessagesAdapter.ActionListener {

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

    @Inject lateinit var presenter: RoomPresenter
    private lateinit var toolbar: Toolbar
    private lateinit var rvMessages: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var etNewMessage: EditText
    private lateinit var tvSend: TextView
    private lateinit var llNewMessage: LinearLayout
    private var adapter = MessagesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencies()
        setupUi()
        presenter.attach(this)
    }

    private fun initDependencies() {
        App.component.createRoomComponent(RoomModule(getRoomId()))
                .inject(this)
    }

    private fun setupUi() {
        setContentView(R.layout.room_activity)
        initViews()
        setupToolbar()
        setupRecyclerView()
        setupMessageTextChangedListener()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar) as Toolbar
        rvMessages = findViewById(R.id.rvMessages) as RecyclerView
        progressBar = findViewById(R.id.progressBar) as ProgressBar
        etNewMessage = findViewById(R.id.etNewMessage) as EditText
        tvSend = findViewById(R.id.tvSend) as TextView
        llNewMessage = findViewById(R.id.llNewMessage) as LinearLayout
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        with (rvMessages) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            setHasFixedSize(true)
            addItemDecoration(MessagesItemDecoration())
            addOnScrollListener(pagingScrollListener)
            addOnScrollListener(readScrollListener)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        rvMessages.adapter = adapter
    }

    private val pagingScrollListener = object : PagingScrollListener(PAGING_THRESHOLD) {
        override fun onLoadMoreItems() {
            presenter.onLoadMoreItems()
        }
    }

    private val readScrollListener = object : VisiblePositionsScrollListener() {
        override fun onVisiblePositionsChanged(firstPosition: Int, lastPosition: Int) {
            val visibleMessages = adapter.getMessagesInRange(firstPosition, lastPosition)
            presenter.onVisibleMessagesChanged(visibleMessages)
        }
    }

    private fun setupMessageTextChangedListener() {
        etNewMessage.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) {
                    hideSendButtonIfNot();
                } else {
                    showSendButtonIfNot();
                }
            }
        })
    }

    private fun hideSendButtonIfNot() {
        if (tvSend.translationY == 0f) {
            tvSend.animate()
                    .translationY((llNewMessage.height - tvSend.y).toFloat())
        }
    }

    private fun showSendButtonIfNot() {
        if (tvSend.translationY != 0f) {
            tvSend.animate()
                    .translationY(0f)
        }
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun getRoomId() = intent.getStringExtra(EXTRA_ROOM_ID)

    override fun getRoomName() = intent.getStringExtra(EXTRA_ROOM_NAME)

    override fun setToolbarTitle(title: String) {
        toolbar.title = title
    }

    override fun showInitLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideInitLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showLoadingMore() {
        adapter.loading = true
    }

    override fun hideLoadingMore() {
        adapter.loading = false
    }

    override fun addMessages(messages: List<Message>) {
        adapter.addMessages(messages.asReversed())
        forceOnReadPositionsChangedCallback()
    }

    private fun forceOnReadPositionsChangedCallback() {
        rvMessages.postDelayed({ readScrollListener.forceCallback(rvMessages) }, 100)
    }

    override fun enablePagingListener() {
        pagingScrollListener.enabled = true
    }

    override fun disablePagingListener() {
        pagingScrollListener.enabled = false
    }

    override fun invalidateMessage(message: Message) {
        adapter.invalidateMessage(message)
    }

}