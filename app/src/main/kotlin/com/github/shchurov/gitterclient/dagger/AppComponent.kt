package com.github.shchurov.gitterclient.dagger

import com.github.shchurov.gitterclient.domain.interactors.implementation.MyRoomsInteractorImpl
import com.github.shchurov.gitterclient.domain.interactors.implementation.RoomMessagesInteractorImpl
import com.github.shchurov.gitterclient.domain.interactors.implementation.TokenInteractorImpl
import com.github.shchurov.gitterclient.presentation.presenters.implementations.LogInPresenterImpl
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(interactor: MyRoomsInteractorImpl)

    fun inject(interactor: RoomMessagesInteractorImpl)

    fun inject(interactor: TokenInteractorImpl)

    fun inject(presenter: LogInPresenterImpl)

}