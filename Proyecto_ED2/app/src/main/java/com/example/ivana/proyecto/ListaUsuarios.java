package com.example.ivana.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.example.ivana.proyecto.AlgoritmoCompresion.ListAdapter;
import com.example.ivana.proyecto.AlgoritmoCompresion.ListAdapter2;
import com.example.ivana.proyecto.AlgoritmoCompresion.Usuarios;
import com.example.ivana.proyecto.api.ContactosApi;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListaUsuarios extends AppCompatActivity {

    private ListAdapter adapter;
    String token = "";
    ListView lista;
    Button b;
    private RequestQueue requestQueue;
    private static ListaUsuarios mInstance;

    IP ips = new IP();
    String ip =ips.ip;


    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ip).addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();
    ContactosApi api = retrofit.create(ContactosApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);
        lista = (ListView) findViewById(R.id.Lista);
        b = (Button) findViewById(R.id.Regresar);

        Intent intent = new Intent(ListaUsuarios.this, Chat.class);
        Bundle extras = getIntent().getExtras();
        token = extras.getString("token");
        Usuarios(token);
        b.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Usuarios2(token);
                    }
                    });
               }


        class Validar {
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


    public void Usuarios(String token1)
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
                        Toast.makeText(ListaUsuarios.this, "Autorizacion exitosa!!!", Toast.LENGTH_SHORT).show();
                        String cargar = response.body().string();
                        Validar v = new Validar();
                        ArrayList<String> n = new ArrayList<String>();
                        ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
                        n = v.ReadJson(cargar);
                        String nuevoToken = n.get(n.size()-1);
                        n.remove(nuevoToken);
                        for(int i=0;i<n.size();i++)
                        {
                            listaUsuarios.add(new Usuarios(n.get(i)));
                        }
                        try {
                            adapter = new ListAdapter(getApplicationContext(), listaUsuarios);
                            lista.setAdapter(adapter);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(ListaUsuarios.this, "Autorizacion fallida!!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ListaUsuarios.this,"Autorizacion fallida", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void Usuarios2(String token1)
    {
        Call<ResponseBody> call = api.getSecret(token1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
            {
                if(response.isSuccessful())
                {
                    try {
                        //Toast.makeText(ListaUsuarios.this, "Autorizacion exitosa!!!", Toast.LENGTH_SHORT).show();
                        String cargar = response.body().string();
                        Validar v = new Validar();
                        ArrayList<String> n = new ArrayList<String>();
                        ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
                        n = v.ReadJson(cargar);
                        String nuevoToken = n.get(n.size() - 1);
                        n.remove(nuevoToken);
                        Intent intent = new Intent(ListaUsuarios.this, Chat.class);
                        Bundle extras = getIntent().getExtras();
                        intent.putExtra("token",nuevoToken);
                        startActivity(intent);

                    }
                        catch (Exception e)
                        {
                            Toast.makeText(ListaUsuarios.this, "Autorizacion fallida!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                Toast.makeText(ListaUsuarios.this,"Autorizacion fallida", Toast.LENGTH_SHORT).show();
            }
        });

    }





}
