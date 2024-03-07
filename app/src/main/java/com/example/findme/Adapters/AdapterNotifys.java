package com.example.findme.Adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findme.R;
import com.example.findme.connections.ConnectionBackend;
import com.example.findme.models.Notify;
import com.example.findme.models.UserDevice;

import java.util.ArrayList;

public class AdapterNotifys extends RecyclerView.Adapter<AdapterNotifys.ViewHolderNotifys> implements View.OnClickListener {

    ArrayList<Notify> listaNotifys;
    private View.OnClickListener clickListener;
    ConnectionBackend conn = new ConnectionBackend();

    public AdapterNotifys(ArrayList<Notify> listaNotificaciones) {
        this.listaNotifys = listaNotificaciones;
    }

    @NonNull
    @Override
    public ViewHolderNotifys onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_notify,null,false);
        view.setOnClickListener(this);
        return new ViewHolderNotifys(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNotifys holder, int position) {
        final Notify notify = listaNotifys.get(position);
        holder.titulo.setText(notify.getTitulo());
        holder.contenido.setText(notify.getContenido());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("¿Desea eliminar la notificación?").setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //conexion para eliminar
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
                titulo.setTitle("Eliminar notificación");
                titulo.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaNotifys.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.clickListener = listener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick(v);
    }


    public class ViewHolderNotifys extends RecyclerView.ViewHolder {
        ImageView delete;
        TextView titulo;
        TextView contenido;
        public ViewHolderNotifys(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.btnDeleteNotify);
            titulo = itemView.findViewById(R.id.idTitulo);
            contenido = itemView.findViewById(R.id.idContenido);
        }
    }
}
