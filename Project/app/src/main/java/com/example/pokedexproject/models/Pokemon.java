package com.example.pokedexproject.models;

import java.util.List;

public class Pokemon {
    private String name;
    private String type;
    private List<String> abilities;
    private int weight;
    private int height;
    private List<String> heldItems;
    private List<String> moves;
    private int baseExperience;
    private String imageFront;
    private String imageBack;

    // Constructor including all attributes
    public Pokemon(String name, String type, List<String> abilities, int weight, int height,
                   List<String> heldItems, List<String> moves, int baseExperience,
                   String imageFront, String imageBack) {
        this.name = name;
        this.type = type;
        this.abilities = abilities;
        this.weight = weight;
        this.height = height;
        this.heldItems = heldItems;
        this.moves = moves;
        this.baseExperience = baseExperience;
        this.imageFront = imageFront;
        this.imageBack = imageBack;
    }

    // Getters for Firestore
    public String getName() { return name; }
    public String getType() { return type; }
    public List<String> getAbilities() { return abilities; }
    public int getWeight() { return weight; }
    public int getHeight() { return height; }
    public List<String> getHeldItems() { return heldItems; }
    public List<String> getMoves() { return moves; }
    public int getBaseExperience() { return baseExperience; }
    public String getImageFront() { return imageFront; }
    public String getImageBack() { return imageBack; }
}
