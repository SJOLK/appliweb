package fr.diginamic.appliweb.services;

import fr.diginamic.appliweb.daos.DepartementDao;
import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.exceptions.ExceptionFonctionnelle;
import fr.diginamic.appliweb.repositories.DepartementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class DepartementService {
    private final DepartementDao departementDao;
    @Autowired
    private DepartementRepository departementRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getNomDepartement(String codeDepartement) {
        String url = "https://geo.api.gouv.fr/departements/" + codeDepartement + "?fields=nom,code,codeRegion";
        try {
            // On définit que la réponse sera mappée dans une Map
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if(response != null && response.containsKey("nom")){
                return (String) response.get("nom");
            }
        } catch (Exception e) {
            // En cas de problème, on retourne simplement le code département
            System.err.println("Erreur lors de l'appel API pour le département " + codeDepartement + " : " + e.getMessage());
        }
        return codeDepartement;
    }

    public DepartementService(final DepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    public List<Departement> extractDepartements() {
        return departementDao.findAll();
    }

    public Departement extractDepartement(int id) {
        return departementDao.findById(id);
    }

    @Transactional
    public Departement insertDepartement(Departement dep) throws ExceptionFonctionnelle {
        if (dep.getCode() == null || dep.getCode().trim().length() < 2 || dep.getCode().trim().length() > 3) {
            throw new ExceptionFonctionnelle("Le code du département doit contenir entre 2 et 3 caractères.");
        }
        if (dep.getNomDep() == null || dep.getNomDep().trim().length() < 3) {
            throw new ExceptionFonctionnelle("Le nom du département est obligatoire et doit comporter au moins 3 lettres.");
        }
        // Vérifier l'unicité du code
        if (departementRepository.findByCode(dep.getCode()).isPresent()) {
            throw new ExceptionFonctionnelle("Un département avec ce code existe déjà.");
        }
        departementRepository.save(dep);
        return dep;
    }


    @Transactional
    public Departement modifierDepartement(Long id, Departement depMaj) throws ExceptionFonctionnelle {
        Departement depDB = departementRepository.findById(id)
                .orElseThrow(() -> new ExceptionFonctionnelle("Le département d'identifiant " + id + " n'existe pas."));
        if (depMaj.getCode() == null || depMaj.getCode().trim().length() < 2 || depMaj.getCode().trim().length() > 3) {
            throw new ExceptionFonctionnelle("Le code du département doit contenir entre 2 et 3 caractères.");
        }
        if (depMaj.getNomDep() == null || depMaj.getNomDep().trim().length() < 3) {
            throw new ExceptionFonctionnelle("Le nom du département est obligatoire et doit comporter au moins 3 lettres.");
        }
        // Vous pouvez ajouter une vérification d'unicité si le code est modifié, par exemple.
        depDB.setCode(depMaj.getCode());
        depDB.setNomDep(depMaj.getNomDep());
        departementRepository.save(depDB);
        return depDB;
    }

    public void supprimerDepartement(int id) {
        departementDao.deleteDepartement(id);
    }
}