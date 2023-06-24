package br.unibh.sdm.appusuario.api;

import java.util.List;

import br.unibh.sdm.appusuario.entidades.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsuarioService {

    @Headers({
            "Accept: application/json",
            "User-Agent: AppUsuario"
    })
    @GET("usuario")
    Call<List<Usuario>> getUsuarios();

    @GET("usuario/{id}")
    Call<Usuario> getUsuario(@Path("id") String id);

    @POST("usuario")
    Call<Usuario> criaUsuario(@Body Usuario usuario);

    @PUT("usuario/{id}")
    Call<Usuario> atualizaUsuario(@Path("id") String id, @Body Usuario usuario);

    @DELETE("usuario/{id}")
    Call<Boolean> excluiUsuario(@Path("id") String id);

}