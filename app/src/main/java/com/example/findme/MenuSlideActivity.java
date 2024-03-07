package com.example.findme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findme.Utils.ConstantSQLite;
import com.example.findme.connections.ConnectionBackend;
import com.example.findme.models.User;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.reactivex.annotations.NonNull;

public class MenuSlideActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    ConnectionBackend conAPI = new ConnectionBackend();
    AlertDialog alert = null;
    LocationManager locationManager;
    String token;
    TextView nameNav, mailNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = MenuSlideActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_slide);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_notificaciones, R.id.nav_users,R.id.nav_profile, R.id.nav_register_person)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertNoGps();
        }
        if(ConstantSQLite.ConsultarDatosUser(context).getId() == null){
            User user = conAPI.ConsultaDatosUserAPI(ConstantSQLite.ConsultarDatosMainUser(context).getId());
            try {
                ConstantSQLite.RegisterUserSQL(user, context);
            }catch (Exception e){
                e.getStackTrace();
            }
        }
        createNotificationChannel();
        User user = ConstantSQLite.ConsultarDatosUser(getApplicationContext());
        Toast.makeText(getApplicationContext(), "Hola " + user.getNombre(), Toast.LENGTH_LONG).show();
        View headerView = navigationView.getHeaderView(0);
        nameNav = (TextView) headerView.findViewById(R.id.txtNombreUsuarioNav);
        mailNav = (TextView) headerView.findViewById(R.id.txtMailUsuarioNav);
        nameNav.setText(String.format("Hola "+user.getNombre()+" "+user.getApellido()));
        mailNav.setText(user.getEmail());
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FIREBASE", "getInstanceId failed", task.getException());
                            return;
                        }
                        User test = ConstantSQLite.ConsultarDatosUser(getApplicationContext());
                        if(ConstantSQLite.ConsultarDatosUser(getApplicationContext()).getToken() == null){
                            // Get new Instance ID token
                            token = task.getResult().getToken();
                            String userId = ConstantSQLite.ConsultarDatosMainUser(getApplicationContext()).getId();
                            conAPI.setUseerSmartphone(userId, token);
                            ConstantSQLite.UpdateTokenUser(getApplicationContext(), token, userId);
                            // Log and toast

                            Log.d("FIREBASE" , token);
                            User test2 = ConstantSQLite.ConsultarDatosUser(getApplicationContext());
                            //Toast.makeText(MenuSlideActivity.this, token, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description ="test";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel( getString(R.string.default_notification_channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_slide, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}