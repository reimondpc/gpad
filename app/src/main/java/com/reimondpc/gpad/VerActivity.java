package com.reimondpc.gpad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieSyncManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class VerActivity extends AppCompatActivity {
    private static final int EDIT = Menu.FIRST;
    private static final int DELETE = Menu.FIRST + 1;
    private static final int EXIT = Menu.FIRST + 2;
    String title, content, msj;
    TextView TITLE, CONTENT;
    AdaptadorBD DB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        Bundle bundle = this.getIntent().getExtras();
        title = bundle.getString("title");
        content = bundle.getString("content");

        TITLE = (TextView) findViewById(R.id.tvTitulo);
        CONTENT = (TextView) findViewById(R.id.tvContent);
        TITLE.setText(title);
        CONTENT.setText(content);
    }

    //Metodo para Mostar las opciones del menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        super.onCreateOptionsMenu(menu);
        menu.add(1, EDIT, 0, R.string.menu_editar);
        menu.add(2, DELETE, 0, R.string.menu_eliminar);
        menu.add(3, EXIT, 0, R.string.menu_salir);
        return true;
    }

    //Metodo para darle una accion a las opciones del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case EDIT:
                actividad("edit");
                return true;
            case DELETE:
                alert();
                return true;
            case EXIT:
                actividad("delete");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actividad(String f){
        if (f.equals("edit")){
            String type = "edit";
            Intent intent = new Intent(VerActivity.this, AgregarActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            startActivity(intent);
        } else {
            if (f.equals("delete")){
                CookieSyncManager.createInstance(this);
                Intent intent = new Intent(VerActivity.this, PrincipalActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private void alert(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("Gpad");
        alerta.setMessage("¿Deseas eliminar la nota?");
        alerta.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete();
            }
        });
        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        alerta.show();
    }

    private void  delete(){
        DB = new AdaptadorBD(this);
        DB.deleteNote(title);
        actividad("delete");
        msj = ("La nota ha sido eliminada") ;
        Mensaje(msj);
    }

    //Metodo para el enviar mensajes al usuario
    public void Mensaje(String msj){
        Toast toast = Toast.makeText(this, msj, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }
}
