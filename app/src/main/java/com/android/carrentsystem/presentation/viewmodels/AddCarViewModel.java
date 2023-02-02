package com.android.carrentsystem.presentation.viewmodels;

import android.net.Uri;

import com.android.carrentsystem.data.DatabaseRepository;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.CarCategory;
import com.android.carrentsystem.data.models.Color;

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

public class AddCarViewModel extends ViewModel {

    private final DatabaseRepository databaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MediatorLiveData<String> uploadImageState = new MediatorLiveData<>();
    private final MediatorLiveData<List<CarCategory>> retrieveCarCategoriesLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<List<Color>> colorListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> addCarStateLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<String> errorState = new MediatorLiveData<>();

    @Inject
    public AddCarViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public void retrieveCarCategories() {
        databaseRepository.retrieveCarCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<List<CarCategory>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<CarCategory> categoryList) {
                        retrieveCarCategoriesLiveData.setValue(categoryList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        retrieveCarCategoriesLiveData.setValue(null);
                    }
                });
    }

    public void retrieveColors() {
        databaseRepository.retrieveColors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<List<Color>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Color> colorList) {
                        colorListLiveData.setValue(colorList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        retrieveCarCategoriesLiveData.setValue(null);
                    }
                });
    }

    public void uploadImageToFirebaseStorage(Uri imageUri, String agencyId, String platNum) {
        String folderName = "cars" + "/" + agencyId + "/" + platNum;
        SingleObserver<String> singleObserver = databaseRepository.uploadImageToFirebaseStorage(imageUri, folderName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(String downloadUrl) {
                        uploadImageState.setValue(downloadUrl);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void addNewCar(String agencyId, Car car) {
        SingleObserver<Boolean> singleObserver = databaseRepository.addNewCar(agencyId, car)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        addCarStateLiveData.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public MediatorLiveData<String> observeUploadImageState() {
        return uploadImageState;
    }

    public MediatorLiveData<List<CarCategory>> observeRetrieveCarCategoriesLiveData() {
        return retrieveCarCategoriesLiveData;
    }

    public MediatorLiveData<List<Color>> observeColorListLiveData() {
        return colorListLiveData;
    }

    public MediatorLiveData<Boolean> observeAddCarStateLiveData() {
        return addCarStateLiveData;
    }

    public MediatorLiveData<String> observeErrorState() {
        return errorState;
    }
}
