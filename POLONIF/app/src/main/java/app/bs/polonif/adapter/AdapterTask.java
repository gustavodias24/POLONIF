package app.bs.polonif.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import app.bs.polonif.R;
import app.bs.polonif.ResponderDecorateActivity;
import app.bs.polonif.model.TaskModel;

public class AdapterTask extends RecyclerView.Adapter<AdapterTask.MyViewHolder> {
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tasks");
    List<TaskModel> lista;
    Activity a;

    Boolean isAdmin;

    public AdapterTask(List<TaskModel> lista, Activity a, Boolean isAdmin) {
        this.lista = lista;
        this.a = a;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tasks, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TaskModel taskModel = lista.get(position);

        Picasso.get().load(taskModel.getImagem()).into(holder.imageTask);
        holder.title.setText(taskModel.getTitulo());
        holder.descri.setText(taskModel.getDescricao());

        holder.comecar.setOnClickListener(v -> {
            Intent i = new Intent(a, ResponderDecorateActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("id", taskModel.getId());
            a.startActivity(i);
        });

        if (isAdmin){
            holder.layoutAdmin.setVisibility(View.VISIBLE);
        }

        holder.excluir.setOnClickListener( v -> {
            ref.child(taskModel.getId()).setValue(null).addOnCompleteListener( task -> {
                if ( task.isSuccessful()){
                    Toast.makeText(a, "Excluído com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageTask;
        Button comecar, excluir;
        TextView title, descri;

        LinearLayout layoutAdmin;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageTask = itemView.findViewById(R.id.imageTask);
            comecar = itemView.findViewById(R.id.comecar);
            title = itemView.findViewById(R.id.titleTask);
            descri = itemView.findViewById(R.id.descriTask);

            excluir = itemView.findViewById(R.id.excluir);
            layoutAdmin = itemView.findViewById(R.id.layoutAdmin);
        }
    }
}
