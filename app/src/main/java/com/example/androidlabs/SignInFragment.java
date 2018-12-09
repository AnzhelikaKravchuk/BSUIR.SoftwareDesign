package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


public class SignInFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private EditText userEmailEditText;
    private EditText userPasswordEditText;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        setupUserEmailEditText((EditText) view.findViewById(R.id.emailSignInTextInput));
        setupUserPasswordEditText((EditText) view.findViewById(R.id.passwordSignInTextInput));

        Button createUserButton = view.findViewById(R.id.moveToCreateUserFragmentButton);
        createUserButton.setOnClickListener(this);
        Button signInUserButton = view.findViewById(R.id.signInUserButton);
        signInUserButton.setOnClickListener(this);

        final RadioGroup showPasswordRadioGroup = view.findViewById(R.id.showPasswordRadioGroup);
        showPasswordRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.showPasswordRadioButton:
                        userPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    case R.id.hidePasswordRadioButton:
                        userPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                }
            }
        });
        return view;
    }

    private void setupUserEmailEditText(final EditText userEmailEditText){
        this.userEmailEditText = userEmailEditText;

        this.userEmailEditText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (!Patterns.EMAIL_ADDRESS.matcher(
                        userEmailEditText.getText().toString()
                ).matches()) {
                    userEmailEditText.setError("Enter valid email");
                } else {
                    userEmailEditText.setError(null);
                }
            }
        });
    }

    private void setupUserPasswordEditText(final EditText userPasswordEditText){
        this.userPasswordEditText = userPasswordEditText;
        this.userPasswordEditText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (userPasswordEditText.getText().toString().length() < 6) {
                    userPasswordEditText.setError("Enter at least 6 sumbols");
                } else {
                    userPasswordEditText.setError(null);
                }


            }
        });
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
                if (userEmailEditText.getText().toString().length() == 0){
                    userEmailEditText.setError("required");
                }
                if (userEmailEditText.getError() != null) {
                    userEmailEditText.requestFocus();
                    return;
                }
                if (userPasswordEditText.getText().toString().length() == 0){
                    userPasswordEditText.setError("required");
                }
                if (userPasswordEditText.getError() != null){
                    userPasswordEditText.requestFocus();
                    return;
                }

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