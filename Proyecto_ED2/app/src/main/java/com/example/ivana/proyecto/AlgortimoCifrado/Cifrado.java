package com.example.ivana.proyecto.AlgortimoCifrado;
import java.math.BigInteger;
import java.util.ArrayList;
public class Cifrado
{
    public void CrearMatriz(String mensaje , int grado , char[][] caracteres)
    {
        int olas = (grado - 1) * 2;

        for (int  i = 0 ; i < grado ; i ++ )
        {
            for (int j = i ; j < mensaje.length(); j+=olas)
            {
                int pos2 = j + (olas - i*2);
                caracteres[i][j] = mensaje.charAt(j);
                if(pos2 < mensaje.length())
                {
                    caracteres[i][pos2] = mensaje.charAt(pos2);

                }

            }
        }
    }


    public String CifrarMensaje(char[][] caracteres, int x, int y){
        String mensaje = "";
        for (int  i = 0 ; i < x ; i ++)
        {
            for (int j = 0 ; j < y ; j ++)
            {
                if(caracteres[i][j]!='\0')
                {
                    mensaje = mensaje+caracteres[i][j];

                }

            }
        }
        return mensaje;

    }
}
