package com.example.pokedexproject.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.pokedexproject.models.Pokemon;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Pokemon>> pokemonListLiveData;

    public HomeViewModel() {
        pokemonListLiveData = new MutableLiveData<>(new ArrayList<>());
    }

    // Method to expose Pokémon data as LiveData
    public LiveData<List<Pokemon>> getPokemonListLiveData() {
        return pokemonListLiveData;
    }

    // Method to update the Pokémon list (for fetching new data)
    public void setPokemonList(List<Pokemon> pokemonList) {
        pokemonListLiveData.setValue(pokemonList);
    }

    // Method to add a single Pokémon (when fetching details)
    public void addPokemon(Pokemon pokemon) {
        List<Pokemon> currentList = pokemonListLiveData.getValue();
        if (currentList != null) {
            currentList.add(pokemon);
            pokemonListLiveData.setValue(currentList);
        }
    }
}
