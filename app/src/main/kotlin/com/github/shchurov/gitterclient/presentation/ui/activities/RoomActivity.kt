package com.github.shchurov.gitterclient.presentation.ui.activities

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.dagger.modules.RoomModule
import com.github.shchurov.gitterclient.domain.models.Message
import com.github.shchurov.gitterclient.presentation.presenters.RoomPresenter
import com.github.shchurov.gitterclient.presentation.ui.RoomView
import com.github.shchurov.gitterclient.presentation.ui.adapters.MessagesAdapter
import com.github.shchurov.gitterclient.utils.*
import com.github.shchurov.gitterclient.utils.animation_flow.AnimationFlow
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
    private lateinit var progressBarLoading: ProgressBar
    private lateinit var etNewMessage: EditText
    private lateinit var flSend: FrameLayout
    private lateinit var tvSend: TextView
    private lateinit var progressBarSending: ProgressBar
    private lateinit var ivError: ImageView
    private var adapter = MessagesAdapter(this)
    private var animation: AnimationFlow? = null

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
        tvSend.setOnClickListener { presenter.onSendClick(etNewMessage.text.toString()) }
        initSendTranslation()
        setupMessageTextChangedListener()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar) as Toolbar
        rvMessages = findViewById(R.id.rvMessages) as RecyclerView
        progressBarLoading = findViewById(R.id.progressBarLoading) as ProgressBar
        etNewMessage = findViewById(R.id.etNewMessage) as EditText
        tvSend = findViewById(R.id.tvSend) as TextView
        flSend = findViewById(R.id.flSend) as FrameLayout
        progressBarSending = findViewById(R.id.progressBarSending) as ProgressBar
        ivError = findViewById(R.id.ivError) as ImageView
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

    private fun initSendTranslation() {
        flSend.post { flSend.translationY = flSend.height.toFloat() }
    }

    private fun setupMessageTextChangedListener() {
        etNewMessage.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (before == 0 && s.length > 0) {
                    showSendButton();
                } else if (before > 0 && s.length == 0) {
                    hideSendButton();
                }
            }
        })
    }

    private fun showSendButton() {
        animation?.cancel()
        animation = AnimationFlow.create()
                .play(sendContainerShowAnimation)
                .start()
    }

    private val sendContainerShowAnimation = SendContainerAnimation(0f)

    private fun hideSendButton() {
        animation?.cancel()
        animation = AnimationFlow.create()
                .play(sendContainerHideAnimation)
                .start()
    }

    private val sendContainerHideAnimation by lazy { SendContainerAnimation(flSend.height.toFloat()) }

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
        progressBarLoading.visibility = View.VISIBLE
    }

    override fun hideInitLoading() {
        progressBarLoading.visibility = View.GONE
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

    override fun hideKeyboard() {
        hideSoftInputKeyboard()
    }

    override fun showSendingInProgress() {
        animation?.cancel()
        disableMessageEditText()
        animation = AnimationFlow.create()
                .play(sendContainerHideAnimation)
                .setup {
                    tvSend.visibility = View.INVISIBLE
                    progressBarSending.visibility = View.VISIBLE
                }
                .play(sendContainerShowAnimation)
                .start()
    }

    fun disableMessageEditText() {
        etNewMessage.isEnabled = false
        etNewMessage.isClickable = false
    }

    override fun showSendingError() {
        animation?.cancel()
        animation = AnimationFlow.create()
                .play(sendContainerHideAnimation)
                .setup {
                    tvSend.visibility = View.INVISIBLE
                    progressBarSending.visibility = View.GONE
                    ivError.visibility = View.VISIBLE
                }
                .play(sendContainerShowAnimation)
                .wait(600)
                .play(sendContainerHideAnimation)
                .setup {
                    ivError.visibility = View.GONE
                    tvSend.visibility = View.VISIBLE
                    enableMessageEditText()
                }
                .play(sendContainerShowAnimation)
                .start()
    }

    fun enableMessageEditText() {
        etNewMessage.isEnabled = true
        etNewMessage.isClickable = true
    }

    override fun hideSendingInProgress() {
        animation?.cancel()
        animation = AnimationFlow.create()
                .play(sendContainerHideAnimation)
                .setup {
                    progressBarSending.visibility = View.GONE
                    tvSend.visibility = View.VISIBLE
                    enableMessageEditText()
                }
                .start()
    }

    override fun clearMessageEditText() {
        etNewMessage.setText("")
    }

    override fun addMessage(message: Message) {
        adapter.insertMessage(message)
        rvMessages.post { rvMessages.smoothScrollToPosition(0) }
    }

    private inner class SendContainerAnimation(private val endValue: Float) : AnimationFlow.Animation {
        override fun createAnimator() = ObjectAnimator.ofFloat(flSend, "translationY", flSend.translationY, endValue)

        override fun onCancel() {
        }
    }

}