package com.example.findme.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.example.findme.Adapters.AdapterUserDevice;
import com.example.findme.R;
import com.example.findme.Utils.ConstantSQLite;
import com.example.findme.connections.ConectionSQLite;
import com.example.findme.connections.ConnectionBackend;
import com.example.findme.models.UserDevice;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    ConnectionBackend conAPI = new ConnectionBackend();
    List<UserDevice> lista;
    RecyclerView recycler;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String userId = ConstantSQLite.ConsultarDatosMainUser(getContext()).getId();
        try {
            mView = inflater.inflate(R.layout.fragment_users, container, false);
            recycler=mView.findViewById(R.id.recyclerIdFrag);
            lista = conAPI.ListarUsuariosReceptor(userId);
            recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            AdapterUserDevice adapter = new AdapterUserDevice((ArrayList<UserDevice>) lista);
            if(adapter.getItemCount()>0){
                recycler.setAdapter(adapter);
            }else {
                Toast.makeText(getContext(), "No hay usuarios para mostrar", Toast.LENGTH_LONG).show();
            }
            /*adapter.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(),"Seleccion: "+ lista.get(recycler.getChildAdapterPosition(v)).getIdPersonDevice(),Toast.LENGTH_SHORT).show();
                    return false;
                }
            });*/
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}