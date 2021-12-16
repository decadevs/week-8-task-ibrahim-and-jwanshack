package com.olamachia.maptrackerweekeighttask.repository

import com.olamachia.maptrackerweekeighttask.models.Pokemon
import com.olamachia.maptrackerweekeighttask.models.PokemonDetails


class PokemonRepository {

    companion object {
        var pokemonList: List<Pokemon>? = null
        var singlePokemon: List<PokemonDetails>? = null
    }
}