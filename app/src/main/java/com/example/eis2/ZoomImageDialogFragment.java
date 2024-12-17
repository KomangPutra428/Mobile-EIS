package com.example.eis2;

import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import com.bumptech.glide.load.model.LazyHeaders;
import com.github.chrisbanes.photoview.PhotoView;

public class ZoomImageDialogFragment extends DialogFragment {

    private static final String ARG_IMAGE_URL = "imageUrl";

    public static ZoomImageDialogFragment newInstance(String imageUrl) {
        ZoomImageDialogFragment fragment = new ZoomImageDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_zoom_image, container, false);
        PhotoView photoView = rootView.findViewById(R.id.photo_view);

        Bundle args = getArguments();
        if (args != null) {
            String imageUrl = args.getString(ARG_IMAGE_URL);
            loadZoomedImage(imageUrl, photoView);
        }

        return rootView;
    }

    private void loadZoomedImage(String imageUrl, PhotoView photoView) {
        LazyHeaders auth = new LazyHeaders.Builder() // can be cached in a field and reused
                .addHeader("Authorization", new BasicAuthorization("admin", "Databa53"))
                .build();

        Glide.with(requireContext())
                .load(new GlideUrl(imageUrl, auth))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(photoView);
    }

    public class BasicAuthorization implements LazyHeaderFactory {
        private final String username;
        private final String password;

        public BasicAuthorization(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override public String buildHeader() {
            return "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
        }
    }
}
