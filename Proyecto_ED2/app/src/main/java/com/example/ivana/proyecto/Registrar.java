package com.example.ivana.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivana.proyecto.AlgortimoCifrado.Cifrado;
import com.example.ivana.proyecto.AlgortimoCifrado.Descifrado;
import com.example.ivana.proyecto.api.AutorizacionRegistro;
import com.example.ivana.proyecto.api.ContactosApi;
import com.example.ivana.proyecto.api.Registro;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registrar extends AppCompatActivity {

    TextView resultado;
    EditText Nombre,NombreUsuario,Contraseña,Correo;
    Button Registro;
    public String ValidarEmail ="",ValidarUsuario="";
    IP ips = new IP();
    String ip = ips.ip;
    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ip).addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();
    ContactosApi api = retrofit.create(ContactosApi.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        resultado = (TextView) findViewById(R.id.MensajeValidacion);
        Nombre = (EditText) findViewById(R.id.NombreUsuario);
        NombreUsuario = (EditText) findViewById(R.id.Nombre);
        Contraseña = (EditText) findViewById(R.id.Registrar_Contraseña);
        Correo = (EditText) findViewById(R.id.Registrar_Correo);
        Registro = (Button) findViewById(R.id.ValidarRegistro);

        Registro.setOnClickListener(
                new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String correo = Correo.getText().toString();
                String c1 = Contraseña.getText().toString();
                String c2 = NombreUsuario.getText().toString();
                String n = Nombre.getText().toString();
                boolean condicion2 = false;
                int contador=0;
                for(int i=0;i<correo.length();i++)
                {
                    String d = correo.charAt(i)+"";
                    if(d.equals("@"))
                    {
                      contador++;
                    }
                    if(correo.endsWith(".com") &&condicion2 ==false)
                    {
                        contador++;
                        condicion2=true;
                    }
                    if(correo.endsWith(".es") &&condicion2 ==false)
                    {
                        contador++;
                        condicion2=true;
                    }
                }

                if(c1.length()>0 &&c2.length()>0&&n.length()>0&& contador ==2)
                {
                    Registro r = new Registro(n,c1,c2,correo);
                    SendUsers(r);
                }
                if(c1.length()==0)
                {
                    resultado.setText("");
                    Nombre.setText("");
                    Contraseña.setText("");
                    NombreUsuario.setText("");
                    resultado.setText("Debe de ingresar una contraseña");
                }
                if(c2.length()==0)
                {
                    resultado.setText("");
                    Nombre.setText("");
                    Contraseña.setText("");
                    NombreUsuario.setText("");
                    resultado.setText("Debe de ingresar su nombre");
                }
                if(n.length()==0)
                {
                    resultado.setText("");
                    Nombre.setText("");
                    Contraseña.setText("");
                    NombreUsuario.setText("");
                    resultado.setText("Debe de ingresar nombre de usuario");
                }
                if(contador!=2)
                {
                    resultado.setText("Path `Correo` is invalid ("+(correo)+")");
                    Correo.setText("");
                }
            }
        });


    }

    class Validar
    {
        public ArrayList<String> ReadJson(String json)
        {
            String s= json.substring(1, json.length()-1);
            ArrayList<String> Json = new ArrayList<String>();
            String tempo ="";
            int contador = 0;
            for (int i = 0; i < s.length(); i++)
            {
                String v = s.charAt(i)+"";

                if(v.equals("{"))
                {
                    tempo += v;
                }
                if(!v.equals("{") &&!v.equals("]"))
                {
                    tempo += v;
                }
                if(v.equals("}"))
                {
                    tempo = tempo+v;
                    String d = tempo.substring(0,1);
                    if(!d.equals(","))
                    {
                        String agregar = tempo.substring(1,tempo.length()-2);
                        Json.add(agregar);
                    }
                    if(d.equals(","))
                    {
                        String agregar = tempo.substring(2,tempo.length()-2);
                        Json.add(agregar);
                    }
                    tempo ="";

                }
            }
            ArrayList<String> Json2 = new ArrayList<String>();
            for (int i = 0; i < Json.size(); i++)
            {
                String[] campos = Json.get(i).split(",");
                String name = campos[1].substring(12,campos[1].length()-1);
                Json2.add(name+","+"lol");
            }
            return Json2;
        }

        public boolean Autorizacion(String s)
        {

            boolean permiso = false;
            try {
               if(s.equals("¡Usuario creado con exito!"))
               {
                   permiso =true;
               }
               else
               {
                   resultado.setText("Username ya existente, intente con otro");
               }
            }
            catch (Exception e)
            {
                resultado.setText("Error de autenticacion!!");
            }

            return permiso;
        }

        public boolean ValidarCorreo(String s)
        {
            boolean validar = true;
            String f = s.substring(48,72);
            if(f.equals("Path `Correo` is invalid"))
            {
                ValidarEmail =f;
                validar =false;
            }
            return validar;
        }
    }

    private void SendUsers(Registro users)
    {
        Call<AutorizacionRegistro> call = api.Registro(users);
        call.enqueue(new Callback<AutorizacionRegistro>() {
            @Override
            public void onResponse(Call<AutorizacionRegistro> call, Response<AutorizacionRegistro> response)
            {
                try {
                    Toast.makeText(Registrar.this, "", Toast.LENGTH_SHORT).show();
                    String v = response.body().getMessage();
                    Validar va = new Validar();
                    boolean q = va.Autorizacion(v);
                    if (q == true) {
                        Toast.makeText(Registrar.this, "Autentifacion valida", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registrar.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                catch (Exception e)
                {
                    resultado.setText("Error de autenticacion");
                }
            }

            @Override
            public void onFailure(Call<AutorizacionRegistro> call, Throwable t)
            {
                Toast.makeText(Registrar.this,"Autentifacion valida",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

