package com.example.androidlabs.businessLogic;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.example.androidlabs.MainActivity;
import com.example.androidlabs.R;
import com.example.androidlabs.dataAccess.roomdDb.AppDatabase;
import com.example.androidlabs.dataAccess.roomdDb.entities.UserAdditionalInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

public class UserManagementServicece {

    private MainActivity mainActivity;
    private NavController navController;

    public int nextDestinationId;
    private AppDatabase db;
    private AuthentificationService auth;

    public UserManagementServicece(MainActivity mainActivity, NavController navController, AppDatabase db){
        this.mainActivity = mainActivity;
        this.navController = navController;
        this.db = db;
        auth = new AuthentificationService(mainActivity, navController, this.db);
    }


    public void createUser(
            final String email, final String password,
            final String name, final String surname,
            final String phoneNumber, final Bitmap photo
    ) {

        auth.getFirebaeAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String uid = auth.getFirebaeAuth().getCurrentUser().getUid();

                            UserAdditionalInfo newUserAdditionalInfo = new UserAdditionalInfo();
                            newUserAdditionalInfo.uid = uid;
                            newUserAdditionalInfo.name = name;
                            newUserAdditionalInfo.surname = surname;
                            newUserAdditionalInfo.phoneNumber = phoneNumber;
                            String pathToPhoto = saveToInternalStorage(photo);
                            newUserAdditionalInfo.pathToPhoto = pathToPhoto;
                            db.userDAdditionalInfo().addUserAdditionalInfo(newUserAdditionalInfo);

                            mainActivity.updateCurrentUser(auth.getCurrentUserAsUser());

                            navController.navigate(R.id.profileFragment);

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

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(mainActivity.getApplicationContext());

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





    public void updateUser(
            String uid, String email, String password,
            String name, String surname, String phoneNumber, Bitmap photo
    ){
        auth.getFirebaeAuth().getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(mainActivity.getApplicationContext(), task.getException().toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (!password.isEmpty()) {
            auth.getFirebaeAuth().getCurrentUser().updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(mainActivity.getApplicationContext(), task.getException().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        UserAdditionalInfo userAdditionalInfo = new UserAdditionalInfo();
        userAdditionalInfo.name = name;
        userAdditionalInfo.email = email;
        userAdditionalInfo.password = password;
        userAdditionalInfo.surname = surname;
        userAdditionalInfo.phoneNumber = phoneNumber;
        String pathtoPhoto = saveToInternalStorage(photo);
        userAdditionalInfo.pathToPhoto = pathtoPhoto;
        userAdditionalInfo.uid = uid;
        db.userDAdditionalInfo().updateUserAdditionalInfo(userAdditionalInfo);
        navController.navigate(R.id.profileFragment);
    }

}
