package com.example.androidlabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class signInFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private EditText userEmailEditText;
    private EditText userPasswordEditText;

    public signInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        userEmailEditText = view.findViewById(R.id.emailSignInTextInput);
        userPasswordEditText = view.findViewById(R.id.passwordSignInTextInput);

        Button createUserButton = view.findViewById(R.id.moveToCreateUserFragmentButton);
        createUserButton.setOnClickListener(this);
        Button signInUserButton = view.findViewById(R.id.signInUserButton);
        signInUserButton.setOnClickListener(this);

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
            case R.id.moveToCreateUserFragmentButton:
                mListener.moveToCreateUserDestination();
                break;
            case R.id.signInUserButton:
                mListener.signInUser(
                    userEmailEditText.getText().toString(),
                    userPasswordEditText.getText().toString()
                );
                break;
        }


    }

    public interface OnFragmentInteractionListener {
        void moveToCreateUserDestination();
        void signInUser(String email, String password);
    }
}
