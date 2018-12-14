package com.example.androidlabs;
import androidx.annotation.NonNull;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidlabs.businessLogic.UserManagementService;
import com.example.androidlabs.businessLogic.models.User;

public class IndexFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        User currentUser = UserManagementService.getInstance().getCurrentUser();
        if(currentUser == null){
            mListener.navigateToSignInDestination(R.id.indexFragment);
        }
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

    public interface OnFragmentInteractionListener {
        void navigateToSignInDestination(int nextDestinationId);
    }


}