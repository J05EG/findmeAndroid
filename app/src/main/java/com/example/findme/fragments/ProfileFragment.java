package com.example.findme.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.findme.LoginActivity;
import com.example.findme.R;
import com.example.findme.Utils.ConstantSQLite;
import com.example.findme.models.User;

import java.text.DateFormat;

public class ProfileFragment extends Fragment {

    TextView txtBienvenida, txtNombre, txtRut, txtCorreo, txtFechaNacimiento ;
    Button btnCerrarSesion, btnEditar;
    View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_profile, container, false);
         txtBienvenida = v.findViewById(R.id.txtBienvenida);
         txtNombre = v.findViewById(R.id.txtNombre);
         txtRut = v.findViewById(R.id.txtRut);
         txtCorreo = v.findViewById(R.id.txtCorreo);
         txtFechaNacimiento = v.findViewById(R.id.txtFechaNacimiento2);
        User user = ConstantSQLite.ConsultarDatosUser(getContext());
         String saludo = "Hola "+user.getNombre();
        txtBienvenida.setText(saludo);
        txtNombre.setText(String.format("Nombre: "+user.getNombre()+" "+user.getApellido()));
        txtRut.setText(String.format("Rut: "+user.getRut()));
        txtCorreo.setText(String.format("Email: "+user.getEmail()));
        String[] fecha = user.getfNacimiento().split("T");
        txtFechaNacimiento.setText(fecha[0]);

        btnCerrarSesion = v.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantSQLite.BorrarDB(getContext());
                LoginActivity.cambiarEstadoButton(getContext(), false);
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return v;
    }
}