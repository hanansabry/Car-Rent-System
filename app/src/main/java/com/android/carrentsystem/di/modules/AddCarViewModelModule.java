package com.android.carrentsystem.di.modules;

import com.android.carrentsystem.di.ViewModelKey;
import com.android.carrentsystem.presentation.viewmodels.AddCarViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AddCarViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddCarViewModel.class)
    public abstract ViewModel bindViewModel(AddCarViewModel viewModel);
}
