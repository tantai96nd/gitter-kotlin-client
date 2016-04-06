package com.github.shchurov.gitterclient.dagger.modules

import android.support.v7.app.AppCompatActivity
import com.github.shchurov.gitterclient.dagger.scopes.PerScreen
import dagger.Module
import dagger.Provides

@Module
class GeneralScreenModule(
        private val activity: AppCompatActivity
) {

    @Provides
    @PerScreen
    fun provideActivity() = activity

}