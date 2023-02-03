package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText titulo, comentario;
    private TextView textC;
    private Button bCrear, bVer, bEliminar;
    private CommentDBHelper commentDBHelper;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titulo = findViewById(R.id.textInput1);
        comentario = findViewById(R.id.textInput2);
        textC = findViewById(R.id.textC);
        bCrear = findViewById(R.id.bCrear);
        bVer = findViewById(R.id.bVer);
        bEliminar = findViewById(R.id.bEliminar);
        spinner = findViewById(R.id.spinner);
        commentDBHelper = new CommentDBHelper(this);

        loadSpinnerData();

        bCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titulo.getText().toString().trim();
                String comment = comentario.getText().toString().trim();
                if (title.isEmpty() || comment.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Todos los campos deben estar rellenados", Toast.LENGTH_LONG).show();
                } else {
                    boolean isInserted = commentDBHelper.insertData(title, comment);
                    if (isInserted == true) {
                        Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_LONG).show();
                        loadSpinnerData();
                        titulo.setText(null);
                        comentario.setText(null);
                    } else {
                        Toast.makeText(MainActivity.this, "Data not inserted", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        bVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTitle = spinner.getSelectedItem().toString();
                textC.setText(commentDBHelper.getCommentByTitle(selectedTitle));
            }
        });

        bEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTitle = spinner.getSelectedItem().toString();
                commentDBHelper.deleteCommentByTitle(selectedTitle);
                textC.setText(null);
                loadSpinnerData();
                Toast.makeText(getApplicationContext(), "Comment Deleted", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadSpinnerData();
    }

    private void loadSpinnerData() {
        CommentDBHelper commentDBHelper = new CommentDBHelper((getApplicationContext()));
        List<String> titles = commentDBHelper.getAllTitles();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, titles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}