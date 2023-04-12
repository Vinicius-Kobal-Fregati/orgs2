package br.com.alura.orgs.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.R
import br.com.alura.orgs.databinding.ActivityPerfilUsuarioBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PerfilUsuarioActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityPerfilUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        preencheCampos()
        configuraBotaoSair()
    }

    private fun configuraBotaoSair() {
        binding.activityPerfilUsuarioBotaoSair.setOnClickListener {
            lifecycleScope.launch {
                deslogaUsuario()
            }
        }
    }

    private fun preencheCampos() {
        lifecycleScope.launch {
            usuario
                // Só acessa o collect se não for null
                .filterNotNull()
                .collect { usuarioEncontrado ->
                    binding.activityPerfilUsuarioId.text = usuarioEncontrado.id
                    binding.activityPerfilUsuarioNome.text = usuarioEncontrado.nome
                }
        }
    }
}