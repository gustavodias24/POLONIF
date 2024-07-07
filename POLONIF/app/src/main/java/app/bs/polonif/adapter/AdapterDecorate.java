package app.bs.polonif.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.bs.polonif.R;
import app.bs.polonif.model.DecorateModel;

public class AdapterDecorate extends RecyclerView.Adapter<AdapterDecorate.MyViewHolder> {

    List<DecorateModel> lista;
    Activity c;

    public AdapterDecorate(List<DecorateModel> lista, Activity c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(lista.get(position).getImagem()).into( holder.imageDecoratexxx);

        holder.btnRemover.setOnClickListener( v -> {
            lista.remove(position);
            this.notifyDataSetChanged();
        });
        holder.textInfo.setText(lista.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textInfo;
        Button btnRemover;

        ImageView imageDecoratexxx;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textInfo = itemView.findViewById(R.id.text_info);
            btnRemover = itemView.findViewById(R.id.btn_excluir);
            imageDecoratexxx = itemView.findViewById(R.id.imageDecoratexxx);
        }
    }
}
