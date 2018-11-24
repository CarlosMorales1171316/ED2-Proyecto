package com.example.ivana.proyecto;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivana.proyecto.api.Autorizacion;
import com.example.ivana.proyecto.api.Autorizacion2;
import com.example.ivana.proyecto.api.Contactos;
import com.example.ivana.proyecto.api.ContactosApi;
import com.example.ivana.proyecto.api.EnviarMensajes;
import com.example.ivana.proyecto.api.Mensajes;
import com.example.ivana.proyecto.api.logIn;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

     TextView resultado;
    EditText Nombre,Contraseña;
    Button Registro,Inicio;
    public String token ="";
    public String ValidarUsuario="";
    private static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL =1;
    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL =1;
    private String important ="";

   // public String ip ="http://192.168.43.138:7000/users/login";

    IP ips = new IP();
    String ip = ips.ip;
    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ip).addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();
    ContactosApi api = retrofit.create(ContactosApi.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultado= (TextView) findViewById(R.id.Inicio_Validacion);
        Nombre =(EditText) findViewById(R.id.Inicio_Nombre);
        Contraseña =(EditText) findViewById(R.id.Inicio_Contraseña);
        Inicio = (Button) findViewById(R.id.Iniciar_Sesion);
        Registro =(Button) findViewById(R.id.Inicio_Registro);


        CheckPermission();
        CheckPermission2();
        CheckPermission3();
        CheckPermission4();



        Registro.setOnClickListener(
                new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    Intent intent = new Intent(MainActivity.this, Registrar.class);
                    Nombre.setText("");
                    Contraseña.setText("");
                    resultado.setText("");
                    startActivity(intent);
            }
        });

        Inicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String nombre = Nombre.getText().toString();
                String contraseña = Contraseña.getText().toString();
                resultado.setText("");
                if(nombre.length()>0&&contraseña.length()>0)
                {
                logIn(nombre,contraseña);
                }
                else
                {
                    resultado.setText("Debe de llenar todos los campos para iniciar sesion");
                }
            }

        });
    }

    class Validar
    {
        public boolean AutorizacionLogIn(String s)
        {

            boolean permiso = false;

            try {
                String[] v = s.split("\\{");
                String[] v1 = v[1].split("\\{");
                String[] v2 = v1[0].split(",");
                if (v2[0].equals("\"message\":\"¡Autorizado!\"")) {
                    ValidarUsuario = v2[0];
                    permiso = true;
                    String tempo="";
                   token = v2[1].substring(9,v2[1].length()-3);

                } else
                    {
                    ValidarUsuario = "¡No Autorizado!";
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ValidarUsuario = "¡No Autorizado!";
            }
            return permiso;
        }
    }
    class PostData extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Inserting data...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... pararms) {
            try
            {
                return postData(pararms[0]);
            }
            catch (IOException ex)
            {
                return "Network error";
            }
            catch (JSONException ex)
            {
                return "Data Invalid";
            }

        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            Validar validar = new Validar();
            boolean  v= validar.AutorizacionLogIn(result);
            if(v == true) {
                Intent intent = new Intent(MainActivity.this, Chat.class);
                String t = token;
                Nombre.setText("");
                Contraseña.setText("");
                resultado.setText("");
                Bundle extras = getIntent().getExtras();
                intent.putExtra("usuario",Nombre.getText().toString());
                intent.putExtra("token",token);
                startActivity(intent);
            }
            else
            {
                resultado.setText(ValidarUsuario);
            }
            if(progressDialog != null)
            {
                progressDialog.dismiss();
            }

        }
        private String postData(String urlPath)throws IOException, JSONException
        {
            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader= null;

            try {
                JSONObject dataSend = new JSONObject();

                String nombre = Nombre.getText().toString();
                dataSend.put("Username", nombre);

                String contraseña = Contraseña.getText().toString();
                dataSend.put("Contraseña",contraseña);

                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000/* milisegundos*/);
                urlConnection.setConnectTimeout(10000/* milisegundos*/);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataSend.toString());
                bufferedWriter.flush();
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            }
            finally
            {
                if(bufferedReader != null)
                {
                    bufferedReader.close();
                }
                if(bufferedWriter != null)
                {
                    bufferedWriter.close();
                }
            }


            return result.toString();
        }
    }

    private void CheckPermission()
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.INTERNET))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET},MY_PERMISSION_REQUEST_WRITE_EXTERNAL);
            }
        }
    }
    private void CheckPermission2()
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_NETWORK_STATE))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},MY_PERMISSION_REQUEST_READ_EXTERNAL);
            }
        }
    }

    private void CheckPermission3()
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_WRITE_EXTERNAL);
            }
        }
    }
    private void CheckPermission4()
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                //Explicacion permiso
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_READ_EXTERNAL);
            }
        }
    }

    public ArrayList<String> LeerArchivos(File archivo){

        ArrayList<String> temp = new ArrayList<String>();
        try
        {
            String cadenaArchivo;
            FileReader filereader = new FileReader(archivo);
            BufferedReader bufferedreader = new BufferedReader(filereader);
            while((cadenaArchivo = bufferedreader.readLine())!=null) {
                temp.add(cadenaArchivo);
            }
            bufferedreader.close();

        }catch(Exception e){

        }
        return temp;
    }

    public void logIn(final String nombre, String contraseña)
    {
        logIn login = new logIn(nombre,contraseña);
        Call<Autorizacion> call = api.LogIn(login);
        call.enqueue(new Callback<Autorizacion>()
        {
            @Override
            public void onResponse(Call<Autorizacion> call, Response<Autorizacion> response)
            {
                if(response.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Autorizacion exitosa!!!", Toast.LENGTH_SHORT).show();
                    token = response.body().getToken().toString();
                    Intent intent = new Intent(MainActivity.this, Chat.class);
                    intent.putExtra("usuario",nombre);
                    intent.putExtra("token",token);
                    startActivity(intent);
                }
                else
                {
                    resultado.setText("Usuario no existente");
                }
            }

            @Override
            public void onFailure(Call<Autorizacion> call, Throwable t)
            {
                Toast.makeText(MainActivity.this, "Autorizacion fallida!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void Mensajeria(String token1)
    {
        EnviarMensajes mensajeria = new EnviarMensajes("bryan","hola","diego","12/12/2016","puto","lol","nombre");
        Call<Autorizacion2> call = api.Mensajeria(mensajeria);
        call.enqueue(new Callback<Autorizacion2>()
        {
            @Override
            public void onResponse(Call<Autorizacion2> call, Response<Autorizacion2> response)
            {
                if(response.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,response.body().getToken(), Toast.LENGTH_SHORT).show();
                    token = response.body().getToken().toString();
                }
            }

            @Override
            public void onFailure(Call<Autorizacion2> call, Throwable t)
            {
                Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
