package com.example.ivana.proyecto.CompresionArchivos;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Huffman
{

    private static char charArray[];
    private static int Ntabla[] = new int[0x7f];
    private static Nodo miNodo[];
    private static int TamañoTabla = 0;
    private static Arbol arbol;
    private static int tamañoNodo = 0;
    public static HuffmanMetodos hC;
    public String tabla ="";


    public void DescomprimirTxt(String tabla,String compreso,String ruta) throws IOException
    {
        String descifrado ="";

        String[] tabla1 = tabla.split("=");
        ArrayList<String> KEY = new ArrayList<String>();
        ArrayList<String> VALUE = new ArrayList<String>();
        for (int i = 0; i < tabla1.length; i++)
        {
            String tempo = tabla1[i];
            String agregar =tempo.substring(1,tempo.length()-1);
            String[] valores = agregar.split(">");
            KEY.add(valores[1]);
            VALUE.add(valores[0]);
        }

        String[] cadena = compreso.split("\\*");

        for (int i = 0; i < KEY.size(); i++)
        {
            boolean condicion = false;
            for (int j = 0; j < cadena.length; j++)
            {
                if(KEY.get(i).equals(cadena[j])&&condicion == false)
                {
                    descifrado +=VALUE.get(i);
                    condicion = true;
                }
                if(cadena[j].length()==0&&condicion == false)
                {
                    descifrado +=VALUE.get(i);
                    condicion = true;
                }

            }



        }

        new File(ruta).createNewFile();
        FileWriter fw = new FileWriter(ruta);
        BufferedWriter out  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ruta),true),"UTF8"));
        fw.write("");
        fw.close();
        out.write(descifrado);
        out.close();

    }

    public String comprimirImagen(String ruta)
    {
        if(ruta.endsWith("jpg")||ruta.endsWith("JPG")) {
            Bitmap bm = BitmapFactory.decodeFile(ruta);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        }
        else
        {
            Bitmap bm = BitmapFactory.decodeFile(ruta);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            return encodedImage;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public void DescomprimirImagen(String imagen, String ruta)
    {

        if(ruta.endsWith("jpg")||ruta.endsWith("JPG"))
        {
            byte[] decodedString = Base64.decode(imagen, Base64.DEFAULT);

            Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            try (FileOutputStream out = new FileOutputStream(ruta)) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            byte[] decodedString = Base64.decode(imagen, Base64.DEFAULT);

            Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            try (FileOutputStream out = new FileOutputStream(ruta)) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Huffman(String value)
    {
        tablaFrecuencia(value);
        organizarNodo();
        Nodo x = CrearArbol();
        hC = new HuffmanMetodos(x,charArray);
        tabla = hC.tabla;
    }

    public static void tablaFrecuencia(String value)
    {
        int i;
        charArray = value.toCharArray();
        for(i = 0; i < charArray.length; i++)
            Ntabla[Ascii(charArray[i])] += 1;
    }

    public static int Ascii(char substringValue)
    {
        return substringValue&0x7f;
    }

    public static void organizarNodo()
    {
        int counter = 0;
        int j = 0;
        for(int i = 0; i < Ntabla.length; i++)
        {
            if(Ntabla[i]>0)
                counter++;
        }

        TamañoTabla =  counter;
        counter = 0;
        miNodo = new Nodo[TamañoTabla];

        for(int i = 0; i < 127; i++)
        {
            if(Ntabla[i] != 0)
            {
                miNodo[counter]= new Nodo(Ntabla[i], (char)i, null, null);
                counter++;
            }
        }
        tamañoNodo = miNodo.length;
        Ordenar();

    }

    public static Nodo CrearArbol()
    {
        for(int i = 1; i < tamañoNodo; i++)
        {
            try
            {
                if(miNodo[1].frecuencia >= miNodo[0].frecuencia)
                {
                    arbol = new Arbol(miNodo[0],miNodo[i]);
                    miNodo[0] = arbol;
                    Valores(i, tamañoNodo);
                    tamañoNodo -= 1;
                    i -= 1;
                    Ordenar();
                }
                else
                {
                    if(i+1 < tamañoNodo)
                    {
                        arbol = new Arbol(miNodo[i], miNodo[i+1]);
                        miNodo[1] = arbol;
                        Valores(i+1, tamañoNodo);
                        Ordenar();
                        tamañoNodo -= 1;
                        i -= 1;
                    }
                    else
                    {
                        miNodo[1] = miNodo[i];
                        miNodo[0] = new Arbol(miNodo[0], miNodo[1]);
                    }
                }
            }
            catch(Exception e)
            {
            }
        }
        return miNodo[0];
    }

    private static void Valores(int index, int length)
    {   try
    {
        for(int i = index; i < length; i++)
            miNodo[i] = miNodo[i+1];

    }
    catch(Exception e)
    {
    }
    }
    private static void Ordenar()
    {
        Nodo temp;
        for(int i = tamañoNodo-1; i > 1; i--)
        {
            for(int j = 0; j < i; j++)
            {
                if(miNodo[j].frecuencia > miNodo[j+1].frecuencia)
                {
                    temp = miNodo[j+1];
                    miNodo[j+1] = miNodo[j];
                    miNodo[j] = temp;
                }

                if(miNodo[j].frecuencia == miNodo[j+1].frecuencia && miNodo[j].izquierda != null)
                {
                    temp = miNodo[j+1];
                    miNodo[j+1] = miNodo[j];
                    miNodo[j] = temp;
                }
            }
        }
    }

}


