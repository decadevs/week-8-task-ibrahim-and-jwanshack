package com.olamachia.maptrackerweekeighttask

import android.content.ContentValues
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapapplication.api.GetPokeApi
import com.olamachia.maptrackerweekeighttask.api.RetrofitInstance
import com.example.mapapplication.models.PokemonList
import com.olamachia.maptrackerweekeighttask.repository.PokemonRepository
import com.olamachia.maptrackerweekeighttask.utils.ItemOffsetDecoration
import com.olamachia.maptrackerweekeighttask.utils.Utils.isOnline
import com.olamachia.maptrackerweekeighttask.adapter.PokemonListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit


class PokemonActivity : AppCompatActivity() {

    lateinit var getPokeApi: GetPokeApi
    lateinit var recyclerView: RecyclerView
    private var retrofit: Retrofit = RetrofitInstance.instance
    private var pokemonList: PokemonList? = null
    private val compositeDisposable = CompositeDisposable()
    private lateinit var logo : ImageView
    private lateinit var progressBar: ProgressBar

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)

        logo=findViewById(R.id.pokemonLogo)
        progressBar= findViewById(R.id.progressbarpklist)

        getPokeApi = retrofit.create(GetPokeApi::class.java)

        logo.visibility=View.VISIBLE
        progressBar.visibility=View.INVISIBLE

        //Check if the phone has a connection
        if (isOnline(this)) {
            //set recycler view dimensions and style
            recyclerView = findViewById(R.id.pokemon_recycler)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(this, 2)
            val itemDecoration = ItemOffsetDecoration(this, R.dimen.spacing)
            recyclerView.addItemDecoration(itemDecoration)
            // fetch pokemon
            findViewById<Button>(R.id.reloadButton).setOnClickListener {

                val limit = findViewById<EditText>(R.id.limitET).text
                if (limit.isNullOrBlank()){
                    Toast.makeText(this,"Please enter a number",Toast.LENGTH_LONG).show()
                }else{
                    progressBar.visibility=View.VISIBLE
                    getData(limit.toString().toInt(),0)
                }
            }
        } else {
            Toast.makeText(
                this,
                "Network connection required",
                Toast.LENGTH_LONG
            ).show()
        }

    }



    private fun getData(limit: Int, offset: Int) {
        //check if the list repository if it is empty fetch pokemon into it
            //in the composite disposable make the Api call
            compositeDisposable.add(getPokeApi.getPokemonList(limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { error(Log.i("AAA", "Check Network Connection")) }
                .subscribe { pokeDex ->
                    pokemonList = pokeDex
                    //send the fetched data to the list in the repository
                    PokemonRepository.pokemonList = pokemonList?.results
                    PokemonRepository.pokemonList?.forEach {
                        // log the name and url for debugging
                        Log.i(ContentValues.TAG, "Pokemon: " + it.name)
                        Log.i(ContentValues.TAG, "Pokemon: " + it.url)
                    }
                    //send the list to the adapter and attach it to the recycler view
                    logo.visibility=View.INVISIBLE
                    progressBar.visibility=View.INVISIBLE
                    val adapter = PokemonListAdapter(PokemonRepository.pokemonList!!, limit, offset)
                    recyclerView.adapter = adapter
                }
            )



    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }
}