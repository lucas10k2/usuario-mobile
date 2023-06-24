package br.unibh.sdm.appusuario.activities;

import static android.os.Build.VERSION_CODES.R;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import br.unibh.sdm.appusuario.R;
import br.unibh.sdm.appusuario.api.UsuarioService;
import br.unibh.sdm.appusuario.api.RestServiceGenerator;
import br.unibh.sdm.appusuario.entidades.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioUsuarioActivity extends AppCompatActivity {

    private UsuarioService service = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(br.unibh.sdm.appusuario.R.layout.activity_fomulario_usuario);
        setTitle("Edição de Usuario");
        service = RestServiceGenerator.createService(UsuarioService.class);
        configuraBotaoSalvar();
        inicializaObjeto();
    }

    @SuppressLint("SetTextI18n")
    private void inicializaObjeto() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra("objeto") != null) {
            Usuario objeto = (Usuario) intent.getSerializableExtra("objeto");
            EditText id = findViewById(br.unibh.sdm.appusuario.R.id.editTextId);
            EditText nome = findViewById(br.unibh.sdm.appusuario.R.id.editTextNome);
            EditText email = findViewById(br.unibh.sdm.appusuario.R.id.editTextEmail);
            EditText senha = findViewById(br.unibh.sdm.appusuario.R.id.editTextSenha);
            id.setText(objeto.getId());
            nome.setText(objeto.getNome());
            email.setText(objeto.getEmail());
            senha.setText(objeto.getSenha());
            id.setEnabled(false);
            Button botaoSalvar = findViewById(br.unibh.sdm.appusuario.R.id.buttonSalvar);
            botaoSalvar.setText("Atualizar");
        }
    }

    private void configuraBotaoSalvar() {
        Button botaoSalvar = findViewById(br.unibh.sdm.appusuario.R.id.buttonSalvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FormularioUsuario","Clicou em Salvar");
                Usuario usuario = recuperaInformacoesFormulario();
                Intent intent = getIntent();
                if (intent.getSerializableExtra("objeto") != null) {
                    Usuario objeto = (Usuario) intent.getSerializableExtra("objeto");
                    usuario.setId(objeto.getId());
                    usuario.setNome(objeto.getNome());
                    if (validaFormulario(usuario)) {
                        atualizaUsuario(usuario);
                    }
                } else {
                    if (validaFormulario(usuario)) {
                        salvaUsuario(usuario);
                    }
                }
            }
        });
    }

    private boolean validaFormulario(Usuario usuario){
        boolean valido = true;
        EditText id = findViewById(br.unibh.sdm.appusuario.R.id.editTextId);
        EditText nome = findViewById(br.unibh.sdm.appusuario.R.id.editTextNome);
        EditText email = findViewById(br.unibh.sdm.appusuario.R.id.editTextEmail);
        EditText senha = findViewById(br.unibh.sdm.appusuario.R.id.editTextSenha);
        if (usuario.getId() == null || usuario.getId().trim().length() == 0){
            id.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            valido = false;
        } else {
            id.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP);
        }
        if (usuario.getNome() == null || usuario.getNome().trim().length() == 0){
            nome.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            valido = false;
        } else {
            nome.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP);
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().length() == 0){
            email.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            valido = false;
        } else {
            email.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP);
        }
        if (usuario.getSenha() == null || usuario.getSenha().trim().length() == 0){
            senha.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            valido = false;
        } else {
            senha.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP);
        }


        if (!valido){
            Log.e("FormularioUsuario", "Favor verificar os campos destacados");
            Toast.makeText(getApplicationContext(), "Favor verificar os campos destacados", Toast.LENGTH_LONG).show();
        }
        return valido;
    }

    private void salvaUsuario(Usuario usuario) {
        Call<Usuario> call = service.criaUsuario(usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Log.i("FormularioUsuario", "Salvou a Usuario "+ usuario.getId());
                    Toast.makeText(getApplicationContext(), "Salvou a Usuario "+ usuario.getId(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.e("FormularioUsuario", "Erro (" + response.code()+"): Verifique novamente os valores");
                    Toast.makeText(getApplicationContext(), "Erro (" + response.code()+"): Verifique novamente os valores", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("FormularioUsuario", "Erro: " + t.getMessage());
            }
        });
    }

    private void atualizaUsuario(Usuario usuario) {
        Log.i("FormularioUsuario","Vai atualizar usuario "+usuario.getId());
        Call<Usuario> call = service.atualizaUsuario(usuario.getId(), usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Log.i("FormularioUsuario", "Atualizou a usuario " + usuario.getId());
                    Toast.makeText(getApplicationContext(), "Atualizou a Usuario " + usuario.getId(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.e("FormularioUsuario", "Erro (" + response.code()+"): Verifique novamente os valores");
                    Toast.makeText(getApplicationContext(), "Erro (" + response.code()+"): Verifique novamente os valores", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("FormularioUsuario", "Erro: " + t.getMessage());
            }
        });
    }

    @NotNull
    private Usuario recuperaInformacoesFormulario() {
        EditText id = findViewById(br.unibh.sdm.appusuario.R.id.editTextId);
        EditText nome = findViewById(br.unibh.sdm.appusuario.R.id.editTextNome);
        EditText email = findViewById(br.unibh.sdm.appusuario.R.id.editTextEmail);
        EditText senha = findViewById(br.unibh.sdm.appusuario.R.id.editTextSenha);
        Usuario usuario = new Usuario();
        usuario.setId(id.getText().toString());
        usuario.setNome(nome.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setSenha(senha.getText().toString());
        return usuario;
    }

}