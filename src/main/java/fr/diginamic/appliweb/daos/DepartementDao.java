package fr.diginamic.appliweb.daos;

import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;

import java.util.List;

public interface DepartementDao {
    List<Departement> findAll();

    Departement findById(int id);

    Departement findByNom(String nomDep);

    void insertDepartement(Departement departement);

    void updateDepartement(Departement departement);

    void deleteDepartement(int id);

    List<Ville> extraireVilleParDepMinMax(int id, int min, int max);
}
