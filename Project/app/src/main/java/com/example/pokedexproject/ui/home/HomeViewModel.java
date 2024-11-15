package com.example.pokedexproject.ui.home;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.pokedexproject.models.Pokemon;
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

public class HomeViewModel extends ViewModel {

    public interface OnPokemonsFetchedListener {
        void onPokemonsFetched(List<Pokemon> pokemons);
    }

    public void fetchPokemons(OnPokemonsFetchedListener listener) {
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

                        fetchPokemonDetails(name, url, fetchedPokemons, listener);
                    }
                }
            }
        });
    }

    private void fetchPokemonDetails(String name, String url, List<Pokemon> fetchedPokemons, OnPokemonsFetchedListener listener) {
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

                    String frontImage = details.getAsJsonObject("sprites").get("front_default").getAsString();
                    String backImage = details.getAsJsonObject("sprites").get("back_default").getAsString();
                    String type = details.getAsJsonArray("types").get(0).getAsJsonObject()
                            .getAsJsonObject("type").get("name").getAsString();
                    int weight = details.get("weight").getAsInt();
                    int height = details.get("height").getAsInt();
                    int baseExperience = details.get("base_experience").getAsInt();

                    List<String> abilities = parseDetailsArray(details.getAsJsonArray("abilities"), "ability");
                    List<String> heldItems = parseDetailsArray(details.getAsJsonArray("held_items"), "item");
                    List<String> moves = parseDetailsArray(details.getAsJsonArray("moves"), "move");

                    Pokemon pokemon = new Pokemon(name, type, abilities, weight, height, heldItems, moves,
                            baseExperience, frontImage, backImage);

                    fetchedPokemons.add(pokemon);

                    if (fetchedPokemons.size() == 10) {
                        listener.onPokemonsFetched(fetchedPokemons);
                    }
                }
            }
        });
    }

    private List<String> parseDetailsArray(JsonArray jsonArray, String key) {
        List<String> resultList = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                resultList.add(jsonArray.get(i).getAsJsonObject().getAsJsonObject(key).get("name").getAsString());
            }
        }
        return resultList;
    }
}
