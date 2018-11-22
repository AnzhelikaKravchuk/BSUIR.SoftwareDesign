package com.example.androidlabs;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.androidlabs.db.AppDatabase;
import com.example.androidlabs.db.entities.User;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity implements
        IndexFragment.OnFragmentInteractionListener, profileFragment.OnFragmentInteractionListener,
        editProfileFragment.OnFragmentInteractionListener{

private DrawerLayout mDrawerLayout;
private NavController navController;

public static AppDatabase appDatabase;
public static User currentUser;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase").allowMainThreadQueries().build();
        currentUser = MainActivity.appDatabase.userDao().getUser();
        if (currentUser == null) {
               Button button = findViewById(R.id.button);
               button.setVisibility(View.VISIBLE);
        }
        mDrawerLayout = findViewById(R.id.drawer_layout);

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
@Override
public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();

        switch (menuItem.getItemId()) {
        case R.id.index_menu_item:
        navController.navigate(R.id.indexFragment);
        return true;
        case R.id.first_menu_item:
        navController.navigate(R.id.profileFragment);
        return true;

        }
        return false;
        }
        };
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        }

        public void addUser(View view)
        {
                User user = new User();
                user.email ="111germanstashinskii@gmail.com";
                user.password ="Hello1";
                user.id =1;
                MainActivity.appDatabase.userDao().addUser(user);
                Button button = findViewById(R.id.button);
                 button.setVisibility(View.GONE);
        }

@Override
public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
        }

@Override
public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

        case android.R.id.home:
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;

        case R.id.about_menu_item:
        navController.navigate(R.id.aboutActivity);
        return true;

default:
        return super.onOptionsItemSelected(item);
        }
        }

@Override
public void onFragmentInteraction(Uri uri) {
        }
public void openEditableProfile(View view) {
        navController.navigate(R.id.editProfileFragment);
        }

}