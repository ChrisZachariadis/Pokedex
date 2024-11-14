package com.example.pokedexproject.ui.shared;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pokedexproject.models.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<List<Pokemon>> pokemonListLiveData = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<List<Pokemon>> getPokemonListLiveData() {
        return pokemonListLiveData;
    }

    public void addPokemon(Pokemon pokemon) {
        List<Pokemon> currentList = pokemonListLiveData.getValue();
        if (currentList != null) {
            currentList.add(pokemon);
            pokemonListLiveData.postValue(currentList);
        }
    }
}
