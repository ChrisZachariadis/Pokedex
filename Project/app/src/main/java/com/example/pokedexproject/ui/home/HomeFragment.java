package com.example.pokedexproject.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pokedexproject.databinding.FragmentHomeBinding;
import com.example.pokedexproject.models.Pokemon;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseFirestore.getInstance();

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

                    // Get data from JSON
                    String frontImage = details.getAsJsonObject("sprites").get("front_default").getAsString();
                    String type = details.getAsJsonArray("types").get(0).getAsJsonObject()
                            .getAsJsonObject("type").get("name").getAsString();

                    // Create Pokémon object
                    Pokemon pokemon = new Pokemon(name, type, frontImage);
                    pokemonList.add(pokemon);  // Add to list for RecyclerView display

                    // Save to Firestore
                    savePokemonToFirestore(pokemon);

                    // Update RecyclerView on main thread
                    getActivity().runOnUiThread(() -> pokemonAdapter.notifyDataSetChanged());
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
