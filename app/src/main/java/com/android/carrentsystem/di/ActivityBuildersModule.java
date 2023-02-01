package com.android.carrentsystem.di;


import com.android.carrentsystem.di.modules.AddCarViewModelModule;
import com.android.carrentsystem.di.modules.AuthenticationViewModelModule;
import com.android.carrentsystem.presentation.SplashActivity;
import com.android.carrentsystem.presentation.agency.AddCarActivity;
import com.android.carrentsystem.presentation.start.LoginActivity;
import com.android.carrentsystem.presentation.start.RegisterActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector(modules = AuthenticationViewModelModule.class)
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector(modules = AuthenticationViewModelModule.class)
    abstract RegisterActivity contributeRegisterActivity();

    @ContributesAndroidInjector(modules = AddCarViewModelModule.class)
    abstract AddCarActivity contributeAddCarActivity();
}
