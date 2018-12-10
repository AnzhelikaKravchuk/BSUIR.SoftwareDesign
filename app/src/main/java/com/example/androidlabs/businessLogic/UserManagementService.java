package com.example.androidlabs.businessLogic;

import com.example.androidlabs.dataAccess.entities.User;
import com.example.androidlabs.dataAccess.roomdDb.AppDatabase;
import com.example.androidlabs.dataAccess.roomdDb.entities.UserAdditionalInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;

public class UserManagementService {
    private static volatile UserManagementService instance;

    private FirebaseAuth mAuth;
    private User currentUser;
    private FirebaseUser currentFirebaseUser;
    private AppDatabase appAdditionalInfoDatabase;

    public UserManagementService(AppDatabase appAdditionalInfoDatabase) {
        this.appAdditionalInfoDatabase = appAdditionalInfoDatabase;
        mAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = mAuth.getCurrentUser();
        authenticateResultListener = null;
        creationResultListener = null;
        updateResultListener = null;

        if (currentFirebaseUser != null) {
            currentUser = initCurrentUser();
        } else {
            currentUser = null;
        }

        instance = this;
    }

    public static UserManagementService getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private User initCurrentUser() {
        UserAdditionalInfo userAdditionalInfo = appAdditionalInfoDatabase.userDAdditionalInfo()
                .getUserAdditionalInfo(currentFirebaseUser.getUid());

        return new User(
                currentFirebaseUser.getUid(),
                userAdditionalInfo.name,
                userAdditionalInfo.surname,
                currentFirebaseUser.getEmail(),
                userAdditionalInfo.phoneNumber,
                userAdditionalInfo.pathToPhoto,
                userAdditionalInfo.rssNewsUrl
        );
    }

    public boolean isUserSignedIn() {
        return currentUser != null;
    }

    public void createUser(
            final String email, final String password,
            final String name, final String surname,
            final String phoneNumber, final String pathToPhoto
    ) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();

                            UserAdditionalInfo newUserAdditionalInfo = new UserAdditionalInfo();
                            newUserAdditionalInfo.uid = uid;
                            newUserAdditionalInfo.name = name;
                            newUserAdditionalInfo.surname = surname;
                            newUserAdditionalInfo.phoneNumber = phoneNumber;
                            newUserAdditionalInfo.pathToPhoto = pathToPhoto;
                            appAdditionalInfoDatabase.userDAdditionalInfo().addUserAdditionalInfo(newUserAdditionalInfo);
                            currentFirebaseUser = mAuth.getCurrentUser();
                            currentUser = initCurrentUser();
                            creationResultListener.onCreationResultSuccess();
                        } else {
                            creationResultListener.onCreationResultFailed(task.getException().toString());
                        }
                    }
                });
    }

    public void logout() {
        mAuth.signOut();
    }


    private OnAuthenticateResultListener authenticateResultListener;

    private OnUpdateResultListener updateResultListener;

    private OnCreationResultListener creationResultListener;

    private OnConfirmResultListener onConfirmResultListener;

    public interface OnUpdateResultListener {
        void OnUpdateResultSuccess();

        void onUpdateFailed(String failedTextException);
    }

    public interface OnCreationResultListener {
        void onCreationResultSuccess();

        void onCreationResultFailed(String exceptionText);
    }

    public interface OnAuthenticateResultListener {
        void onAuthenticateSuccess();

        void onAuthenticateFailed();
    }

    public interface OnConfirmResultListener {
        void onConfirmFailed(String exceptionText);
    }

    public void setOnAuthenticateResultListener(OnAuthenticateResultListener listener) {
        this.authenticateResultListener = listener;
    }

    public void setOnCreationResultListener(OnCreationResultListener listener) {
        this.creationResultListener = listener;
    }

    public void setOnUpdateResultListener(OnUpdateResultListener listener) {
        this.updateResultListener = listener;

    }

    public void setOnConfirmResultListener(OnConfirmResultListener listener) {
        this.onConfirmResultListener = listener;
    }

    public void signInUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentFirebaseUser = mAuth.getCurrentUser();
                            currentUser = initCurrentUser();
                            authenticateResultListener.onAuthenticateSuccess();
                        } else {
                            authenticateResultListener.onAuthenticateFailed();
                        }
                    }
                });
    }



    public void updateUser(
            final String uid, final String email, String password,
            final String name, final String surname, final String phoneNumber,
            final String pathToPhoto, String currentPassword
    ) {
        if (!currentUser.email.equals(email) || !password.isEmpty()) {
            updateUserWithCredentials(
                    email, password, currentPassword, uid, name, surname, phoneNumber, pathToPhoto
            );
        }
        else {
            updateUserAdditionalInfo(uid, name, surname, phoneNumber, pathToPhoto);
            currentUser = initCurrentUser();
            updateResultListener.OnUpdateResultSuccess();
        }

    }

    private void updateUserWithCredentials(
            final String email, final String password, final String currentPassword,
            final String uid, final String name, final String surname, final String phoneNumber,
            final String pathToPhoto
    ) {
        currentFirebaseUser.reauthenticate(
                EmailAuthProvider.getCredential(currentUser.email, currentPassword)
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (!currentUser.email.equals(email)) {
                        currentFirebaseUser.updateEmail(email);
                    }
                    if (!password.isEmpty()) {
                        currentFirebaseUser.updatePassword(password);
                    }
                    updateUserAdditionalInfo(uid, name, surname, phoneNumber, pathToPhoto);
                    currentUser = initCurrentUser();
                    currentUser.email = email;
                    updateResultListener.OnUpdateResultSuccess();
                } else {
                    onConfirmResultListener.onConfirmFailed(task.getException().toString());
                }

            }

        });
    }

    private void updateUserAdditionalInfo(String uid, String name, String surname, String phoneNumber,
                                          String pathToPhoto) {
        UserAdditionalInfo userAdditionalInfo = new UserAdditionalInfo();
        userAdditionalInfo.name = name;
        userAdditionalInfo.surname = surname;
        userAdditionalInfo.phoneNumber = phoneNumber;
        userAdditionalInfo.pathToPhoto = pathToPhoto;
        userAdditionalInfo.uid = uid;
        appAdditionalInfoDatabase.userDAdditionalInfo().updateUserAdditionalInfo(userAdditionalInfo);
    }

    public void updateUserRssUrl(String rssNewsUrl){
        UserAdditionalInfo userAdditionalInfo = appAdditionalInfoDatabase.userDAdditionalInfo()
                .getUserAdditionalInfo(currentUser.uid);
        userAdditionalInfo.rssNewsUrl = rssNewsUrl;
        appAdditionalInfoDatabase.userDAdditionalInfo().updateUserAdditionalInfo(userAdditionalInfo);
        currentUser.rssNewsUrl = rssNewsUrl;
    }
}
