package fr.diginamic.appliweb.dtos;


import java.util.ArrayList;
import java.util.List;

public class DepartementDto {

    private int id;
    private String nom;
    private List<VilleDto> villes = new ArrayList<>();

    public DepartementDto(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    /**
     * Getter
     * @return id
     */
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

    public List<VilleDto> getVilles() {
        return villes;
    }

    public void setVilles(List<VilleDto> villes) {
        this.villes = villes;
    }
}
