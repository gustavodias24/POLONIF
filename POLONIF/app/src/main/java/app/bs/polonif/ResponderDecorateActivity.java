package app.bs.polonif;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import app.bs.polonif.databinding.ActivityProfessorBinding;
import app.bs.polonif.databinding.ActivityResponderDecorateBinding;
import app.bs.polonif.model.DecorateModel;
import app.bs.polonif.model.TaskModel;

public class ResponderDecorateActivity extends AppCompatActivity {

    private ActivityResponderDecorateBinding mainBinding;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tasks");
    private List<DecorateModel> lista = new ArrayList<>();
    private String id_task;
    private int page = 0;
    private int correta = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityResponderDecorateBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        id_task = Objects.requireNonNull(getIntent().getExtras()).getString("id", "");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dado : snapshot.getChildren()) {
                    TaskModel task = dado.getValue(TaskModel.class);

                    if (task.getId().equals(id_task)) {
                        lista.addAll(task.getDecorates());
                        configDecorate();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mainBinding.responder.setOnClickListener(v -> {
            int respUser = 0;

            if (mainBinding.resp1.isChecked()) {
                respUser = 1;
            } else if (mainBinding.resp2.isChecked()) {
                respUser = 2;
            } else if (mainBinding.resp3.isChecked()) {
                respUser = 3;
            }

            if (respUser == correta) {
                exibirResultado("Parabéns!", "Você acertou a resposta correta, clique em 'ok' para ir para  a próxima!", true);
            } else {
                exibirResultado("Game Over!", "Você errou a resposta correta, clique em 'ok' para ir para  voltar!", false);
            }

//            mainBinding.resp1.setChecked(false);
//            mainBinding.resp2.setChecked(false);
//            mainBinding.resp3.setChecked(false);


        });
    }

    private void exibirResultado(String title, String descri, boolean win) {
        AlertDialog.Builder dialogB = new AlertDialog.Builder(ResponderDecorateActivity.this);
        dialogB.setTitle(title);
//        dialogB.setCancelable(false);
        dialogB.setMessage(descri);
        dialogB.setPositiveButton("ok", (d, i) -> {
            if (win) {
                page += 1;
                configDecorate();
            } else {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            }
        });

        dialogB.show();

    }

    private void configDecorate() {
        if (!lista.isEmpty()) {
            correta = new Random().nextInt(3) + 1;
            DecorateModel decorate;

            try {
                decorate = lista.get(page);
            } catch (Exception e) {
                page = 0;
                decorate = lista.get(page);
            }

            mainBinding.pergunta.setText(decorate.getPergunta());

            Picasso.get().load(decorate.getImagem()).into(mainBinding.imagem);
            switch (correta) {
                case 1:
                    mainBinding.resp1.setText(decorate.getRespCorreta());
                    mainBinding.resp2.setText(decorate.getRespErrada1());
                    mainBinding.resp3.setText(decorate.getRespErrada2());
                    break;
                case 2:
                    mainBinding.resp2.setText(decorate.getRespCorreta());
                    mainBinding.resp3.setText(decorate.getRespErrada1());
                    mainBinding.resp1.setText(decorate.getRespErrada2());
                    break;
                case 3:
                    mainBinding.resp3.setText(decorate.getRespCorreta());
                    mainBinding.resp2.setText(decorate.getRespErrada1());
                    mainBinding.resp1.setText(decorate.getRespErrada2());
                    break;
            }
        }
    }
}