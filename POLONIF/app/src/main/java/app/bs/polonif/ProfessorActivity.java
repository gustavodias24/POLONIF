package app.bs.polonif;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.bs.polonif.adapter.AdapterTask;
import app.bs.polonif.databinding.ActivityMainBinding;
import app.bs.polonif.databinding.ActivityProfessorBinding;
import app.bs.polonif.model.TaskModel;

public class ProfessorActivity extends AppCompatActivity {

    private ActivityProfessorBinding mainBinding;

    private AdapterTask adapter;
    private List<TaskModel> lista = new ArrayList<>();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tasks");

    private boolean bool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityProfessorBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        bool = Objects.requireNonNull(getIntent().getExtras()).getBoolean("bool", false);

        if ( !bool ){
            mainBinding.adicionar.setVisibility(View.GONE);
            mainBinding.professor.setText("Área do aluno");
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mainBinding.adicionar.setOnClickListener(v -> startActivity(new Intent(this, ConfigTaskActivity.class)));

        mainBinding.rTask.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.rTask.setHasFixedSize(true);
        mainBinding.rTask.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new AdapterTask(lista, this, bool);
        mainBinding.rTask.setAdapter(adapter);


        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for (DataSnapshot dado : snapshot.getChildren()) {
                    lista.add(dado.getValue(TaskModel.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}