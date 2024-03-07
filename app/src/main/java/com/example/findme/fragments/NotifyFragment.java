package com.example.findme.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findme.Adapters.AdapterNotifys;
import com.example.findme.Adapters.AdapterUserDevice;
import com.example.findme.R;
import com.example.findme.Utils.ConstantSQLite;
import com.example.findme.connections.ConnectionBackend;
import com.example.findme.models.Notify;
import com.example.findme.models.UserDevice;

import java.util.ArrayList;
import java.util.List;

public class NotifyFragment extends Fragment {

    ConnectionBackend conAPI = new ConnectionBackend();
    List<Notify> lista;
    RecyclerView recycler;
    View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_notify, container, false);
        String userId = ConstantSQLite.ConsultarDatosMainUser(getContext()).getId();
        try {
            recycler=mView.findViewById(R.id.recyclerIdNotify);
            //lista = conAPI.ListarNotificaicones(userId);
            recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            AdapterNotifys adapter = new AdapterNotifys((ArrayList<Notify>) lista);
            if(adapter.getItemCount()>0){
                recycler.setAdapter(adapter);
            }else {
                Toast.makeText(getContext(), "No hay Notificaciones para mostrar", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "No hay Notificaciones para mostrar", Toast.LENGTH_LONG).show();
        }
        return mView;
    }
}