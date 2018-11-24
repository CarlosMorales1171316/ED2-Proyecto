package com.example.ivana.proyecto.CompresionArchivos;

public class HuffmanMetodos
{
    private Nodo rutaNodo;
    private char c;
    private char charArray[];
    private int i=0;
    public String FinalBit = "";
    public String tabla ="";

    public HuffmanMetodos(Nodo miNodo, char [] charArray)
    {
        String temp;
        int i;

        rutaNodo = miNodo;
        this.charArray = charArray;
        for(i = 0; i < charArray.length; i++)
        {
            temp = Buscar(rutaNodo, "", charArray[i]);
            FinalBit += temp+"*";
            if(temp.length()==0)
            {
                temp = charArray[i]+"";
            }
            tabla += ("|"+charArray[i]+">"+temp+"|=");
        }
    }

    public String Buscar(Nodo rutNodo, String valor,char myChar)
    {
        String valueL ="";
        if(rutNodo != null)
        {
            if(rutNodo.izquierda != null)
                valueL = Buscar(rutNodo.izquierda, valor+"0", myChar);
            if(rutNodo.c == myChar)
                return valor;

            else
            {
                if(valueL == "")
                {
                    return Buscar(rutNodo.derecha, valor+"1",myChar);
                }
                else
                {
                    return valueL;
                }
            }
        }
        else
        {
            return valueL;
        }
    }


}

