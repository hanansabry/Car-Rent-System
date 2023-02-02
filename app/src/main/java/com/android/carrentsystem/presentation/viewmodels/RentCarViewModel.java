package com.android.carrentsystem.presentation.viewmodels;

import android.net.Uri;

import com.android.carrentsystem.data.DatabaseRepository;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.CarCategory;
import com.android.carrentsystem.data.models.Color;
import com.android.carrentsystem.data.models.RentOrder;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RentCarViewModel extends ViewModel {

    private final DatabaseRepository databaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MediatorLiveData<String> uploadImageState = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> addOrderStateLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<String> errorState = new MediatorLiveData<>();

    @Inject
    public RentCarViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public void uploadImageToFirebaseStorage(Uri imageUri, String agencyName, String civilId) {
        String folderName = "orders" + "/" + agencyName + "/" + civilId;
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

    public void addNewOrder(RentOrder order) {
        SingleObserver<Boolean> singleObserver = databaseRepository.addNewRentOrder(order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        addOrderStateLiveData.setValue(success);
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

    public MediatorLiveData<String> observeErrorState() {
        return errorState;
    }
}
