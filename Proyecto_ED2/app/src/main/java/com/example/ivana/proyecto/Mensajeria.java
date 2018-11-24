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

import com.example.ivana.proyecto.AlgoritmoCompresion.ListAdapter;
import com.example.ivana.proyecto.AlgoritmoCompresion.Usuarios;
import com.example.ivana.proyecto.AlgortimoCifrado.Descifrado;
import com.example.ivana.proyecto.api.ContactosApi;
import com.example.ivana.proyecto.api.Mensajes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Mensajeria extends AppCompatActivity {

    private Button btnIniciar;
    private EditText usuario;
    TextView resultado;
    //String ip = "http://192.168.1.13:7000/users/";


    IP ips = new IP();
    String ip =ips.ip;


    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ip).addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();
    ContactosApi api = retrofit.create(ContactosApi.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);
        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        usuario = (EditText) findViewById(R.id.txtNombre);
        resultado = (TextView) findViewById(R.id.Inicio_Validacion2);
        btnIniciar.setOnClickListener(
                new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Mensajeria.this, Chat.class);
                Bundle extras = getIntent().getExtras();
                String receptor = usuario.getText().toString();
                String token = extras.getString("token");
                String usuario = extras.getString("usuario");
                Usuarios(token,usuario,receptor);
            }
        });
    }

    public void Usuarios(String token1, final String emisor, final String receptor)
    {
        Call<ResponseBody> call = api.getSecret(token1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
            {
                if(response.isSuccessful())
                {
                    try
                    {
                        Toast.makeText(Mensajeria.this, "Autorizacion exitosa!!!", Toast.LENGTH_SHORT).show();
                        String cargar = response.body().string();
                        Validar v = new Validar();
                        ArrayList<String> n = new ArrayList<String>();
                        ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
                        n = v.ReadJson(cargar);
                        String nuevoToken = n.get(n.size()-1);
                        n.remove(nuevoToken);
                        boolean validar =false;
                            for (String s : n)
                            {
                                if (s.equals(receptor)&&!s.equals(emisor))
                                {
                                    Intent intent = new Intent(Mensajeria.this, Chat_Usuario.class);
                                    Bundle extras = getIntent().getExtras();
                                    intent.putExtra("token", nuevoToken);
                                    intent.putExtra("emisor", emisor);
                                    intent.putExtra("receptor", receptor);
                                    startActivity(intent);
                                    validar = true;
                                    break;
                                }

                        }
                        if(validar==false)
                        {
                            resultado.setText("Usuario no existente!!!");
                            usuario.setText("");

                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                Toast.makeText(Mensajeria.this,"Autorizacion fallida", Toast.LENGTH_SHORT).show();
            }
        });

    }

    class Validar
    {

        public ArrayList<String> ReadJson(String json)
        {
            ArrayList<String> Json = new ArrayList<String>();
            ArrayList<String> json2 = new ArrayList<String>();
            String s = json.substring(9, json.length() - 1);
            String tempo2 = "";
            boolean validar = true;
            boolean validarToken = false;
            String tempo = "";
            String token ="" ;



            for (int i = 0; i < s.length(); i++)
            {
                String v = s.charAt(i) + "";
                if(validar == true)
                {
                    if(!v.equals("{"))
                    {
                        tempo2+=v;
                    }
                    if(v.equals("}"))
                    {
                        json2.add(tempo2.substring(1,tempo2.length()-1));
                        tempo2="";
                    }
                }
                if(v.equals("]"))
                {
                    tempo2="";
                    validar =false;
                }
                if(validar ==false)
                {
                    tempo2+=v;
                }
            }

            if(s.length()>0)
            {
                token = tempo2.substring(11,tempo2.length()-1);
            }


            String agregar = "";

            for (int i = 0; i < json2.size(); i++)
            {
                String[] campos = json2.get(i).split(",");
                String name = campos[1].substring(12, campos[1].length() - 1);
                Json.add(name);
            }
            Json.add(token);

            return Json;
        }
    }
}
