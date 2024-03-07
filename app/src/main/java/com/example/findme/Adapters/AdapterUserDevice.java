package com.example.findme.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findme.MenuSlideActivity;
import com.example.findme.R;
import com.example.findme.connections.ConnectionBackend;
import com.example.findme.fragments.HomeFragment;
import com.example.findme.models.UserDevice;

import net.glxn.qrgen.android.QRCode;

import java.util.ArrayList;


public class AdapterUserDevice extends RecyclerView.Adapter<AdapterUserDevice.ViewHolderDatos> implements View.OnLongClickListener, View.OnClickListener {

    ArrayList<UserDevice> listaUsuarios;
    private View.OnLongClickListener longClickListener;
    private View.OnClickListener clickListener;
    ConnectionBackend conn = new ConnectionBackend();

    public AdapterUserDevice(ArrayList<UserDevice> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        view.setOnLongClickListener(this);
        return new ViewHolderDatos(view);
    }

    public static class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView nombre, apellido;
        ImageView btnGetUbi, btnSetMap, btnQr, deletePerson;

        public ViewHolderDatos( View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.idNombre);
            apellido = itemView.findViewById(R.id.idApellido);
            btnGetUbi = itemView.findViewById(R.id.btnGetUbi);
            btnSetMap = itemView.findViewById(R.id.btnSetMap);
            btnQr = itemView.findViewById(R.id.btnGetQR);
            deletePerson = itemView.findViewById(R.id.btnDeletePerson);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolderDatos holder, final int position) {
        final UserDevice user = listaUsuarios.get(position);
        holder.nombre.setText(user.getDatosPersona().getNombre());
        holder.apellido.setText(user.getDatosPersona().getApellido());

        holder.btnGetUbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId =user.getIdPersonDevice();
                String mens = conn.getLastUserLocation(userId);
                String mens2 = conn.getLimitAreaUser(userId);
                Toast.makeText(v.getContext(), mens, Toast.LENGTH_SHORT).show();
                String[] pos = mens.split(",");
                String[] posArea = mens2.split(",");
                HomeFragment fragment = new HomeFragment();
                Bundle params = new Bundle();
                if(pos.length>1){
                    params.putDouble("latDouble",Double.valueOf(pos[0]));
                    params.putDouble("longDouble",Double.valueOf(pos[1]));
                    params.putString("userName",user.getDatosPersona().getNombre()+" "+user.getDatosPersona().getApellido());
                    if(posArea.length>1){
                        params.putDouble("latDoubleCenter",Double.valueOf(posArea[0]));
                        params.putDouble("longDoubleCenter",Double.valueOf(posArea[1]));
                        params.putDouble("radioCenter",Double.valueOf(posArea[2]));
                    }
                    Navigation.findNavController(v).navigate(R.id.nav_home,params);
                }else{
                    Toast.makeText(v.getContext(), "No hay una ubicación para este usuario aún", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.btnSetMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final View view = v;
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("¿Desea Cambiar el área delimitada? \n \n Se eliminará la anterior").setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String userId =user.getIdPersonDevice();
                                String mens = conn.getLastUserLocation(userId);
                                Bundle params = new Bundle();
                                String[] pos = mens.split(",");
                                if(pos.length>1){
                                    params.putDouble("latDouble",Double.valueOf(pos[0]));
                                    params.putDouble("longDouble",Double.valueOf(pos[1]));
                                    params.putString("userName",user.getDatosPersona().getNombre()+" "+user.getDatosPersona().getApellido());
                                    params.putString("userDeviceId",userId);
                                    Navigation.findNavController(view).navigate(R.id.nav_home,params);
                                }else{
                                    Toast.makeText(view.getContext(),"Usuario sin locacion ",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog titulo = alert.create();
                titulo.setTitle("Eliminar persona");
                titulo.show();
            }
        });

        holder.btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qr = String.format(user.getIdPersonDevice()+","+user.getDatosPersona().getNombre()+","+user.getDatosPersona().getApellido());
                Bitmap bitmap = QRCode.from(qr).withSize(500, 500).bitmap();
                ImageView imageView = new ImageView(v.getContext());
                imageView.setImageBitmap(bitmap);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setCancelable(false);
                builder.setView(imageView);
                final AlertDialog dialog = builder.create();
                dialog.setTitle("Datos de "+user.getDatosPersona().getNombre()+" para asociar teléfono");
                dialog.show();
            }
        });

        holder.deletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("¿Desea eliminar a "+user.getDatosPersona().getNombre()+"?").setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //conexion para eliminar
                                conn.deleteUserDevice(user.getIdPersonDevice());
                                Navigation.findNavController(v).navigate(R.id.nav_users);
                                Toast.makeText(v.getContext(),"Se ha eliminado exitosamente",Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog titulo = alert.create();
                titulo.setTitle("Eliminar persona");
                titulo.show();
            }
        });
    }

    @Override
    public int getItemCount() { return listaUsuarios.size(); }

    public void  setOnLongClickListener(View.OnLongClickListener listener){
        this.longClickListener = listener;
    }

    @Override
    public boolean onLongClick(View v) {
        if(longClickListener != null){
            longClickListener.onLongClick(v);
            return true;
        }
        return false;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.clickListener = listener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick(v);
    }
}