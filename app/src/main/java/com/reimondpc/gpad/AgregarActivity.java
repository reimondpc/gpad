package com.reimondpc.gpad;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AgregarActivity extends AppCompatActivity {
    String type, getTitle, content;
    Button add;
    EditText TITLE, CONTENT;
    private static final int DELETE = Menu.FIRST;
    AdaptadorBD DB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar);
        add = (Button) findViewById(R.id.btnAgregar);
        TITLE = (EditText) findViewById(R.id.etTitulo);
        CONTENT = (EditText) findViewById(R.id.etNotas);

        Bundle bundle = this.getIntent().getExtras();
        getTitle = bundle.getString("title");
        content = bundle.getString("content");
        type = bundle.getString("type");

        if (type.equals("add")){
            add.setText("Agregar Nota");
        } else {
            if (type.equals("edit")){
                TITLE.setText(getTitle);
                CONTENT.setText(content);
                add.setText("Actualizar Nota");
            }
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUpdateNotes();
            }
        });
    }

    //Metodo para Mostar las opciones del menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        super.onCreateOptionsMenu(menu);
        menu.add(1, DELETE, 0, R.string.menu_salir);
        return true;
    }

    //Metodo para darle una accion a las opciones del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case DELETE:
                CookieSyncManager.createInstance(this);
                Intent intent = new Intent(AgregarActivity.this, PrincipalActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Metodo para actualizar las notas
    private void addUpdateNotes(){
        DB = new AdaptadorBD(this);
        String title, content, msj;
        title = TITLE.getText().toString();
        content = CONTENT.getText().toString();
        if (type.equals("add")){
            if (title.equals("")){
                msj = "Ingrese un titulo";
                TITLE.requestFocus();
                Mensaje(msj);
            } else {
                if (content.equals("")){
                    msj = "Ingrese un contenido";
                    CONTENT.requestFocus();
                    Mensaje(msj);
                } else {
                    Cursor c = DB.getNote(title);
                    String getTitle = "";
                    if (c.moveToFirst()){
                        do {
                            getTitle = c.getString(1);
                        }while (c.moveToNext());
                    }
                    if (getTitle.equals(title)){
                        TITLE.requestFocus();
                        msj = "El titulo de la nota ya existe";
                        Mensaje(msj);
                    } else {
                        DB.addNote(title, content);
                        actividad(title, content);
                        msj = "La nota se guardo correctamente";
                        Mensaje(msj);
                    }
                }
            }
        } else {
            if (type.equals("edit")){
                add.setText("Actualizar Nota");
                if (title.equals("")){
                    msj = "Ingrese un titulo";
                    TITLE.requestFocus();
                    Mensaje(msj);
                } else {
                    if (content.equals("")){
                        msj = "Ingrese un contenido";
                        CONTENT.requestFocus();
                        Mensaje(msj);
                    } else {
                        Cursor c = DB.getNote(title);
                        String getTitle = "";
                        if (c.moveToFirst()){
                            do {
                                getTitle = c.getString(1);
                            }while (c.moveToNext());
                        }
                        if (getTitle.equals(title)){
                            TITLE.requestFocus();
                            msj = "El titulo de la nota ya existe";
                            Mensaje(msj);
                        } else {
                            DB.updateNote(title, content, getTitle);
                            actividad(title, content);
                            msj = "La nota se actualizo correctamente";
                            Mensaje(msj);
                        }
                    }
                }
            }
        }
    }

    //Metodo para el enviar mensajes al usuario
    public void Mensaje(String msj){
        Toast toast = Toast.makeText(this, msj, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    //Metodo para enviar los datos a la siguiente actividad
    public void actividad(String title, String content){
        Intent intent = new Intent(AgregarActivity.this, VerActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        startActivity(intent);
    }
}
