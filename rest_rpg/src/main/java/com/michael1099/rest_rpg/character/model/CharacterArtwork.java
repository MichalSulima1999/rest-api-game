package com.michael1099.rest_rpg.character.model;

public enum CharacterArtwork {

    HUMAN_MALE_1("human_male_1.jpg"),
    HUMAN_FEMALE_1("human_female_1.jpg"),
    ELF_MALE_1("elf_male_1.jpg"),
    ELF_FEMALE_1("elf_female_1.jpg"),
    DWARF_MALE_1("dwarf_male_1.jpg"),
    DWARF_FEMALE_1("dwarf_female_1.jpg");

    private final String artworkName;

    CharacterArtwork(String artworkName) {
        this.artworkName = artworkName;
    }

    public String getArtworkName() {
        return artworkName;
    }
}
