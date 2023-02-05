package com.android.carrentsystem.presentation.viewmodels;

import com.android.carrentsystem.data.DatabaseRepository;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.CarCategory;
import com.android.carrentsystem.data.models.RentOrder;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AgencyCarsViewModel extends ViewModel {

    private final DatabaseRepository databaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MediatorLiveData<List<Car>> carListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> violationsDoneState = new MediatorLiveData<>();
    private final MediatorLiveData<String> errorState = new MediatorLiveData<>();

    @Inject
    public AgencyCarsViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public void retrieveAgencyCars(String agencyId) {
        databaseRepository.retrieveAgencyCars(agencyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<List<Car>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Car> carList) {
                        carListLiveData.setValue(carList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        carListLiveData.setValue(null);
                    }
                });
    }

    public void setViolationsDone(String carId) {
        SingleObserver<Boolean> singleObserver = databaseRepository.setViolationsDone(carId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        violationsDoneState.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public MediatorLiveData<List<Car>> observeCarListLiveData() {
        return carListLiveData;
    }

    public MediatorLiveData<Boolean> observeViolationsDoneState() {
        return violationsDoneState;
    }

    public MediatorLiveData<String> observeErrorState() {
        return errorState;
    }
}
