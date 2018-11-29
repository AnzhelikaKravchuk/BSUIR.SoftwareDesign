package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class profileFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;

    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView surnameTextView = view.findViewById(R.id.surnameTextView);
        TextView phoneNumberTextView = view.findViewById(R.id.phoneNumberTextView);
        TextView emailTextView = view.findViewById(R.id.emailTextView);

        ImageView photoImageView = view.findViewById(R.id.photoImageView);

        nameTextView.setText(MainActivity.currentUser.name);
        surnameTextView.setText(MainActivity.currentUser.surname);
        phoneNumberTextView.setText(MainActivity.currentUser.phoneNumber);
        emailTextView.setText(MainActivity.currentUser.email);
        photoImageView.setImageBitmap(MainActivity.currentUser.loadImageFromStorage());

        Button navigateToEditProfileButton = view.findViewById(R.id.navigateToEditProfileButton);
        navigateToEditProfileButton.setOnClickListener(this);

         return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigateToEditProfileButton:
                mListener.navigateToEditProfile();
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        void navigateToEditProfile();
    }
}
