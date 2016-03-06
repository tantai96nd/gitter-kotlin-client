package com.github.shchurov.gitterclient.dagger.modules

import android.app.Activity
import com.github.shchurov.gitterclient.dagger.scopes.PerActivity
import com.github.shchurov.gitterclient.data.Preferences
import com.github.shchurov.gitterclient.data.database.Database
import com.github.shchurov.gitterclient.data.network.GitterApi
import com.github.shchurov.gitterclient.domain.interactors.*
import com.github.shchurov.gitterclient.domain.interactors.implementation.*
import com.github.shchurov.gitterclient.domain.interactors.threading.SchedulersProvider
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
    fun provideGetTokenInteractor(gitterApi: GitterApi, preferences: Preferences,
            schedulersProvider: SchedulersProvider): GitterLogInInteractor {
        return GitterLogInInteractorImpl(gitterApi, preferences, schedulersProvider)
    }

    @Provides
    @PerActivity
    fun provideLogInPresenter(view: LogInView, preferences: Preferences, gitterLogInInteractor: GitterLogInInteractor)
            : LogInPresenter {
        return LogInPresenterImpl(view, preferences, gitterLogInInteractor)
    }

    //endregion

    //region RoomsListActivity

    @Provides
    @PerActivity
    fun provideRoomsListView(): RoomsListView = activity as RoomsListView

    @Provides
    @PerActivity
    fun provideGetRoomsInteractor(gitterApi: GitterApi, database: Database, schedulersProvider: SchedulersProvider)
            : GetRoomsInteractor {
        return GetRoomsInteractorImpl(gitterApi, database, schedulersProvider)
    }

    @Provides
    @PerActivity
    fun provideUpdateRoomLastAccessTimeInteractor(database: Database): UpdateRoomLastAccessTimeInteractor {
        return UpdateRoomLastAccessTimeInteractorImpl(database)
    }

    @Provides
    @PerActivity
    fun provideRoomsListPresenter(view: RoomsListView, getRoomsInteractor: GetRoomsInteractor,
            updateRoomLastAccessTimeInteractor: UpdateRoomLastAccessTimeInteractor): RoomsListPresenter {
        return RoomsListPresenterImpl(view, getRoomsInteractor, updateRoomLastAccessTimeInteractor)
    }

    //endregion

    //region RoomActivity

    @Provides
    @PerActivity
    fun provideRoomView(): RoomView = activity as RoomView

    @Provides
    @PerActivity
    fun provideGetRoomMessagesInteractor(gitterApi: GitterApi, schedulersProvider: SchedulersProvider)
            : GetRoomMessagesInteractor {
        return GetRoomMessagesInteractorImpl(gitterApi, schedulersProvider)
    }

    @Provides
    @PerActivity
    fun provideMarkMessagesAsReadInteractor(gitterApi: GitterApi, database: Database,
            schedulersProvider: SchedulersProvider): MarkMessageAsReadInteractor {
        return MarkMessageAsReadInteractorImpl(gitterApi, database, schedulersProvider)
    }

    @Provides
    @PerActivity
    fun provideRoomPresenter(view: RoomView, getRoomsMessagesInteractor: GetRoomMessagesInteractor,
            markMessageAsReadInteractor: MarkMessageAsReadInteractor): RoomPresenter {
        return RoomPresenterImpl(view, getRoomsMessagesInteractor, markMessageAsReadInteractor)
    }

    //endregion

}