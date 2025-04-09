package fr.diginamic.appliweb.services;

import fr.diginamic.appliweb.daos.DepartementDao;
import fr.diginamic.appliweb.entities.Departement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService {
    private final DepartementDao departementDao;

    public DepartementService(final DepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    public List<Departement> extractDepartements() {
        return departementDao.findAll();
    }

    public Departement extractDepartement(int id) {
        return departementDao.findById(id);
    }

    public void insertDepartement(Departement departement) {
        departementDao.insertDepartement(departement);
    }

    public void modifierDepartement(int id, Departement departementAModifier) {
        Departement departement = departementDao.findById(id);

        if  (departement != null) {
            departement.setNomDep(departementAModifier.getNomDep());
            departementDao.updateDepartement(departement);
        }
    }

    public void supprimerDepartement(int id) {
        departementDao.deleteDepartement(id);
    }
}