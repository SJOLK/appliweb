package fr.diginamic.appliweb.services;

import fr.diginamic.appliweb.dtos.VilleDto;
import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;
import fr.diginamic.appliweb.daos.VilleDao;
import fr.diginamic.appliweb.mappers.VilleMapper;
import fr.diginamic.appliweb.repositories.DepartementRepository;
import fr.diginamic.appliweb.repositories.VilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VilleService {

    @Autowired
    private VilleDao villeDao;

    @Autowired
    private VilleRepository villeRepo;

    @Autowired
    private DepartementRepository depRepo;

    /**
     * Extrait et retourne toutes les villes en base
     */
    public List<Ville> extractVilles() {
        return villeDao.extractAll();
    }

    /**
     * Extrait la ville dont l'ID est passé en paramètre
     */
    public Ville extractVille(int idVille) {
        return villeDao.extractById(idVille);
    }

    /**
     * Extrait la ville dont le nom est passé en paramètre
     */
    public Ville extractVille(String nom) {
        return villeDao.extractByName(nom);
    }

    /**
     * Insère une nouvelle ville et retourne la liste des villes après insertion
     */
    @Transactional
    public List<Ville> insertVille(Ville ville) {
        villeDao.insertVille(ville);
        return villeDao.extractAll();
    }

    /**
     * Modifie la ville (dont l'identifiant est passé en paramètre)
     * avec les nouvelles données de villeModifiee
     * Retourne la liste après modification
     */
    @Transactional
    public List<Ville> modifierVille(int idVille, Ville villeModifiee) {
        villeDao.updateVille(idVille, villeModifiee);
        return villeDao.extractAll();
    }

    /**
     * Supprime la ville dont l'id est passé en paramètre
     * et retourne la liste des villes après suppression
     */
    @Transactional
    public List<Ville> supprimerVille(int idVille) {
        villeDao.deleteVille(idVille);
        return villeDao.extractAll();
    }
}
