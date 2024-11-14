package com.example.pokedexproject.ui.Settings;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.pokedexproject.models.Pokemon;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SettingsViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void downloadDataToFirestore() {
        // Fetch Pokémon list from the API or any cached list (This is a placeholder)
        List<Pokemon> pokemonList = fetchPokemonListFromAPI();

        for (Pokemon pokemon : pokemonList) {
            db.collection("pokemons").document(pokemon.getName())
                    .set(pokemon)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Pokemon saved: " + pokemon.getName()))
                    .addOnFailureListener(e -> Log.w("Firestore", "Error adding document", e));
        }
    }

    public void deleteDataFromFirestore() {
        db.collection("pokemons").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (com.google.firebase.firestore.DocumentSnapshot document : task.getResult().getDocuments()) {
                    db.collection("pokemons").document(document.getId()).delete()
                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Pokemon deleted: " + document.getId()))
                            .addOnFailureListener(e -> Log.w("Firestore", "Error deleting document", e));
                }
            }
        });
    }

    // Placeholder method to fetch Pokémon list
    private List<Pokemon> fetchPokemonListFromAPI() {
        // Implement the logic to get Pokémon data, possibly cached in HomeViewModel
        return null;
    }
}
