package com.olamachia.maptrackerweekeighttask.models

import com.example.mapapplication.models.Ability
import com.example.mapapplication.models.Move
import com.example.mapapplication.models.Stat


class PokemonDetails {
    var abilities: List<Ability>? = null
    var type: List<String>? = null
    var height: Double? = null
    var weight: Double? = null
    var base_experience:Double? = null
    var img: String? = null
    var name: String? = null
    var moves: List<Move>? = null
    var stats: List<Stat>? = null
    var id:Int? = null
}