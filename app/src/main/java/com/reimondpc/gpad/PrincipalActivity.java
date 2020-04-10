package com.reimondpc.gpad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reimondpc.gpad.Adapters.AdaptadorBD;
import com.reimondpc.gpad.Adapters.AdapterNotes;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterNotes.OnNoteListener {
    private static final int ADD = Menu.FIRST;
    private static final int DELETE = Menu.FIRST + 1;
    private static final int EXIT = Menu.FIRST + 2;
    private static final int LOGOUT = Menu.FIRST + 3;

    private static final String TAG = "PrincipalActivity";

    RecyclerView rvLista;
    TextView tvTitulo;
    ArrayList<Notes> listNotes;
    String getTitle, getContent, noteSelected;
    AdapterNotes adapter;

    FloatingActionButton fabAdd;

    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        initComponent();
        /*rvLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                getTitle = (String) rvLista.getItemAtPosition(position);
                alert("list");
                return true;
            }
        });*/

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividad("add");
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    showNotes();
                } else {
                    goLogInScreen();
                }
            }
        };
    }

    //Metodo para iniciar los componentes
    private void initComponent() {
        tvTitulo = (TextView) findViewById(R.id.tvTitulo);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        listNotes = new ArrayList<>();
        rvLista = (RecyclerView) findViewById(R.id.rvLista);
        rvLista.setLayoutManager(new LinearLayoutManager(this));
        rvLista.setHasFixedSize(true);
        rvLista.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initializerFirebase();
        adapter = new AdapterNotes(listNotes, this);
        showNotes();
    }

    //Metodo para inicializar Firebase
    private void initializerFirebase() {
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users").child(userId);
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
        switch (id) {
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
    private void showNotes() {
        databaseReference.child("Notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listNotes.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Notes notes = data.getValue(Notes.class);
                    listNotes.add(notes);

                    rvLista.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Actividad para saber si crear o editar una nota
    public void actividad(String act) {
        String type = "";
        if (act.equals("add")) {
            type = "add";
            Intent intent = new Intent(PrincipalActivity.this, AgregarActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
        } else {
            if (act.equals("edit")) {
                type = "edit";
                Intent intent = new Intent(PrincipalActivity.this, AgregarActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("title", getTitle);
                intent.putExtra("content", getContent);
                intent.putExtra("noteSelected", noteSelected);
                startActivity(intent);
            }
        }
    }

    //Alerta para eliminar una o mas notas
    private void alert(String f) {
        final AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        if (f.equals("list")) {
            alerta.setTitle(getTitle)
                    .setMessage("¿Deseas eliminar la nota " + getTitle + "?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete("delete");
                            Intent intent = getIntent();
                            startActivity(intent);
                        }
                    });
        } else {
            if (f.equals("deletes")) {
                alerta.setTitle("Confirmar")
                        .setMessage("¿Deseas eliminar todas las notas?")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
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
    public void delete(String f) {
        Notes notes = new Notes();
        if (f.equals("delete")) {
            notes.setIdNote(noteSelected);
            databaseReference.child("Notes").child(notes.getIdNote()).removeValue();
        } else {
            if (f.equals("deletes")) {
                notes.setIdNote(noteSelected);
                databaseReference.child("Notes").removeValue();
            }
        }
    }

    //Metodo para regresar al login
    private void goLogInScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Metodo para cerrar sesion
    public void logOut() {
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
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
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    //Metodo para hacer Click en las notas
    @Override
    public void onNoteClick(int position) {
        noteSelected = listNotes.get(position).getIdNote();
        getTitle = listNotes.get(position).getTitle();
        getContent = listNotes.get(position).getContent();
        actividad("edit");
    }
}
