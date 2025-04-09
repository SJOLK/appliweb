package fr.diginamic.appliweb.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.jetbrains.annotations.NotNull;


@Entity
public class Ville {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2)
    private String nom;

    @Min(value = 1)
    private int nbHabitants;


    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false)
    @JsonBackReference// Nom de la colonne FK
    private Departement departement;

    public Ville() {
    }


    public Ville(Long id, @NotNull String nom, int nbHabitants, Departement departement) {
        this.id = id;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.departement = departement;
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

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", nbHabitants=" + nbHabitants +
                ", departement=" + departement +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
