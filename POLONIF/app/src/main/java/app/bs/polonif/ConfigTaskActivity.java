package app.bs.polonif;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import app.bs.polonif.databinding.ActivityConfigTaskBinding;
import app.bs.polonif.databinding.ActivityProfessorBinding;
import app.bs.polonif.model.TaskModel;

public class ConfigTaskActivity extends AppCompatActivity {

    private ActivityConfigTaskBinding mainBinding;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("tasks");
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tasks");

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityConfigTaskBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mainBinding.prosseguir.setOnClickListener(v -> uploadFile());

        mainBinding.imgTask.setOnClickListener(v -> openFileChooser());


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
                mainBinding.imgTask.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        String uploadId = databaseReference.push().getKey();


                        TaskModel novaTask = new TaskModel();

                        novaTask.setImagem(imageUrl);
                        novaTask.setId(uploadId);
                        novaTask.setTitulo(mainBinding.edtTitulo.getText().toString());
                        novaTask.setDescricao(mainBinding.edtDescri.getText().toString());

                        databaseReference.child(novaTask.getId()).setValue(novaTask);

                        Intent i = new Intent(this, ConfigDecorateActivity.class);
                        i.putExtra("uploadId", uploadId);
                        finish();
                        startActivity(i);
                        Toast.makeText(ConfigTaskActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(ConfigTaskActivity.this, "Upload falhou: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("mayara", "uploadFile: " + e.getMessage());
                    });
        } else {
            Toast.makeText(this, "Imagem não selecionada", Toast.LENGTH_SHORT).show();
        }
    }
}