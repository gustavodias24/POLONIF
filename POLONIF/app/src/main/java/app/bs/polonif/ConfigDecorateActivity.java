package app.bs.polonif;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.bs.polonif.adapter.AdapterDecorate;
import app.bs.polonif.databinding.ActivityConfigDecorateBinding;
import app.bs.polonif.databinding.ActivityConfigTaskBinding;
import app.bs.polonif.databinding.LayoutAdicionarDecorateBinding;
import app.bs.polonif.model.DecorateModel;
import app.bs.polonif.model.TaskModel;

public class ConfigDecorateActivity extends AppCompatActivity {

    private AdapterDecorate adapter;
    private RecyclerView r;
    private Uri imageUri;
    private ActivityConfigDecorateBinding mainBinding;
    private Dialog d;
    private Bundle b;

    private TaskModel taskModel;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tasks");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("tasks");
    LayoutAdicionarDecorateBinding decorateBinding;

    List<DecorateModel> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorateBinding = LayoutAdicionarDecorateBinding.inflate(getLayoutInflater());
        mainBinding = ActivityConfigDecorateBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        
        mainBinding.finalizar.setOnClickListener(v -> {
            taskModel.setDecorates(lista);
            databaseReference.child(taskModel.getId()).setValue(taskModel).addOnCompleteListener( task ->{
               if ( task.isSuccessful() ){
                   finish();
                   Toast.makeText(this, "Pronto!", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(this, ProfessorActivity.class));
               } else{
                   Toast.makeText(this, "Tente novamente...", Toast.LENGTH_SHORT).show();
               }
            });
        });

        decorateBinding.imageDecorate.setOnClickListener( v -> openFileChooser());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mainBinding.adicionar.setOnClickListener(v -> d.show());
        configD();

        b = getIntent().getExtras();

        configRecyclerView();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dado : snapshot.getChildren()) {
                    TaskModel taskFinder = dado.getValue(TaskModel.class);
                    assert taskFinder != null;
                    if (taskFinder.getId().equals(b.getString("uploadId", ""))) {
                        taskModel = taskFinder;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        decorateBinding.adicionar.setOnClickListener(v -> uploadFile());
    }

    private void configRecyclerView() {
        r = mainBinding.rvDecorate;
        r.setLayoutManager(new LinearLayoutManager(this));
        r.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        adapter = new AdapterDecorate(lista, this);
        r.setAdapter(adapter);
    }

    private void configD() {
        AlertDialog.Builder b = new AlertDialog.Builder(ConfigDecorateActivity.this);

        b.setView(decorateBinding.getRoot());
        d = b.create();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                decorateBinding.imageDecorate.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        DecorateModel decorateModel = new DecorateModel();

                        decorateModel.setImagem(imageUrl);
                        decorateModel.setPergunta(decorateBinding.edtPergunta.getText().toString());
                        decorateModel.setRespErrada1(decorateBinding.edtqr1.getText().toString());
                        decorateModel.setRespErrada2(decorateBinding.edtqr2.getText().toString());
                        decorateModel.setRespCorreta(decorateBinding.edtqc.getText().toString());

                        lista.add(decorateModel);

                        decorateBinding.edtPergunta.setText("");
                        decorateBinding.edtqr1.setText("");
                        decorateBinding.edtqr2.setText("");
                        decorateBinding.edtqc.setText("");
                        adapter.notifyDataSetChanged();
                        d.dismiss();

                        decorateBinding.imageDecorate.setImageResource(R.drawable.addimg);

                        Toast.makeText(ConfigDecorateActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    }))
                    .addOnFailureListener(e -> Toast.makeText(ConfigDecorateActivity.this, "Upload falhou: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Imagem não selecionada", Toast.LENGTH_SHORT).show();
        }
    }
}