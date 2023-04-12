package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.extensions.vaiPara
import br.com.alura.orgs.model.Usuario
import br.com.alura.orgs.preferences.dataStore
import br.com.alura.orgs.preferences.usuarioLogadoPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

// Activity base que manterá métodos que serão reutilzados pelas outras activities
abstract class UsuarioBaseActivity : AppCompatActivity() {

    private val usuarioDao by lazy {
        AppDatabase.instancia(this)
            .usuarioDao()
    }
    private var _usuario: MutableStateFlow<Usuario?> = MutableStateFlow(null)
    // Cópia da original, sendo imutável
    protected var usuario: StateFlow<Usuario?> = _usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            verificaUsuarioLogado()
        }
    }

    private suspend fun verificaUsuarioLogado() {
        dataStore.data.collect { preferences ->
            preferences[usuarioLogadoPreferences]?.let { usuarioId ->
                buscaUsuario(usuarioId)
            } ?: vaiParaLogin()
        }
    }

    private suspend fun buscaUsuario(usuarioId: String) {
        // O first or null faz a busca apenas uma vez do flow, ele não se inscreve nele,
        // diferente do collect
        // O firstOrNull não trava a coroutine
        _usuario.value = usuarioDao
            .buscaPorId(usuarioId)
            .firstOrNull()
    }

    protected suspend fun deslogaUsuario() {
        dataStore.edit { preferences ->
            // Esse código vai remover a chave a acionar os flows dela
            preferences.remove(usuarioLogadoPreferences)
        }
    }

    private fun vaiParaLogin() {
        vaiPara(LoginActivity::class.java) {
            // Um comportamento extra que remove todas as activities anteriores
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        // Com esse finish, não é possível do usuário voltar do login para essa tela
        finish()
    }
}