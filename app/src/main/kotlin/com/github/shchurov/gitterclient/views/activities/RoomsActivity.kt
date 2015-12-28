package com.github.shchurov.gitterclient.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.github.shchurov.gitterclient.R
import com.github.shchurov.gitterclient.presenters.implementations.RoomsPresenterImpl
import com.github.shchurov.gitterclient.presenters.interfaces.LogInPresenter
import com.github.shchurov.gitterclient.presenters.interfaces.RoomsPresenter
import com.github.shchurov.gitterclient.views.interfaces.RoomsView

class RoomsActivity : AppCompatActivity(), RoomsView {

    private lateinit var presenter: RoomsPresenter

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RoomsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rooms_activity)
        presenter = RoomsPresenterImpl(this)
        presenter.onCreate()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}