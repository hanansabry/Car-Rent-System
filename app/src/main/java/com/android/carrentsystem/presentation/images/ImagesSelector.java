package com.android.carrentsystem.presentation.images;

import android.net.Uri;

public abstract class ImagesSelector {

    public interface ImagesSelectorCallback {

        void onImageAdded(Uri imageUri, int position);

        void onImageSelectedError(String error);

    }

}
