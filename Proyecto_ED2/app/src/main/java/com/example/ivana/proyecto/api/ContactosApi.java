package com.example.ivana.proyecto.api;
import javax.security.auth.login.LoginException;
import com.example.ivana.proyecto.api.logIn;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ContactosApi
{
 @POST("/users/login")
 Call<Autorizacion> LogIn(@Body logIn logIn);
 @GET("/users")
 Call<ResponseBody> getSecret(@Header("Authorization") String authtoken);
 @POST("/mensajes")
 Call<Autorizacion2> Mensajeria(@Body EnviarMensajes mensaje);
@POST("/users/registrarse")
 Call<AutorizacionRegistro> Registro(@Body Registro registro);
@GET("/mensajes")
Call<ResponseBody> getMensajes(@Header("Authorization") String authtoken);

@POST("/mensajes")
 Call<AutorizacionPostMensajes> EnviarMensaje(@Body PostMensajes postMensajes);

}
