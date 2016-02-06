package com.github.shchurov.gitterclient.presentation.presenters

interface BasePresenter {

    open fun onCreate() {
    }

    open fun onRestart() {
    }

    open fun onStart() {
    }

    open fun onResume() {
    }

    open fun onPause() {
    }

    open fun onStop() {
    }

    open fun onDestroy() {
    }

}