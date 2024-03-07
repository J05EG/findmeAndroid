package com.example.findme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findme.Utils.ConstantSQLite;
import com.example.findme.Utils.Validaciones;
import com.example.findme.connections.ConnectionBackend;
import com.example.findme.models.User;

import java.io.IOException;
import java.util.Calendar;

public class RegisterUserActivity extends AppCompatActivity {
    private ConnectionBackend conn = new ConnectionBackend();
    private User usuario = new User();
    private EditText txtRut, txtNombre, txtApellido, txtEmail, txtPassword,txtPassword2;
    private TextView txtFNacimiento;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Button btnEnviarRegistro;
    private ImageButton btnDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtRut = findViewById(R.id.textRut);
        txtNombre = findViewById(R.id.textNombre);
        txtApellido = findViewById(R.id.textApellido);
        txtFNacimiento = findViewById(R.id.textNacimiento);
        txtEmail = findViewById(R.id.textMail);
        txtPassword = findViewById(R.id.textPassword);
        txtPassword2 = findViewById(R.id.textPasswordConfirmed);
        btnEnviarRegistro = findViewById(R.id.buttonEnviarRegistro);
        btnDate = findViewById(R.id.imageButtonDateUser);


        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterUserActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                dialog.getWindow();
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 441797328000l);
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                String mes = (Integer.toString(month));
                String dia = Integer.toString(day);
                if(month<10){ mes = "0"+mes; }
                if(day <10 ){ dia = "0"+day; }
                Log.d("Fecha", "fecha: "+ year + "-" + mes + "-" + dia);
                String date = year + "-" + mes + "-" + dia;
                txtFNacimiento.setText(date);
            }
        };

        btnEnviarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validaciones()){
                    usuario.setRut(txtRut.getText().toString().trim());
                    usuario.setNombre(txtNombre.getText().toString().trim());
                    usuario.setApellido(txtApellido.getText().toString().trim());
                    usuario.setfNacimiento(txtFNacimiento.getText().toString().trim());
                    usuario.setEmail(txtEmail.getText().toString().trim());
                    usuario.setPassword(txtPassword.getText().toString().trim());
                    try {
                        conn.RegisterUser(usuario);
                        Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Rut ya existe en Base de datos", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Ingrese los datos solicitados", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validaciones(){
        boolean estatus= true;
        if(!Validaciones.Vacio(txtRut)){
            if(!Validaciones.validaRut(txtRut)){
                estatus = false;
            };
        }
        if(Validaciones.Vacio(txtNombre)){
           estatus =false;
        }
        if(Validaciones.Vacio(txtApellido)){
            estatus =false;
        }
        if(Validaciones.VacioTextView(txtFNacimiento)){
            estatus =false;
        }
        if(!Validaciones.Vacio(txtEmail)){
            if(!Validaciones.isEmail(txtEmail)){
                estatus= false;
            }
        }
        if (!Validaciones.Vacio(txtPassword)){
            if(Validaciones.largoPassword(txtPassword)){
                estatus= false;
            }
        }
        if (!Validaciones.Vacio(txtPassword2)) {
            if (Validaciones.samePassword(txtPassword, txtPassword2)) {
                estatus = false;
            }
        }

        return  estatus;
    }
}