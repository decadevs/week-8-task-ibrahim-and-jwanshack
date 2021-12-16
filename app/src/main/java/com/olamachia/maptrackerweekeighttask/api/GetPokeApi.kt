package com.example.mapapplication.api


import com.olamachia.maptrackerweekeighttask.models.PokemonDetails
import com.example.mapapplication.models.PokemonList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetPokeApi {

    @GET("pokemon")
    fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int): Observable<PokemonList>

    @GET("pokemon/{name}")
    fun getPokemonDetails(
        @Path("name") name:String):Observable<PokemonDetails>


}