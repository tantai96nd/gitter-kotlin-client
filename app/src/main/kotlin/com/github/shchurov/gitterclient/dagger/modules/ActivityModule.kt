package com.github.shchurov.gitterclient.dagger.modules

import android.app.Activity
import com.github.shchurov.gitterclient.dagger.scopes.PerActivity
import com.github.shchurov.gitterclient.data.Preferences
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.GetRoomMessagesInteractor
import com.github.shchurov.gitterclient.domain.interactors.GetRoomsInteractor
import com.github.shchurov.gitterclient.domain.interactors.GetTokenInteractor
import com.github.shchurov.gitterclient.domain.interactors.implementation.GetRoomMessagesInteractorImpl
import com.github.shchurov.gitterclient.domain.interactors.implementation.GetRoomsInteractorImpl
import com.github.shchurov.gitterclient.domain.interactors.implementation.GetTokenInteractorImpl
import com.github.shchurov.gitterclient.presentation.presenters.LogInPresenter
import com.github.shchurov.gitterclient.presentation.presenters.RoomPresenter
import com.github.shchurov.gitterclient.presentation.presenters.RoomsListPresenter
import com.github.shchurov.gitterclient.presentation.presenters.implementations.LogInPresenterImpl
import com.github.shchurov.gitterclient.presentation.presenters.implementations.RoomPresenterImpl
import com.github.shchurov.gitterclient.presentation.presenters.implementations.RoomsListPresenterImpl
import com.github.shchurov.gitterclient.presentation.ui.LogInView
import com.github.shchurov.gitterclient.presentation.ui.RoomView
import com.github.shchurov.gitterclient.presentation.ui.RoomsListView
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: Activity) {

    //region LogInActivity

    @Provides
    @PerActivity
    fun provideLogInView(): LogInView = activity as LogInView

    @Provides
    @PerActivity
    fun provideGetTokenInteractor(gitterApi: GitterApi): GetTokenInteractor {
        return GetTokenInteractorImpl(gitterApi)
    }

    @Provides
    @PerActivity
    fun provideLogInPresenter(view: LogInView, preferences: Preferences, getTokenInteractor: GetTokenInteractor)
            : LogInPresenter {
        return LogInPresenterImpl(view, preferences, getTokenInteractor)
    }

    //endregion

    //region RoomsListActivity

    @Provides
    @PerActivity
    fun provideRoomsListView(): RoomsListView = activity as RoomsListView

    @Provides
    @PerActivity
    fun provideGetRoomsInteractor(gitterApi: GitterApi, database: Database): GetRoomsInteractor {
        return GetRoomsInteractorImpl(gitterApi, database)
    }

    @Provides
    @PerActivity
    fun provideRoomsListPresenter(view: RoomsListView, getRoomsInteractor: GetRoomsInteractor): RoomsListPresenter {
        return RoomsListPresenterImpl(view, getRoomsInteractor)
    }

    //endregion

    //region RoomActivity

    @Provides
    @PerActivity
    fun provideRoomView(): RoomView = activity as RoomView

    @Provides
    @PerActivity
    fun provideGetRoomMessagesInteractor(gitterApi: GitterApi): GetRoomMessagesInteractor {
        return GetRoomMessagesInteractorImpl(gitterApi)
    }

    @Provides
    @PerActivity
    fun provideRoomPresenter(view: RoomView, getRoomsMessagesInteractor: GetRoomMessagesInteractor)
            : RoomPresenter {
        return RoomPresenterImpl(view, getRoomsMessagesInteractor)
    }

    //endregion

}