package com.example.androidlabs.businessLogic;

import android.widget.Toast;

import com.example.androidlabs.MainActivity;
import com.example.androidlabs.R;
import com.example.androidlabs.dataAccess.entities.User;
import com.example.androidlabs.dataAccess.roomdDb.AppDatabase;
import com.example.androidlabs.dataAccess.roomdDb.entities.UserAdditionalInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

public class AuthentificationService {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private MainActivity mainActivity;
    private NavController navController;
    private AppDatabase appAdditionalInfoDatabase;

    public AuthentificationService(MainActivity activity, NavController controller, AppDatabase db) {
        mainActivity = activity;
        navController = controller;
        mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        appAdditionalInfoDatabase = db;
    }

    public boolean isUserSignedIn() {
        return currentUser != null;
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public User getCurrentUserAsUser() {
        FirebaseUser userCredentials = mAuth.getCurrentUser();

        UserAdditionalInfo userAdditionalInfo = appAdditionalInfoDatabase.userDAdditionalInfo().getUserAdditionalInfo(userCredentials.getUid().toString());

        return new User(
                userCredentials.getUid(),
                userAdditionalInfo.name,
                userAdditionalInfo.surname,
                userCredentials.getEmail(),
                userAdditionalInfo.phoneNumber,
                userAdditionalInfo.pathToPhoto
        );

    }

    public FirebaseAuth getFirebaeAuth(){
        return mAuth;
    }

    public void signInUser(
            String email, String password
    ) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mainActivity.getApplicationContext(), "You are authenticated.",
                                    Toast.LENGTH_SHORT).show();

                            mainActivity.updateCurrentUser(getCurrentUserAsUser());

                            navController.navigate(R.id.profileFragment);

                        } else {
                            Toast.makeText(mainActivity.getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void logout() {
        mAuth.signOut();
    }
}
