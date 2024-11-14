package com.example.pokedexproject.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pokedexproject.R;
import com.example.pokedexproject.databinding.FragmentHomeBinding;
import com.example.pokedexproject.models.Pokemon;
import com.example.pokedexproject.ui.shared.SharedViewModel;
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

public class HomeFragment extends Fragment implements PokemonAdapter.OnPokemonClickListener {

    private FragmentHomeBinding binding;
    private PokemonAdapter pokemonAdapter;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        setupRecyclerView();

        // Observe the Pokémon list in SharedViewModel
        sharedViewModel.getPokemonListLiveData().observe(getViewLifecycleOwner(), pokemons -> {
            pokemonAdapter.updatePokemonList(pokemons); // Update adapter with new data
        });

        // Fetch Pokémon data
        fetchPokemons();

        return root;
    }

    private void setupRecyclerView() {
        // Initialize adapter with an empty list and set the adapter once in setupRecyclerView
        pokemonAdapter = new PokemonAdapter(new ArrayList<>(), this);
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

                    List<Pokemon> fetchedPokemons = new ArrayList<>();
                    for (int i = 0; i < results.size(); i++) {
                        JsonObject pokemonObject = results.get(i).getAsJsonObject();
                        String name = pokemonObject.get("name").getAsString();
                        String url = pokemonObject.get("url").getAsString();

                        fetchPokemonDetails(name, url, fetchedPokemons);
                    }
                }
            }
        });
    }

    private void fetchPokemonDetails(String name, String url, List<Pokemon> fetchedPokemons) {
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

                    // Add to the fetchedPokemons list
                    fetchedPokemons.add(pokemon);

                    // When all 10 details are fetched, update the SharedViewModel
                    if (fetchedPokemons.size() == 10) {
                        getActivity().runOnUiThread(() -> sharedViewModel.setPokemonList(fetchedPokemons));
                    }
                }
            }
        });
    }

    @Override
    public void onPokemonClick(Pokemon pokemon) {
        // Handle navigation to PokemonDetailsFragment, passing the selected Pokemon
        Bundle bundle = new Bundle();
        bundle.putSerializable("pokemon", pokemon);

        // Navigate using the Navigation component
        Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_pokemonFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
