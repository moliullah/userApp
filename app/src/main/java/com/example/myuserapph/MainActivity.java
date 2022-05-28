package com.example.myuserapph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myuserapph.databinding.ActivityMainBinding;
import com.example.myuserapph.viewmodels.LoginViewModel;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private LoginViewModel loginViewModel;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);
        loginViewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);
        DrawerLayout drawerLayout = binding.mDrawerLayout;
        Toolbar toolbar = binding.mToolbar;
        NavigationView navView = binding.navView;
        setSupportActionBar(toolbar);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainerView2);

        appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.productListFragment,
                        R.id.userProfileFragment, R.id.userOrderFragment,
                        R.id.cartListFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(
                this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        loginViewModel.getAuthenticateStateLiveData().observe(this,authenticateState -> {
            navController.popBackStack();
            if (authenticateState== LoginViewModel.AuthenticateState.AUTHENTICATED){
                navController.navigate(R.id.productListFragment);
            }else{
                navController.navigate(R.id.userLoginFragment);
            }
        });
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.userLoginFragment) {
                toolbar.setVisibility(View.GONE);
                navView.setVisibility(View.GONE);
            }else {
                toolbar.setVisibility(View.VISIBLE);
                navView.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            loginViewModel.logout();
        }
        return super.onOptionsItemSelected(item);
    }
}