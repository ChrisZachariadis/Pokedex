package com.example.pokedexproject.ui.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pokedexproject.R;
import com.example.pokedexproject.models.Pokemon;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Main function of pokemon adapter is to display the pokemons in a list.
 */
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
    private List<Pokemon> pokemonList;
    private final OnPokemonClickListener onPokemonClickListener;

    // Define an interface for click events
    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    // Modify the constructor to accept the click listener
    public PokemonAdapter(List<Pokemon> pokemonList, OnPokemonClickListener listener) {
        this.pokemonList = pokemonList;
        this.onPokemonClickListener = listener;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.nameTextView.setText(pokemon.getName());
        holder.typeTextView.setText(pokemon.getType());
        Picasso.get().load(pokemon.getImageFront()).into(holder.frontImageView);

        // Set up the click listener for each item
        holder.itemView.setOnClickListener(v -> {
            if (onPokemonClickListener != null) {
                onPokemonClickListener.onPokemonClick(pokemon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, typeTextView;
        ImageView frontImageView;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewPokemonName);
            typeTextView = itemView.findViewById(R.id.textViewPokemonType);
            frontImageView = itemView.findViewById(R.id.imageViewPokemonFront);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updatePokemonList(List<Pokemon> newPokemonList) {
        this.pokemonList = newPokemonList;
        notifyDataSetChanged();
    }
}
