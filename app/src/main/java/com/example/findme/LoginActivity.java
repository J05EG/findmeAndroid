package com.example.findme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.findme.Utils.ConstantSQLite;
import com.example.findme.Utils.Validaciones;
import com.example.findme.connections.ConnectionBackend;
import com.example.findme.models.UserLogin;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private ConnectionBackend conn = new ConnectionBackend();
    private UserLogin login = new UserLogin();
    private EditText txtEmail, txtPassword;
    private Button btnLogin, btnRegistrar;
    private RadioButton rbSesion;
    private boolean isActivatedRadioButton;
    private static final String STRING_PREFERENSES = "findme";
    private static final String PREFERENCE_ESTADO_SESION = "estado.sesion";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verificarPermisos();
        final Context context = LoginActivity.this;
        if(obtenerEstadoButton(this)){
            UserLogin userLogin = ConstantSQLite.ConsultarDatosUserLogin(context);
            if(userLogin.getEmail() != null){
                try {
                    if(conn.LoginUser(userLogin, context)) {
                        Intent intent = new Intent(LoginActivity.this, MenuSlideActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(!conn.LoginUser(userLogin, context)){
                        ConstantSQLite.BorrarDB(context);
                        LoginActivity.cambiarEstadoButton(LoginActivity.this, false);
                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        txtEmail = findViewById(R.id.editTextEmail);
        txtPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        btnRegistrar = findViewById(R.id.buttonLogin2);
        rbSesion = findViewById(R.id.radioButtonSesion);
        btnLogin.setClickable(false);

        isActivatedRadioButton = rbSesion.isChecked();
        rbSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActivatedRadioButton){
                    rbSesion.setChecked(false);
                }
                isActivatedRadioButton = rbSesion.isChecked();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login.setEmail(txtEmail.getText().toString().trim());
                login.setPassword(txtPassword.getText().toString().trim());
                try {
                    if(conn.LoginUser(login, context) && Validar()) {
                        guardarEstadoButton();
                        Intent intent = new Intent(LoginActivity.this, MenuSlideActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(!conn.LoginUser(login, context) && Validar()){
                        Toast.makeText(v.getContext(),"Correo o contraseña invalido",Toast.LENGTH_LONG).show();
                    }else{
                        Validar();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }

    public boolean Validar(){
        boolean valor;
        if( valor = Validaciones.Vacio(txtEmail)){

        }else{
             valor= Validaciones.isEmail(txtEmail);
             if(valor) {
                 valor = !Validaciones.Vacio(txtPassword);
             }
        }
    return valor;
    }

    public static void cambiarEstadoButton(Context c, boolean b){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENSES, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_SESION, b).apply();
    }

    public void guardarEstadoButton(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENSES, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_SESION, rbSesion.isChecked()).apply();
    }

    public static boolean obtenerEstadoButton(Context c){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENSES, MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_ESTADO_SESION, false);
    }

    private void verificarPermisos() {
        int permsRequestCode = 100;
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        int accessFinePermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarsePermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (accessFinePermission == PackageManager.PERMISSION_GRANTED && accessCoarsePermission == PackageManager.PERMISSION_GRANTED) {
            //se realiza metodo si es necesario...
        } else {
            requestPermissions(perms, permsRequestCode);
        }
    }
}