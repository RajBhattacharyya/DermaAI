package com.example.dermaai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_IMAGE_RESOURCE = "image_resource";
    private static final String ARG_DETAIL = "detail";
    private String title;
    private int imageResource;
    private String detail;

    public static MyFragment newInstance(String title, int imageResource, String detail) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_IMAGE_RESOURCE, imageResource);
        args.putString(ARG_DETAIL, detail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            imageResource = getArguments().getInt(ARG_IMAGE_RESOURCE);
            detail = getArguments().getString(ARG_DETAIL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        TextView textViewTitle = rootView.findViewById(R.id.textViewTitle);
        ImageView imageViewContent = rootView.findViewById(R.id.imageViewContent);
        TextView textViewDescription = rootView.findViewById(R.id.textViewDescription);

        textViewTitle.setText(title);
        imageViewContent.setImageResource(imageResource);
        textViewDescription.setText(detail);

        return rootView;
    }
}
