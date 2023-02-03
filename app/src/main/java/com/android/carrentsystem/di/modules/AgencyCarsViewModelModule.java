package com.android.carrentsystem.di.modules;

import com.android.carrentsystem.di.ViewModelKey;
import com.android.carrentsystem.presentation.viewmodels.AddCarViewModel;
import com.android.carrentsystem.presentation.viewmodels.AgencyCarsViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AgencyCarsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AgencyCarsViewModel.class)
    public abstract ViewModel bindViewModel(AgencyCarsViewModel viewModel);
}
