package fr.diginamic.appliweb;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.jetbrains.annotations.NotNull;

public class Ville {
    @NotNull
    @Min(1)
    private Integer id;

    @NotNull
    @Size(min = 2)
    private String nom;

    @Min(1)
    private int nbHabitants;


    public Ville() {
    }


    public Ville(int id, String nom, int nbHabitants) {
        this.id = id;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbHabitants() {
        return nbHabitants;
    }

    public void setNbHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }
}
