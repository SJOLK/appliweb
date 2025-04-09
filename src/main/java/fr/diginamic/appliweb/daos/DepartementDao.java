package fr.diginamic.appliweb.daos;

import fr.diginamic.appliweb.entities.Departement;

import java.util.List;

public interface DepartementDao {
    List<Departement> findAll();

    Departement findById(int id);

    void insertDepartement(Departement departement);

    void updateDepartement(Departement departement);

    void deleteDepartement(int id);
}
