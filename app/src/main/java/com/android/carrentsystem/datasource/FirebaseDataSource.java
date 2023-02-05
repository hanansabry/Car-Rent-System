package com.android.carrentsystem.datasource;

import android.net.Uri;

import com.android.carrentsystem.data.models.Agency;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.CarCategory;
import com.android.carrentsystem.data.models.Color;
import com.android.carrentsystem.data.models.RentDate;
import com.android.carrentsystem.data.models.RentOrder;
import com.android.carrentsystem.data.models.Violation;
import com.android.carrentsystem.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class FirebaseDataSource {

    private final StorageReference storageReference;
    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseAuth firebaseAuth;
    private SharedPreferencesDataSource sharedPreferencesDataSource;

    @Inject
    public FirebaseDataSource(StorageReference storageReference
            , FirebaseDatabase firebaseDatabase
            , FirebaseAuth firebaseAuth
            , FirebaseMessaging firebaseMessaging
            , SharedPreferencesDataSource sharedPreferencesDataSource) {
        this.storageReference = storageReference;
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
        this.sharedPreferencesDataSource = sharedPreferencesDataSource;
    }

    public Single<Agency> loginAsAgency(String email, String password) {
        return Single.create(emitter -> {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String agencyId = task.getResult().getUser().getUid();
                            DatabaseReference userRef = firebaseDatabase.getReference(Constants.AGENCIES_NODE).child(agencyId);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Agency user = snapshot.getValue(Agency.class);
                                    if (user != null) {
                                        user.setId(agencyId);
                                        emitter.onSuccess(user);
                                    } else {
                                        emitter.onError(new Throwable("no user record"));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    emitter.onError(error.toException());
                                }
                            });
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    public Single<Agency> registerNewAgency(Agency agency) {
        return Single.create(emitter -> firebaseAuth.createUserWithEmailAndPassword(agency.getEmail(), agency.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String agencyId = task.getResult().getUser().getUid();
                        //get device token
                        DatabaseReference usersRef = firebaseDatabase.getReference(Constants.AGENCIES_NODE);
                        agency.setId(agencyId);
                        usersRef.child(agencyId).setValue(agency).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                emitter.onSuccess(agency);
                            } else {
                                emitter.onError(task1.getException());
                            }
                        });

                    } else {
                        emitter.onError(task.getException());
                    }
                }));
    }

    public Flowable<List<CarCategory>> retrieveCarCategories() {
        return Flowable.create(emitter -> {
            DatabaseReference carsRef = firebaseDatabase.getReference(Constants.CARS).child(Constants.CATEGORIES);
            carsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<CarCategory> categoryList = new ArrayList<>();
                    for (DataSnapshot categorySnapShot : snapshot.getChildren()) {
                        CarCategory carCategory = categorySnapShot.getValue(CarCategory.class);
                        categoryList.add(carCategory);
                    }
                    emitter.onNext(categoryList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        }, BackpressureStrategy.BUFFER);
    }

    public Single<String> uploadImageToFirebaseStorage(Uri imageUri, String folderName) {
        return Single.create(emitter -> {
            StorageReference reference = storageReference.child(folderName + "/" + System.currentTimeMillis() + Constants.IMAGE_FILE_TYPE);
            UploadTask uploadFileTask = reference.putFile(imageUri);
            uploadFileTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    emitter.onError(task.getException());
                } else {
                    // Continue with the fileTask to get the download URL
                    reference.getDownloadUrl().addOnCompleteListener(task1 -> {
                        String downloadUrl = task1.getResult().toString();
                        emitter.onSuccess(downloadUrl);
                    });
                }
                return reference.getDownloadUrl();
            });
        });
    }

    public Single<Boolean> addNewCar(String agencyId, Car car) {
        return Single.create(emitter -> {
            //add car firstly to available cars node
            String carId = firebaseDatabase.getReference(Constants.AVAILABLE_CARS).push().getKey();
            firebaseDatabase.getReference(Constants.AVAILABLE_CARS)
                    .child(carId)
                    .setValue(car)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            firebaseDatabase.getReference(Constants.AGENCIES_NODE)
                                    .child(agencyId)
                                    .child(Constants.CARS)
                                    .child(carId)
                                    .setValue(car)
                                    .addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            emitter.onSuccess(true);
                                        } else {
                                            emitter.onSuccess(false);
                                        }
                                    });
                        } else {
                            emitter.onSuccess(false);
                        }
                    });
        });
    }

    public Flowable<List<Color>> retrieveColors() {
        return Flowable.create(emitter -> {
            DatabaseReference carsRef = firebaseDatabase.getReference(Constants.CARS).child(Constants.COLORS);
            carsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Color> colorList = new ArrayList<>();
                    for (DataSnapshot colorSnapShot : snapshot.getChildren()) {
                        Color color = new Color();
                        color.setColorName(colorSnapShot.getKey());
                        color.setColorCode(colorSnapShot.getValue(String.class));
                        colorList.add(color);
                    }
                    emitter.onNext(colorList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<Car>> retrieveSearchCarResults(String category,
                                                        String type,
                                                        String model,
                                                        String year,
                                                        long from,
                                                        long to) {
        return Flowable.create(emitter -> {
            DatabaseReference carsRef = firebaseDatabase.getReference(Constants.AVAILABLE_CARS);
            carsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Car> carList = new ArrayList<>();
                    for (DataSnapshot carSnapshot : snapshot.getChildren()) {
                        Car car = carSnapshot.getValue(Car.class);
                        car.setId(carSnapshot.getKey());
                        if (car.getCategory().equals(category)
                                && car.getType().equals(type)
                                && car.getModel().equals(model)
                                && car.getYear().equals(year)
                        ) {
                            if (car.getStatus().equals(Car.CarStatus.AVAILABLE.value)) {
                                carList.add(car);
                            } else if (car.getStatus().equals(Car.CarStatus.RENTED.value)) {
                                HashMap<String, RentDate> rentDates = car.getRentDates();
                                if (rentDates != null) {
                                    boolean isAvailable = false;
                                    for (String id : rentDates.keySet()) {
                                        RentDate rentDate = rentDates.get(id);
                                        if (from < rentDate.getFrom() && to < rentDate.getFrom() ||
                                                from > rentDate.getTo()) {
                                            isAvailable = true;
                                        } else {
                                            isAvailable = false;
                                            break;
                                        }
                                    }
                                    if (isAvailable) {
                                        carList.add(car);
                                    }
                                } else {
                                    carList.add(car);
                                }
                            }
                        }
                    }
                    emitter.onNext(carList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        }, BackpressureStrategy.BUFFER);
    }

    public Single<Boolean> addNewOrder(RentOrder order) {
        return Single.create(emitter -> {
            //add car firstly to available cars node
            firebaseDatabase.getReference(Constants.ORDERS)
                    .push()
                    .setValue(order)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                    });
        });
    }

    public Flowable<List<RentOrder>> retrieveAgencyOrders(String agencyId) {
        return Flowable.create(emitter -> {
            Query ordersRef = firebaseDatabase.getReference(Constants.ORDERS).orderByChild("agencyId").equalTo(agencyId);
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<RentOrder> orderList = new ArrayList<>();
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        RentOrder order = orderSnapshot.getValue(RentOrder.class);
                        order.setId(orderSnapshot.getKey());
                        orderList.add(order);
                    }
                    emitter.onNext(orderList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        }, BackpressureStrategy.BUFFER);
    }

    public Single<Boolean> addStatusToRentOrder(String orderId, String agencyNotes, boolean isConfirmed) {
        return Single.create(emitter -> {
            //add car firstly to available cars node
            DatabaseReference orderRef = firebaseDatabase.getReference(Constants.ORDERS).child(orderId);
            HashMap<String, Object> updates = new HashMap<>();
            if (isConfirmed) {
                updates.put("status", RentOrder.RentOrderStatus.PROCESSING.value);
            } else {
                updates.put("status", RentOrder.RentOrderStatus.REJECTED.value);
            }
            updates.put("agencyNotes", agencyNotes);
            orderRef.updateChildren(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                    });
        });
    }

    public Single<Boolean> confirmRentOrder(RentOrder rentOrder) {
        return Single.create(emitter -> {
            //change order status
            DatabaseReference orderRef = firebaseDatabase.getReference(Constants.ORDERS).child(rentOrder.getId());
            HashMap<String, Object> updates = new HashMap<>();
            updates.put("status", RentOrder.RentOrderStatus.CONFIRMED.value);
            orderRef.updateChildren(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //change car status in cars node
                            DatabaseReference carRef = firebaseDatabase.getReference(Constants.AVAILABLE_CARS)
                                    .child(rentOrder.getSelectedCar().getId());

                            RentDate rentDate = new RentDate();
                            rentDate.setFrom(rentOrder.getFrom());
                            rentDate.setTo(rentOrder.getTo());
                            rentDate.setDaysNum(rentOrder.getNumDays());
                            carRef.child("rentDates").push().setValue(rentDate);

                            HashMap<String, Object> carUpdates = new HashMap<>();
                            carUpdates.put("status", Car.CarStatus.RENTED.value);
                            carRef.updateChildren(carUpdates).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    emitter.onSuccess(true);
                                }
                            });
                        } else {
                            emitter.onSuccess(false);
                        }
                    });
        });
    }

    public Flowable<RentOrder> retrieveOrdersByPhone(String phone) {
        return Flowable.create(emitter -> {
            Query ordersRef = firebaseDatabase.getReference(Constants.ORDERS).orderByChild("phone").equalTo(phone).limitToFirst(1);
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            RentOrder order = orderSnapshot.getValue(RentOrder.class);
                            order.setId(orderSnapshot.getKey());
                            emitter.onNext(order);
                            emitter.onComplete();
                        }
                    } else {
                        emitter.onError(new Throwable(Constants.NO_AVAILABLE_ORDERS));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<Car>> retrieveAgencyCars(String agencyId) {
        return Flowable.create(emitter -> {
            Query ordersRef = firebaseDatabase.getReference(Constants.AVAILABLE_CARS).orderByChild("agencyId").equalTo(agencyId);
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Car> carList = new ArrayList<>();
                    for (DataSnapshot carSnapshot : snapshot.getChildren()) {
                        Car car = carSnapshot.getValue(Car.class);
                        car.setId(carSnapshot.getKey());
                        carList.add(car);
                    }
                    emitter.onNext(carList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<Car>> retrieveAllCars() {
        return Flowable.create(emitter -> {
            Query ordersRef = firebaseDatabase.getReference(Constants.AVAILABLE_CARS);
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Car> carList = new ArrayList<>();
                    for (DataSnapshot carSnapshot : snapshot.getChildren()) {
                        Car car = carSnapshot.getValue(Car.class);
                        car.setId(carSnapshot.getKey());
                        carList.add(car);
                    }
                    emitter.onNext(carList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        }, BackpressureStrategy.BUFFER);
    }

    public Single<Boolean> addNewViolation(String carId, Violation violation) {
        return Single.create(emitter -> {
            //add car firstly to available cars node
            firebaseDatabase.getReference(Constants.AVAILABLE_CARS)
                    .child(carId)
                    .child("violationList")
                    .push()
                    .setValue(violation)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                    });
        });
    }

    public Single<Boolean> setViolationsDone(String carId) {
        return Single.create(emitter -> {
            //set car available again
            HashMap<String, Object> updates = new HashMap<>();
            updates.put("status", Car.CarStatus.AVAILABLE.value);
            updates.put("violationList", null);
            firebaseDatabase.getReference(Constants.AVAILABLE_CARS)
                    .child(carId)
                    .updateChildren(updates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //get orders for this car
                            //set order of this car is returned
                            if (task.isSuccessful()) {
                                firebaseDatabase.getReference(Constants.ORDERS)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                                                    RentOrder order = orderSnapshot.getValue(RentOrder.class);
                                                    if (order.getSelectedCar().getId().equals(carId)) {
                                                        orderSnapshot.getRef().child("status").setValue(RentOrder.RentOrderStatus.RETURNED.value);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                emitter.onError(error.toException());
                                            }
                                        });
                            } else {
                                emitter.onError(task.getException());
                            }
                        }
                    });
        });
    }
}
