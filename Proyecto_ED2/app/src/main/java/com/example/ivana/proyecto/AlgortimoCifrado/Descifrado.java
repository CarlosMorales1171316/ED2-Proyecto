package com.example.ivana.proyecto.AlgortimoCifrado;

public class Descifrado
{
    public  void CrearMatriz(String mensaje , int grado , char[][] caracteres)
    {
        int olas = (grado - 1) * 2;
        int k =0;

        for (int  i = 0 ; i < grado ; i ++ )
        {
            for (int j = i; j < mensaje.length() && k<mensaje.length(); j+=olas)
            {
                int pos2 = j + (olas - i*2);
                caracteres[i][j] = mensaje.charAt(k);
                k++;
                if(i!=0 && i!= grado-1 && pos2 < mensaje.length())
                {
                    caracteres[i][pos2] = mensaje.charAt(k);
                    k++;
                }
            }
        }
    }
    public String MensajeDecifrado(char[][] caracteres, int x, int y)
    {
        String MensajeDecifrado = "";
        for (int  i = 0 ; i < y ; i ++)
        {
            for (int j = 0 ; j < x ; j ++)
            {
                if(caracteres[j][i]!='\0')
                {
                    MensajeDecifrado = MensajeDecifrado+caracteres[j][i];
                }
            }
        }
        return MensajeDecifrado;

    }

}
