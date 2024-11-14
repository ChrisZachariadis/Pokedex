package com.example.pokedexproject.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pokedexproject.databinding.FragmentHomeBinding;
import com.example.pokedexproject.models.Pokemon;
import com.example.pokedexproject.ui.shared.SharedViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
//import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private List<Pokemon> pokemonList = new ArrayList<>();
    private PokemonAdapter pokemonAdapter;
    private FirebaseFirestore db;
    private SharedViewModel sharedViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observe the shared Pokémon list
        sharedViewModel.getPokemonListLiveData().observe(getViewLifecycleOwner(), pokemons -> {
            // Update your local list and notify adapter
            pokemonList.clear();
            pokemonList.addAll(pokemons);
            pokemonAdapter.notifyDataSetChanged();
        });

        setupRecyclerView();
        fetchPokemons();

        return root;
    }

    private void setupRecyclerView() {
        pokemonAdapter = new PokemonAdapter(pokemonList);
        binding.recyclerViewPokemons.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewPokemons.setAdapter(pokemonAdapter);
    }

    private void fetchPokemons() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://pokeapi.co/api/v2/pokemon/?limit=10";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("API", "Failed to fetch Pokémon data", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                    JsonArray results = jsonObject.getAsJsonArray("results");

                    for (int i = 0; i < results.size(); i++) {
                        JsonObject pokemonObject = results.get(i).getAsJsonObject();
                        String name = pokemonObject.get("name").getAsString();
                        String url = pokemonObject.get("url").getAsString();

                        fetchPokemonDetails(name, url);
                    }
                }
            }
        });
    }

    private void fetchPokemonDetails(String name, String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("API", "Failed to fetch Pokémon details", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject details = JsonParser.parseString(response.body().string()).getAsJsonObject();

                    // Extracting each required field
                    String frontImage = details.getAsJsonObject("sprites").get("front_default").getAsString();
                    String backImage = details.getAsJsonObject("sprites").get("back_default").getAsString();
                    String type = details.getAsJsonArray("types").get(0).getAsJsonObject()
                            .getAsJsonObject("type").get("name").getAsString();
                    int weight = details.get("weight").getAsInt();
                    int height = details.get("height").getAsInt();
                    int baseExperience = details.get("base_experience").getAsInt();

                    // Parse abilities
                    JsonArray abilitiesArray = details.getAsJsonArray("abilities");
                    List<String> abilities = new ArrayList<>();
                    for (int i = 0; i < abilitiesArray.size(); i++) {
                        abilities.add(abilitiesArray.get(i).getAsJsonObject().getAsJsonObject("ability").get("name").getAsString());
                    }

                    // Parse held items
                    JsonArray heldItemsArray = details.getAsJsonArray("held_items");
                    List<String> heldItems = new ArrayList<>();
                    for (int i = 0; i < heldItemsArray.size(); i++) {
                        heldItems.add(heldItemsArray.get(i).getAsJsonObject().getAsJsonObject("item").get("name").getAsString());
                    }

                    // Parse moves
                    JsonArray movesArray = details.getAsJsonArray("moves");
                    List<String> moves = new ArrayList<>();
                    for (int i = 0; i < movesArray.size(); i++) {
                        moves.add(movesArray.get(i).getAsJsonObject().getAsJsonObject("move").get("name").getAsString());
                    }

                    // Create Pokémon object with all the details
                    Pokemon pokemon = new Pokemon(name, type, abilities, weight, height, heldItems, moves,
                            baseExperience, frontImage, backImage);

                    // Add to list for display and save to Firestore
                    pokemonList.add(pokemon);

//                    savePokemonToFirestore(pokemon);


                    // Update RecyclerView on the main thread
                    getActivity().runOnUiThread(() -> pokemonAdapter.notifyDataSetChanged());

                    sharedViewModel.addPokemon(pokemon);

                }
            }
        });
    }


    private void savePokemonToFirestore(Pokemon pokemon) {
        db.collection("pokemons").document(pokemon.getName())
                .set(pokemon)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Pokemon saved!"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error adding document", e));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
