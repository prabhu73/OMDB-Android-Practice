package com.myomdbapplication.di

import com.myomdbapplication.AppKoinModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.Module

class TestKoinModule {
    companion object {
        @ExperimentalCoroutinesApi
        @InternalCoroutinesApi
        fun getModules(): List<Module> =
            AppKoinModule.getModules().toMutableList()
    }
}