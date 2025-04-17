package fr.diginamic.appliweb.services;

import fr.diginamic.appliweb.dtos.VilleDto;
import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;
import fr.diginamic.appliweb.daos.VilleDao;
import fr.diginamic.appliweb.exceptions.ExceptionFonctionnelle;
import fr.diginamic.appliweb.exceptions.VilleNotFoundException;
import fr.diginamic.appliweb.mappers.VilleMapper;
import fr.diginamic.appliweb.repositories.DepartementRepository;
import fr.diginamic.appliweb.repositories.VilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VilleService {

    @Autowired
    private VilleDao villeDao;

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private DepartementRepository departementRepository;


    public List<Ville> findVillesByNomPrefix(String prefix) {
        List<Ville> list = villeRepository.findByNomStartingWithIgnoreCase(prefix);
        if (list.isEmpty()) {
            throw new VilleNotFoundException(
                    "Aucune ville dont le nom commence par \"" + prefix + "\" n’a été trouvée"
            );
        }
        return list;
    }

    public List<Ville> findVillesByPopulationGreaterThan(int min) {
        List<Ville> list = villeRepository.findByNombreHabitantsGreaterThan(min);
        if (list.isEmpty()) {
            throw new VilleNotFoundException(
                    "Aucune ville n’a une population supérieure à " + min
            );
        }
        return list;
    }

    public List<Ville> findVillesByPopulationBetween(int min, int max) {
        List<Ville> list = villeRepository.findByNombreHabitantsBetween(min, max);
        if (list.isEmpty()) {
            throw new VilleNotFoundException(
                    "Aucune ville n’a une population comprise entre " + min + " et " + max
            );
        }
        return list;
    }

    public List<Ville> findVillesByDeptAndPopGreaterThan(String codeDept, int min) {
        List<Ville> list = villeRepository.findByCodeDepartementAndNombreHabitantsGreaterThan(codeDept, min);
        if (list.isEmpty()) {
            throw new VilleNotFoundException(
                    "Aucune ville n’a une population supérieure à " + min
                            + " dans le département " + codeDept
            );
        }
        return list;
    }

    public List<Ville> findVillesByDeptAndPopBetween(String codeDept, int min, int max) {
        List<Ville> list = villeRepository.findByCodeDepartementAndNombreHabitantsBetween(codeDept, min, max);
        if (list.isEmpty()) {
            throw new VilleNotFoundException(
                    "Aucune ville n’a une population comprise entre "
                            + min + " et " + max
                            + " dans le département " + codeDept
            );
        }
        return list;
    }

    public List<Ville> findTopNVillesByDepartment(String codeDept, int n) {
        Page<Ville> page = villeRepository.findByCodeDepartement(
                codeDept,
                (java.awt.print.Pageable) PageRequest.of(0, n, Sort.by("nombreHabitants").descending())
        );
        List<Ville> list = page.getContent();
        if (list.isEmpty()) {
            throw new VilleNotFoundException(
                    "Aucune ville trouvée dans le département " + codeDept
            );
        }
        return list;
    }
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
    public Ville insertVille(Ville ville) throws ExceptionFonctionnelle {
        // Vérification : population doit être au moins 10
        if (ville.getNbHabitants() < 10) {
            throw new ExceptionFonctionnelle("La ville doit avoir au moins 10 habitants.");
        }

        // Vérification : le nom doit contenir au moins 2 lettres
        if (ville.getNom() == null || ville.getNom().trim().length() < 2) {
            throw new ExceptionFonctionnelle("Le nom de la ville doit contenir au moins 2 lettres.");
        }

        // Vérification : le département est obligatoire et doit être fourni avec son id
        if (ville.getDepartement() == null || ville.getDepartement().getId() == null) {
            throw new ExceptionFonctionnelle("Le département est obligatoire.");
        }
        Long depId = ville.getDepartement().getId();
        Departement dep = departementRepository.findById(depId)
                .orElseThrow(() -> new ExceptionFonctionnelle("Département introuvable."));
        // Vérification : le code du département doit contenir exactement 2 caractères
        if (dep.getCode() == null || dep.getCode().trim().length() != 2) {
            throw new ExceptionFonctionnelle("Le code du département doit contenir exactement 2 caractères.");
        }

        // Vérification : le nom de la ville doit être unique dans ce département
        List<Ville> villesExistantes = villeRepository.findByDepartementAndNom(dep, ville.getNom());
        if (!villesExistantes.isEmpty()) {
            throw new ExceptionFonctionnelle("Une ville portant ce nom existe déjà dans ce département.");
        }

        // Tout est validé : on associe le département complet à la ville et on enregistre
        ville.setDepartement(dep);
        villeRepository.save(ville);
        return ville;
    }



    /**
     * Modifie la ville (dont l'identifiant est passé en paramètre)
     * avec les nouvelles données de villeModifiee
     * Retourne la liste après modification
     */
    @Transactional
    public Ville updamodifierVilleteVille(int id, Ville villeModifiee) throws ExceptionFonctionnelle {
        Ville villeDB = villeRepository.findById(id)
                .orElseThrow(() -> new ExceptionFonctionnelle("La ville d'identifiant " + id + " n'existe pas."));
        // Vérification population
        if (villeModifiee.getNbHabitants() < 10) {
            throw new ExceptionFonctionnelle("La ville doit avoir au moins 10 habitants.");
        }

        // Vérification nom
        if (villeModifiee.getNom() == null || villeModifiee.getNom().trim().length() < 2) {
            throw new ExceptionFonctionnelle("Le nom de la ville doit contenir au moins 2 lettres.");
        }

        // Vérification du département (doit être fourni)
        if (villeModifiee.getDepartement() == null || villeModifiee.getDepartement().getId() == null) {
            throw new ExceptionFonctionnelle("Le département est obligatoire.");
        }
        Departement dep = departementRepository.findById(villeModifiee.getDepartement().getId())
                .orElseThrow(() -> new ExceptionFonctionnelle("Département introuvable."));
        if (dep.getCode() == null || dep.getCode().trim().length() != 2) {
            throw new ExceptionFonctionnelle("Le code du département doit contenir exactement 2 caractères.");
        }

        // Vérification d'unicité du nom dans le département (si changement de nom)
        if (!villeDB.getNom().equalsIgnoreCase(villeModifiee.getNom())) {
            List<Ville> villesExistantes = villeRepository.findByDepartementAndNom(dep, villeModifiee.getNom());
            if (!villesExistantes.isEmpty()) {
                throw new ExceptionFonctionnelle("Une ville portant ce nom existe déjà dans ce département.");
            }
        }

        // Mise à jour des champs
        villeDB.setNom(villeModifiee.getNom());
        villeDB.setNbHabitants(villeModifiee.getNbHabitants());
        villeDB.setDepartement(dep);

        villeRepository.save(villeDB);
        return villeDB;
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

    public List<Ville> listerVillesDuDepartement(String code) {
        Pageable p = Pageable.unpaged();
        return villeRepository.findByDepartementCodeOrderByNbHabitantsDesc(code, (java.awt.print.Pageable) p);
    }
}
