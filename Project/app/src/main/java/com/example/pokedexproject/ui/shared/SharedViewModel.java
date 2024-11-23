package com.example.pokedexproject.ui.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pokedexproject.models.Pokemon;

import java.util.ArrayList;
import java.util.List;

// HomeViewModel fetches the pokemons (API call) and passes the data to the SharedViewModel
public class SharedViewModel extends ViewModel {

    private final MutableLiveData<List<Pokemon>> pokemonListLiveData = new MutableLiveData<>(new ArrayList<>());

    // Expose the LiveData for observing
    public LiveData<List<Pokemon>> getPokemonListLiveData() {
        return pokemonListLiveData;
    }

    // Method to add a single Pokémon to the list
    public void addPokemon(Pokemon pokemon) {
        List<Pokemon> currentList = pokemonListLiveData.getValue();
        if (currentList != null) {
            currentList.add(pokemon);
            pokemonListLiveData.postValue(currentList); // Update LiveData
        }
    }

    // Method to set the entire list of Pokémon at once
    public void setPokemonList(List<Pokemon> pokemonList) {
        pokemonListLiveData.postValue(pokemonList);
    }
}
