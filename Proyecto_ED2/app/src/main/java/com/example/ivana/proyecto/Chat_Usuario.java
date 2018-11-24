package com.example.ivana.proyecto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ivana.proyecto.AlgoritmoCompresion.ListAdapter2;
import com.example.ivana.proyecto.AlgoritmoCompresion.Mensajes;
import com.example.ivana.proyecto.AlgoritmoCompresion.Usuarios;
import com.example.ivana.proyecto.AlgortimoCifrado.Cifrado;
import com.example.ivana.proyecto.AlgortimoCifrado.Descifrado;
import com.example.ivana.proyecto.CompresionArchivos.Huffman;
import com.example.ivana.proyecto.api.AutorizacionPostMensajes;
import com.example.ivana.proyecto.api.AutorizacionRegistro;
import com.example.ivana.proyecto.api.ContactosApi;
import com.example.ivana.proyecto.api.PostMensajes;
import com.example.ivana.proyecto.api.Registro;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chat_Usuario extends AppCompatActivity {

    EditText mensaje;
    ListView Lista;
    private ListAdapter2 adapter;
    Button enviar, archivo;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    String ruta="";
    String compreso="";
    String nombreArchivo ="";
    String mensajeNuevo ="";
    int n =0;
    String tabla ="";
    String tabla2 ="";
    IP ips = new IP();
    String ip =ips.ip;


    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ip).addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();
    ContactosApi api = retrofit.create(ContactosApi.class);




    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__usuario);
        mensaje = (EditText) findViewById(R.id.mensajeEditText);
        Lista =(ListView) findViewById(R.id.mensajeListView);
        enviar = (Button) findViewById(R.id.btnEnviar);
        archivo =(Button) findViewById(R.id.btnEnviarArchivo);
        n=0;
        Intent intent = new Intent(Chat_Usuario.this, Mensajeria.class);
        Bundle extras = getIntent().getExtras();
        String token = extras.getString("token");
        String emisor = extras.getString("emisor");
        String receptor = extras.getString("receptor");



        RecibirMensajes(token);

        archivo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    performFileSearch();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        enviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                n=0;
                Intent intent = new Intent(Chat_Usuario.this, Mensajeria.class);
                Bundle extras = getIntent().getExtras();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String fecha = ""+ (dateFormat.format(date));
                Cifrado cifrar = new Cifrado();
                String emisor = extras.getString("emisor");
                String txt = emisor;
                int grado = 4;

                String m = mensaje.getText().toString();
                String txt2 = m;
                char[][] M2 = new char[grado][txt2.length()];
                cifrar.CrearMatriz(txt2, grado, M2);
                String MensajeCifrado = cifrar.CifrarMensaje(M2, grado, txt2.length());
                String receptor = extras.getString("receptor");
                String token = extras.getString("token");
                PostMensajes post = new PostMensajes(emisor,MensajeCifrado,receptor,fecha,"mensaje",".",".",token);
                SendUsers(post);
                RecibirMensajes(token);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso No Concedido", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private void performFileSearch()
    {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, READ_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            if (data != null)
            {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                if (path.contains("emulated"))
                {
                    path = path.substring(path.indexOf("0") + 1);
                }

                ruta =path;
                Intent intent = new Intent(Chat_Usuario.this, Mensajeria.class);
                Bundle extras = getIntent().getExtras();
                String token = extras.getString("token");
                String receptor = extras.getString("receptor");
                String emisor = extras.getString("emisor");
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String fecha = ""+ (dateFormat.format(date));

                if(path.endsWith("jpg")||path.endsWith("JPG")||path.endsWith("PNG")||path.endsWith("png")) {
                    String ruta = "/storage/emulated/0/" + path;
                    Huffman huffman = new Huffman("prueba");
                    String s = huffman.comprimirImagen(ruta);
                    compreso =s;
                    String name2 = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
                    nombreArchivo = "/storage/emulated/0/Download/"+name2+"_ED2.jpg";
                    mensajeNuevo = "has recibo un archivo con el nombre de "+name2+"_ED2.jpg y se ubica en la carpeta de descargas!!";
                    PostMensajes post = new PostMensajes(emisor,compreso,receptor,fecha,"archivo jpg",".",nombreArchivo,token);
                    PostMensajes post2 = new PostMensajes(emisor,    mensajeNuevo ,receptor,fecha,"mensaje",".",".",token);
                    SendUsers(post);
                    SendUsers(post2);
                    RecibirMensajes(token);
                }
                if(path.endsWith("txt"))
                {
                    ArrayList<String> mensaje = LeerArchivos(new File("/storage/emulated/0/" + path));
                    String mensajenuevo ="";
                    for(String v :mensaje)
                    {
                        mensajenuevo+=v;
                    }
                    Huffman huffman =  new Huffman(mensajenuevo);
                    String compresion=huffman.hC.FinalBit;
                    String compreso1 = compresion.substring(0,compresion.length()-1);
                    tabla = huffman.tabla;
                    compreso =compreso1;
                    String file = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
                    nombreArchivo = "/storage/emulated/0/Download/"+file+"_ED2.txt";
                    mensajeNuevo = "has recibo el archivo "+file+".txt y se ha descargado en tu carpeta download";
                    PostMensajes post = new PostMensajes(emisor,compreso,receptor,fecha,"archivo txt",tabla,nombreArchivo,token);
                    PostMensajes post2 = new PostMensajes(emisor,mensajenuevo,receptor,fecha,"mensaje",".",".",token);
                    SendUsers(post);
                    SendUsers(post2);
                    RecibirMensajes(token);
                }

            }
        }
    }


    class PostData extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Chat_Usuario.this);
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mensaje.setText("");
            if(n==1)
            {
                new PostData2().execute("http://192.168.1.13:7000/mensajes/");
            }
            if(n==0)
            {
                new GetData().execute("http://192.168.1.13:7000/mensajes/");
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
                Intent intent = new Intent(Chat_Usuario.this, Mensajeria.class);
                Bundle extras = getIntent().getExtras();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String fecha = ""+ (dateFormat.format(date));
                String emisor = extras.getString("usuario");
                String receptor = extras.getString("receptor");

                if(n==0)
                {

                Cifrado cifrar = new Cifrado();
                String txt = emisor.toUpperCase();
                int grado = 4;
                char[][] M = new char[grado][txt.length()];
                cifrar.CrearMatriz( txt ,grado, M);
                String nombreCifrado1 = cifrar.CifrarMensaje(M,grado, txt.length());
                dataSend.put("Emisor", nombreCifrado1);

                    String m = mensaje.getText().toString();
                    String txt2 = m.toUpperCase();
                    char[][] M2 = new char[grado][txt2.length()];
                    cifrar.CrearMatriz(txt2, grado, M2);
                    String MensajeCifrado = cifrar.CifrarMensaje(M2, grado, txt2.length());
                    dataSend.put("Mensaje", MensajeCifrado);

                    String txt3 = receptor.toUpperCase();
                    char[][] M3 = new char[grado][txt3.length()];
                    cifrar.CrearMatriz(txt3, grado, M3);
                    String nombreCifrado2 = cifrar.CifrarMensaje(M3, grado, txt3.length());
                    dataSend.put("Receptor", nombreCifrado2);

                    dataSend.put("Fecha", fecha);

                    dataSend.put("Asunto", "mensaje");
                    dataSend.put("Tabla",".");
                    dataSend.put("NombreArchivo",".");
                }

                if(n==1)
                {
                    Cifrado cifrar = new Cifrado();
                    String txt = emisor.toUpperCase();
                    int grado = 4;
                    char[][] M = new char[grado][txt.length()];
                    cifrar.CrearMatriz(txt, grado, M);
                    String nombreCifrado1 = cifrar.CifrarMensaje(M, grado, txt.length());
                    dataSend.put("Emisor", nombreCifrado1);

                    String m = compreso;
                    dataSend.put("Mensaje",m);

                    String txt3 = receptor.toUpperCase();
                    char[][] M3 = new char[grado][txt3.length()];
                    cifrar.CrearMatriz(txt3, grado, M3);
                    String nombreCifrado2 = cifrar.CifrarMensaje(M3, grado, txt3.length());
                    dataSend.put("Receptor", nombreCifrado2);

                    dataSend.put("Fecha", fecha);

                    if(ruta.endsWith("jpg")||ruta.endsWith("JPG"))
                    {
                        dataSend.put("Asunto", "archivo jpg");
                        dataSend.put("Tabla","jpg");
                        dataSend.put("NombreArchivo",nombreArchivo);
                    }
                    if(ruta.endsWith("txt")) {
                        dataSend.put("Asunto", "archivo txt");
                        dataSend.put("Tabla",tabla);
                        dataSend.put("NombreArchivo",nombreArchivo);
                    }
                    n=1;
                }

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
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }
            return result.toString();
        }
    }

    class PostData2 extends AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Chat_Usuario.this);
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mensaje.setText("");
            new GetData().execute("http://192.168.1.13:7000/mensajes/");
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
                Intent intent = new Intent(Chat_Usuario.this, Mensajeria.class);
                Bundle extras = getIntent().getExtras();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String fecha = ""+ (dateFormat.format(date));
                String emisor = extras.getString("usuario");
                String receptor = extras.getString("receptor");

                    Cifrado cifrar = new Cifrado();
                    String txt = emisor.toUpperCase();
                    int grado = 4;
                    char[][] M = new char[grado][txt.length()];
                    cifrar.CrearMatriz(txt, grado, M);
                    String nombreCifrado1 = cifrar.CifrarMensaje(M, grado, txt.length());
                    dataSend.put("Emisor", nombreCifrado1);

                    String m = receptor + " " + mensajeNuevo;
                    String txt5 = m.toUpperCase();
                    char[][] M5 = new char[grado][txt5.length()];
                    cifrar.CrearMatriz(txt5, grado, M5);
                    String Mensaje = cifrar.CifrarMensaje(M5, grado, txt5.length());
                    dataSend.put("Mensaje", Mensaje);

                    String txt3 = receptor.toUpperCase();
                    char[][] M3 = new char[grado][txt3.length()];
                    cifrar.CrearMatriz(txt3, grado, M3);
                    String nombreCifrado2 = cifrar.CifrarMensaje(M3, grado, txt3.length());
                    dataSend.put("Receptor", nombreCifrado2);

                    dataSend.put("Fecha", fecha);

                    dataSend.put("Asunto", "mensaje");
                dataSend.put("Tabla",".");
                dataSend.put("NombreArchivo",".");


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


    class GetData extends AsyncTask<String, Void,String>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(Chat_Usuario.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String...pararms) {
            try {
                return getData(pararms[0]);
            }
            catch (IOException e)
            {
                return "Network error!!!!";
            }
        }

        @Override
        protected void onPostExecute(String res)
        {
            super.onPostExecute(res);
            Bundle extras = getIntent().getExtras();
            String usuarioEmisor = extras.getString("usuario");
            String usuarioReceptor = extras.getString("receptor");
            Descifrado descifrado =  new Descifrado();
            int grado =4;
            Validar validar = new Validar();
            ArrayList<String> json = validar.ReadJsonMensaje(res);

            ArrayList<Mensajes> ListaMensajes = new ArrayList<Mensajes>();
            String va ="";
            for(int i= 0;i<json.size();i++)
            {
                String[] v = json.get(i).split("=>");
                if(v[4].equals("mensaje"))
                {
                    String txt1 = v[0].toUpperCase();
                    char[][] M1 = new char[grado][txt1.length()];
                    descifrado.CrearMatriz(txt1, grado, M1);
                    String emisor = descifrado.MensajeDecifrado(M1, grado, txt1.length());

                    String txt3 = v[2].toUpperCase();
                    char[][] M3 = new char[grado][txt3.length()];
                    descifrado.CrearMatriz(txt3, grado, M3);
                    String receptor = descifrado.MensajeDecifrado(M3, grado, txt3.length());

                    String txt2 = v[1].toUpperCase();
                    char[][] M2 = new char[grado][txt2.length()];
                    descifrado.CrearMatriz(txt2, grado, M2);
                    String mensaje = descifrado.MensajeDecifrado(M2, grado, txt2.length());

                    String d = emisor + " => " + receptor;

                    if (usuarioEmisor.toLowerCase().equals(emisor.toLowerCase()) && usuarioReceptor.toLowerCase().equals(receptor.toLowerCase()) || usuarioEmisor.toLowerCase().equals(receptor.toLowerCase()) && usuarioReceptor.toLowerCase().equals(emisor.toLowerCase())) {

                        ListaMensajes.add(new Mensajes(d.toLowerCase(), mensaje.toLowerCase(), "   " + v[3]));
                    }
                }

                if (v[4].equals("archivo txt") || v[4].equals("archivo jpg"))
                {
                    if (v[5].length() > 2) {
                        if (v[4].endsWith("txt")) {
                            tabla2 = v[5];
                            Descomprimir(v[1], v[6]);

                        }
                    }
                    if (v[5].endsWith("jpg")||v[5].endsWith("png")||v[5].endsWith("JPG")||v[5].endsWith("PNG")) {

                        Descomprimir(v[1], v[6]);
                    }
                }

            }

            adapter = new ListAdapter2(getApplicationContext(),ListaMensajes);
            Lista.setAdapter(adapter);

            if(progressDialog !=null)
            {
                progressDialog.dismiss();
            }
        }
        private String getData(String urlPath)throws IOException
        {
            StringBuilder result = new StringBuilder();

            BufferedReader bufferedReader =null;
            try
            {
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(1000000/* milisegundos*/);
                urlConnection.setConnectTimeout(1000000/* milisegundos*/);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line= bufferedReader.readLine())!=null)
                {
                    result.append(line).append("\n");
                }
            }
            finally {
                if(bufferedReader!=null)
                {
                    bufferedReader.close();
                }
            }



            return result.toString();
        }
    }

    class Validar
    {
        public ArrayList<String> ReadJsonMensaje(String json)
        {
            String s= json.substring(1, json.length()-1);
            ArrayList<String> Json = new ArrayList<String>();
            String tempo ="";
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
                        String agregar = tempo.substring(1,tempo.length()-2)+"}";
                        Json.add(agregar);
                    }
                    if(d.equals(","))
                    {
                        String agregar = tempo.substring(2,tempo.length()-2)+"}";

                        Json.add(agregar);
                    }
                    tempo ="";

                }
            }
            String tempo2 ="";
            String agregar ="";
            boolean validar =false;
            ArrayList<String> json2 = new ArrayList<String>();
            for (int i = 0; i < Json.size(); i++)
            {
                String j = Json.get(i);
                for (int k = 0; k < j.length(); k++)
                {
                    String d = j.charAt(k)+"";
                    tempo2 +=d;
                    if(tempo.endsWith("_id:"))
                    {
                        tempo2 ="";
                    }

                    if(tempo2.endsWith("Emisor"))
                    {
                        tempo2 =""   ;
                    }
                    if(tempo2.endsWith("Mensaje"))
                    {
                        agregar =(tempo2.substring(3,tempo2.length()-10));
                        tempo2 ="";
                    }
                    if(tempo2.endsWith("Receptor"))
                    {
                        agregar =agregar+"=>"+ tempo2.substring(3,tempo2.length()-11);
                        tempo2 ="";
                    }
                    if(tempo2.endsWith("Receptor"))
                    {
                        agregar =agregar+"=>"+ (tempo2.substring(3,tempo2.length()-11));
                        tempo2 ="";
                    }
                    if(tempo2.endsWith("Fecha"))
                    {
                        agregar =agregar+"=>"+(tempo2.substring(3,tempo2.length()-8));
                        tempo2 ="";
                    }
                    if(tempo2.endsWith("Asunto"))
                    {
                        agregar =agregar+"=>"+(tempo2.substring(14,tempo2.length()-9));
                        tempo2 ="";
                    }
                    if(tempo2.endsWith("Tabla"))
                    {
                        agregar =agregar+"=>"+(tempo2.substring(3,tempo2.length()-8));
                        tempo2 ="";
                    }
                    if(tempo2.endsWith("NombreArchivo"))
                    {
                        agregar =agregar+"=>"+(tempo2.substring(3,tempo2.length()-16));
                        tempo2 ="";
                    }
                    if(tempo2.endsWith("}"))
                    {
                        agregar =agregar+"=>"+(tempo2.substring(3,tempo2.length()-2));
                        json2.add(agregar);
                        tempo2 ="";
                    }
                }
                String g="";
            }
            return json2;
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

    public void Descomprimir(String compresion,String path)
    {
        if(path.endsWith("jpg")||ruta.endsWith("JPG")||path.endsWith("PNG")||path.endsWith("png"))
        {
            Huffman huffman = new Huffman("hola");
            huffman.DescomprimirImagen(compresion, path);
        }
        if(path.endsWith("txt"))
        {
            Huffman huffman = new Huffman("hola");
            try {
                huffman.DescomprimirTxt(tabla2,compresion,path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void RecibirMensajes(String token1)
    {
        Call<ResponseBody> call = api.getMensajes(token1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
            {
                if(response.isSuccessful())
                {
                  //  Toast.makeText(Chat_Usuario.this, "Autorizacion exitosa!!!", Toast.LENGTH_SHORT).show();
                    try {
                        String cargar = response.body().string();
                        Bundle extras = getIntent().getExtras();
                        String usuarioEmisor = extras.getString("emisor");
                        String usuarioReceptor = extras.getString("receptor");
                        Descifrado descifrado =  new Descifrado();
                        int grado =4;
                        Validar validar = new Validar();
                        ArrayList<String> json = validar.ReadJsonMensaje(cargar);

                        ArrayList<Mensajes> ListaMensajes = new ArrayList<Mensajes>();
                        try {
                            for (int i = 0; i < json.size(); i++) {
                                String[] v = json.get(i).split("=>");
                                if (v[4].equals("mensaje")) {

                                    String emisor = v[0];


                                    String receptor = v[2];

                                    String txt2 = v[1];
                                    char[][] M2 = new char[grado][txt2.length()];
                                    descifrado.CrearMatriz(txt2, grado, M2);
                                    String mensaje = descifrado.MensajeDecifrado(M2, grado, txt2.length());

                                    String d = emisor + " => " + receptor;

                                    if (usuarioEmisor.equals(emisor) && usuarioReceptor.equals(receptor) || usuarioEmisor.equals(receptor) && usuarioReceptor.equals(emisor)) {

                                        ListaMensajes.add(new Mensajes(d, mensaje, "   " + v[3]));
                                    }
                                }

                                if (v[4].equals("archivo txt") || v[4].equals("archivo jpg")) {
                                    if (v[5].length() > 2) {
                                        if (v[4].endsWith("txt")) {
                                            tabla2 = v[5];
                                            Descomprimir(v[1], v[6]);

                                        }
                                    }
                                    if (v[5].endsWith("jpg") || v[5].endsWith("png") || v[5].endsWith("JPG") || v[5].endsWith("PNG")) {

                                        Descomprimir(v[1], v[6]);
                                    }
                                }

                            }

                            adapter = new ListAdapter2(getApplicationContext(), ListaMensajes);
                            Lista.setAdapter(adapter);

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                Toast.makeText(Chat_Usuario.this,"Autorizacion fallida", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SendUsers(PostMensajes post)
    {
        Call<AutorizacionPostMensajes> call = api.EnviarMensaje(post);
        call.enqueue(new Callback<AutorizacionPostMensajes>() {
            @Override
            public void onResponse(Call<AutorizacionPostMensajes> call, Response<AutorizacionPostMensajes> response)
            {
                    Toast.makeText(Chat_Usuario.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AutorizacionPostMensajes> call, Throwable t)
            {
                Toast.makeText(Chat_Usuario.this,"Autentifacion fallida",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
