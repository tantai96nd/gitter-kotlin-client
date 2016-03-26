package com.github.shchurov.gitterclient.presentation.presenters

abstract class BasePresenter<V> {

    private var view: V? = null

    fun attach(view: V) {
        this.view = view
        onAttach()
    }

    open fun onAttach() {
    }

    fun detach() {
        onDetach()
        view = null
    }

    open fun onDetach() {
    }

    protected fun getView() = view!!

}