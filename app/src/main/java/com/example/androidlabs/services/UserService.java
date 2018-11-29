package com.example.androidlabs.services;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.example.androidlabs.MainActivity;
import com.example.androidlabs.models.User;
import com.example.androidlabs.roomdDb.AppDatabase;
import com.example.androidlabs.roomdDb.entities.UserAdditionalInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

public class UserService {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static boolean isUserSignedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public static User getCurrentUser(AppDatabase appDatabase) {
        FirebaseUser userCredentials = mAuth.getCurrentUser();

        UserAdditionalInfo userAdditionalInfo = appDatabase.userDAdditionalInfo().getUserAdditionalInfo(userCredentials.getUid().toString());

        return new User(
                userCredentials.getUid(),
                userAdditionalInfo.name,
                userAdditionalInfo.surname,
                userCredentials.getEmail(),
                userAdditionalInfo.phoneNumber,
                userAdditionalInfo.pathToPhoto
        );

    }

    public static void createUser(
            final String email, final String password,
            final String name, final String surname,
            final String phoneNumber, final Bitmap photo,
            final MainActivity mainActivity, final NavController navController, final int nextDestinationId,
            final AppDatabase appDatabase
    ) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid().toString();

                            UserAdditionalInfo newUserAdditionalInfo = new UserAdditionalInfo();
                            newUserAdditionalInfo.uid = uid;
                            newUserAdditionalInfo.name = name;
                            newUserAdditionalInfo.surname = surname;
                            newUserAdditionalInfo.phoneNumber = phoneNumber;
                            String pathToPhoto = saveToInternalStorage(photo, mainActivity);
                            newUserAdditionalInfo.pathToPhoto = pathToPhoto;
                            appDatabase.userDAdditionalInfo().addUserAdditionalInfo(newUserAdditionalInfo);

                            mainActivity.updateCurrentUser(UserService.getCurrentUser(appDatabase));

                            navController.navigate(nextDestinationId);

                        } else {
                            Toast.makeText(
                                mainActivity.getApplicationContext(),
                                "Creation failed." + task.getException().toString(),
                                Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
    }

    private static String saveToInternalStorage(Bitmap bitmapImage, Activity activity) {
        ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());

        File directory = cw.getDir("profilePhotosDirectory", Context.MODE_PRIVATE);

        File myPath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static void signInUser(
            String email, String password, final MainActivity mainActivity,
            final NavController navController, final int nextDestinationId,
            final AppDatabase appDatabase
    ) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mainActivity.updateCurrentUser(UserService.getCurrentUser(appDatabase));

                            navController.navigate(nextDestinationId);

                        } else {
                            Toast.makeText(mainActivity.getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public static void logout() {
        mAuth.signOut();
    }


}

