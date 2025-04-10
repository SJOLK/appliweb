package fr.diginamic.appliweb.dtos;



public class VilleDto {

    private int id;
    private String nom;
    private int nbHabs;
    private DepartementDto departement;

    public VilleDto(int id, String nom, int nbHabs) {
        this.id = id;
        this.nom = nom;
        this.nbHabs = nbHabs;
    }

    /**
     * Getter
     *
     * @return id
     */
    public long getId() {
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

    public int getNbHabs() {
        return nbHabs;
    }

    public void setNbHabs(int nbHabs) {
        this.nbHabs = nbHabs;
    }

    public DepartementDto getDepartement() {
        return departement;
    }

    public void setDepartement(DepartementDto departement) {
        this.departement = departement;
    }
}
