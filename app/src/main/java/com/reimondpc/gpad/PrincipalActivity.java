package com.reimondpc.gpad;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int ADD = Menu.FIRST;
    private static final int DELETE = Menu.FIRST + 1;
    private static final int EXIT = Menu.FIRST + 2;
    private static final int LOGOUT = Menu.FIRST + 3;
    ListView lvLista;
    TextView tvLista;
    AdaptadorBD DB;
    List<String> item = null;
    String getTitle;

    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        tvLista = (TextView)findViewById(R.id.tvLista);
        lvLista = (ListView)findViewById(R.id.lvLista);
        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getTitle = (String) lvLista.getItemAtPosition(position);
                alert("list");
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new  GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    showNotes();
                } else {
                    goLogInScreen();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        menu.add(1, ADD, 0, R.string.menu_crear);
        menu.add(2, DELETE, 0, R.string.menu_borrar);
        menu.add(3, EXIT, 0, R.string.menu_salir);
        menu.add(4, LOGOUT, 0, R.string.menu_cerrar_sesion);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case ADD:
                actividad("add");
                return true;
            case DELETE:
                alert("deletes");
                return true;
            case EXIT:
                finish();
                return true;
            case LOGOUT:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Metodo para mostrar las notas en la lista
    private void showNotes(){
        DB = new AdaptadorBD(this);
        Cursor c = DB.getNotes();
        item = new ArrayList<String>();
        String title = "";
        //Para asegurar que hay al menos un registro
        if (!c.moveToFirst()){
            //El cursor esta vacio
            tvLista.setText("No hay notas");
        } else {
            do {
                title = c.getString(1);

                item.add(title);
            } while (c.moveToNext());
        }
        //Adaptador de tipo ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, item);
        lvLista.setAdapter(adapter);
    }

    //Metodo para obtener una nota de la lista
    public String getNote(){
        String type = "", content = "";

        DB = new AdaptadorBD(this);
        Cursor c = DB.getNote(getTitle);

        if (c.moveToFirst()){
            do {
                content = c.getString(2);
            } while (c.moveToNext());
        }
        return content;
    }

    public void actividad(String act){
        String type = "", content = "";
        if (act.equals("add")){
            type = "add";
            Intent intent = new Intent(PrincipalActivity.this, AgregarActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
        } else {
            if (act.equals("edit")){
                type = "edit";
                content = getNote();
                Intent intent = new Intent(PrincipalActivity.this, AgregarActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("title", getTitle);
                intent.putExtra("content", content);
                startActivity(intent);
            } else {
                if (act.equals("see")){
                    content = getNote();
                    Intent intent = new Intent(PrincipalActivity.this, VerActivity.class);
                    intent.putExtra("title", getTitle);
                    intent.putExtra("content", content);
                    startActivity(intent);
                }
            }
        }
    }

    private void alert(String f){
        final AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        if (f.equals("list")){
            alerta.setTitle(getTitle)
            .setMessage("¿Que accion desea realizar?")
            .setNeutralButton( "Ver", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actividad("see");
                }
            });
            alerta.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final AlertDialog.Builder alerta2 = new AlertDialog.Builder(PrincipalActivity.this);
                    alerta2.setTitle("Confirmar")
                            .setMessage("¿Deseas eliminar la nota?")
                            .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                    alerta2.setPositiveButton( "Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete("delete");
                            Intent intent = getIntent();
                            startActivity(intent);
                        }
                    });
                    alerta2.show();
                }
            });
            alerta.setNegativeButton( "Editar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actividad("edit");
                }
            });
        } else {
            if (f.equals("deletes")){
                alerta.setTitle("Confirmar")
                .setMessage("¿Deseas eliminar todas las notas??")
                .setNegativeButton( "Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alerta.setPositiveButton( "Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete("deletes");
                        Intent intent = getIntent();
                        startActivity(intent);
                    }
                });
            }
        }
        alerta.show();
    }

    //Metodo para eliminar notas
    public void delete(String f){
        DB = new AdaptadorBD(this);
        if (f.equals("delete")){
            DB.deleteNote(getTitle);
            showNotes();
        } else {
            if (f.equals("deletes")){
                DB.deleteNotes();
                showNotes();
            }
        }
    }

    //Metodo para regresar al login
    private void goLogInScreen(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Metodo para cerrar sesion
    public void logOut(){
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()){
                    goLogInScreen();
                    Toast.makeText(PrincipalActivity.this, "Sesion cerrada con exito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PrincipalActivity.this, "No se pudo cerrar sesion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
}
