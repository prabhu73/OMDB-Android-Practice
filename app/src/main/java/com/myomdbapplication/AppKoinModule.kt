package com.myomdbapplication

import com.myomdbapplication.db.omdbModule
import com.myomdbapplication.repository.restServiceModule
import com.myomdbapplication.ui.omdbViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.Module

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class AppKoinModule {
    companion object {
        fun getModules(): List<Module> {
            return mutableListOf(restServiceModule, omdbViewModel, omdbModule)
        }
    }
}