package com.github.shchurov.gitterclient.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.github.shchurov.gitterclient.App
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.dagger.components.DaggerGeneralScreenComponent
import com.github.shchurov.gitterclient.domain.models.Room
import com.github.shchurov.gitterclient.presentation.presenters.RoomsListPresenter
import com.github.shchurov.gitterclient.presentation.ui.RoomsListView
import com.github.shchurov.gitterclient.presentation.ui.adapters.RoomsAdapter
import javax.inject.Inject

class RoomsListActivity : AppCompatActivity(), RoomsListView, RoomsAdapter.ActionListener {

    @Inject lateinit var presenter: RoomsListPresenter
    private lateinit var rvRooms: RecyclerView
    private lateinit var toolbar: Toolbar
    private val adapter = RoomsAdapter(this)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RoomsListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencies()
        setupUi()
        presenter.attach(this)
    }

    private fun initDependencies() {
        val component = DaggerGeneralScreenComponent.builder()
                .appComponent(App.appComponent)
                .build()
        component.inject(this)
    }

    private fun setupUi() {
        setContentView(R.layout.rooms_list_activity)
        initViews()
        setupRecyclerView()
        setupToolbar()
    }

    private fun initViews() {
        rvRooms = findViewById(R.id.rvRooms) as RecyclerView
        toolbar = findViewById(R.id.toolbar) as Toolbar
    }

    private fun setupRecyclerView() {
        rvRooms.layoutManager = LinearLayoutManager(this)
        rvRooms.setHasFixedSize(false)
        rvRooms.adapter = adapter
    }

    private fun setupToolbar() {
        toolbar.setTitle(R.string.rooms)
    }

    override fun onRestart() {
        super.onRestart()
        presenter.onReturnFromBackground()
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    override fun showLoading() {
        adapter.loading = true
    }

    override fun hideLoading() {
        adapter.loading = false
    }

    override fun displayRooms(rooms: List<Room>) {
        adapter.rooms = rooms
    }

    override fun goToRoomScreen(id: String, name: String) {
        RoomActivity.start(this, id, name)
    }

    override fun onRoomClick(room: Room) {
        presenter.onRoomClick(room)
    }

}