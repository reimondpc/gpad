package com.reimondpc.gpad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AgregarActivity extends AppCompatActivity {
    String type, pullTitle, content, noteSelected;
    EditText TITLE, CONTENT;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        setTitle("Crear Nota");

        initComponent();
    }

    private void initComponent() {
        TITLE = (EditText) findViewById(R.id.etTitulo);
        CONTENT = (EditText) findViewById(R.id.etContent);

        Bundle bundle = this.getIntent().getExtras();
        noteSelected = bundle.getString("noteSelected");
        pullTitle = bundle.getString("title");
        content = bundle.getString("content");
        type = bundle.getString("type");

        if (type.equals("edit")) {
            TITLE.setText(pullTitle);
            CONTENT.setText(content);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
            setTitle("Editar Nota");
        }
        initializerFirebase();
    }

    private void initializerFirebase() {
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users").child(userId);
    }

    //Metodo para actualizar las notas
    private void addUpdateNotes() {
        Notes notes = new Notes();
        //DB = new AdaptadorBD(this);
        String title, content, timeStamp, msj;
        timeStamp = new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(new Date());
        title = TITLE.getText().toString().trim();
        content = CONTENT.getText().toString().trim();
        if (type.equals("add")) {
            if (title.equals("")) {
                msj = "Ingrese un titulo";
                TITLE.requestFocus();
                TITLE.setError("Required");
                Mensaje(msj);
            } else {
                if (content.equals("")) {
                    msj = "Ingrese un contenido";
                    CONTENT.requestFocus();
                    CONTENT.setError("Required");
                    Mensaje(msj);
                } else {
                    //Cursor c = DB.getNote(title);
                    String getTitle = "";
                    /*if (c.moveToFirst()){
                        do {
                            getTitle = c.getString(1);
                        }while (c.moveToNext());
                    }*/
                    if (getTitle.equals(title)) {
                        TITLE.requestFocus();
                        TITLE.setError("Required");
                        msj = "El titulo de la nota ya existe";
                        Mensaje(msj);
                    } else {
                        //DB.addNote(title, content);
                        notes.setIdNote(UUID.randomUUID().toString());
                        notes.setTitle(title);
                        notes.setContent(content);
                        notes.setTimestamp(timeStamp);
                        databaseReference.child("Notes").child(notes.getIdNote()).setValue(notes);
                        actividad(title, content);
                        msj = "La nota se guardo correctamente";
                        Mensaje(msj);
                    }
                }
            }
        } else {
            if (type.equals("edit")){
                if (title.equals("")){
                    msj = "Ingrese un titulo";
                    TITLE.requestFocus();
                    TITLE.setError("Required");
                    Mensaje(msj);
                } else {
                    if (content.equals("")){
                        msj = "Ingrese un contenido";
                        CONTENT.requestFocus();
                        CONTENT.setError("Required");
                        Mensaje(msj);
                    } else {
                        String getTitle = "";
                        //if ()
                        /*Cursor c = DB.getNote(title);
                        if (c.moveToFirst()){
                            do {
                                getTitle = c.getString(1);
                            }while (c.moveToNext());
                        }*/
                        if (!title.equals(pullTitle)){
                            if (getTitle.equals(title)){
                                TITLE.requestFocus();
                                TITLE.setError("Required");
                                msj = "El titulo de la nota ya existe";
                                Mensaje(msj);
                            } else {
                                notes.setIdNote(noteSelected);
                                notes.setTitle(title);
                                notes.setContent(content);
                                notes.setTimestamp(timeStamp);
                                databaseReference.child("Notes").child(notes.getIdNote()).setValue(notes);
                                actividad(title, content);
                                msj = "La nota se actualizo correctamente";
                                Mensaje(msj);
                            }
                        } else {
                            notes.setIdNote(noteSelected);
                            notes.setTitle(title);
                            notes.setContent(content);
                            notes.setTimestamp(timeStamp);
                            databaseReference.child("Notes").child(notes.getIdNote()).setValue(notes);
                            actividad(title, content);
                            msj = "La nota se actualizo correctamente";
                            Mensaje(msj);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            addUpdateNotes();
        }
        return super.onKeyDown(keyCode, event);
    }

    //Metodo para el enviar mensajes al usuario
    public void Mensaje(String msj) {
        Toast toast = Toast.makeText(this, msj, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    //Metodo para enviar los datos a la siguiente actividad
    public void actividad(String title, String content) {
        Intent intent = new Intent(AgregarActivity.this, PrincipalActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
