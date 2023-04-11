package br.com.alura.orgs.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

// Assim conseguimos criar nosso data store
// Dessa forma, todos que herdam de Context conseguem acessar ele
val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "sessao_usuario")

val usuarioLogadoPreferences = stringPreferencesKey("usuarioLogado")