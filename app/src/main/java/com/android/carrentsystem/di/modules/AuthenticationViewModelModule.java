package com.android.carrentsystem.di.modules;

import com.android.carrentsystem.di.ViewModelKey;
import com.android.carrentsystem.presentation.viewmodels.AuthenticationViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthenticationViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel.class)
    public abstract ViewModel bindViewModel(AuthenticationViewModel viewModel);
}
