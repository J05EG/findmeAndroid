package com.example.findme.fragments;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findme.R;
import com.example.findme.Utils.ConstantSQLite;
import com.example.findme.Utils.Validaciones;
import com.example.findme.connections.ConectionSQLite;
import com.example.findme.connections.ConnectionBackend;
import com.example.findme.models.Person;
import com.example.findme.models.UserDevice;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.glxn.qrgen.android.QRCode;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterPersonFragment extends Fragment {

    private ConnectionBackend conn = new ConnectionBackend();
    private Person person = new Person();
    private TextView txtRut, txtNombre, txtApellido, txtFNacimiento;
    private Button btnEnviarRegistro;
    private ImageButton btnDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register_person, container, false);
        txtRut = view.findViewById(R.id.textRut);
        txtNombre = view.findViewById(R.id.textNombre);
        txtApellido = view.findViewById(R.id.textApellido);
        txtFNacimiento = view.findViewById(R.id.textNacimiento);
        btnEnviarRegistro = view.findViewById(R.id.buttonEnviarRegistro);
        btnDate = view.findViewById(R.id.imageButtonDatePerson);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                dialog.getWindow();
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                Log.d("Fecha", "fecha: "+ year + "-" + month + "-" + day);
                String date = year + "-" + month + "-" + day;
                txtFNacimiento.setText(date);
            }
        };

        btnEnviarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validaciones()){
                    person.setRut(txtRut.getText().toString().trim());
                    person.setNombre(txtNombre.getText().toString().trim());
                    person.setApellido(txtApellido.getText().toString().trim());
                    person.setFechaNacimiento(txtFNacimiento.getText().toString().trim());

                    String userId = null;
                    userId = ConstantSQLite.ConsultarDatosMainUser(getContext()).getId();

                    try {
                        UserDevice userD = conn.RegisterPerson(person,userId);
                        Toast.makeText(getContext(), "Usuario "+userD.getDatosPersona().getNombre()+" se creó correctamente", Toast.LENGTH_LONG).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Navigation.findNavController(view).navigate(R.id.nav_users);
                            }
                        }, 2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(), "Ingrese los datos solicitados", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private boolean validaciones(){
        boolean estatus= true;
        if(!Validaciones.Vacio((EditText) txtRut)){
            if(!Validaciones.validaRut((EditText) txtRut)){
                estatus = false;
            };
        }
        if(Validaciones.Vacio((EditText) txtNombre)){
            estatus =false;
        }
        if(Validaciones.Vacio((EditText) txtApellido)){
            estatus =false;
        }
        if(Validaciones.VacioTextView(txtFNacimiento)){
            estatus =false;
        }

        return  estatus;
    }

}