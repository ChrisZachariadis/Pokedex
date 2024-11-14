package com.example.pokedexproject.ui.pokemonDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pokedexproject.models.Pokemon;

public class PokemonDetailsViewModel extends ViewModel {

    private final MutableLiveData<Pokemon> pokemonLiveData = new MutableLiveData<>();

    // Method to expose Pokémon data as LiveData
    public LiveData<Pokemon> getPokemonLiveData() {
        return pokemonLiveData;
    }

    // Method to set Pokémon data
    public void setPokemon(Pokemon pokemon) {
        pokemonLiveData.setValue(pokemon);
    }
}
