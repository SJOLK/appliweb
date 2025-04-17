package fr.diginamic.appliweb.services;

import fr.diginamic.appliweb.daos.DepartementDao;
import fr.diginamic.appliweb.dtos.DepartementDto;
import fr.diginamic.appliweb.dtos.VilleDto;
import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;
import fr.diginamic.appliweb.exceptions.ExceptionFonctionnelle;
import fr.diginamic.appliweb.mappers.DepartementMapper;
import fr.diginamic.appliweb.mappers.VilleMapper;
import fr.diginamic.appliweb.repositories.DepartementRepository;
import fr.diginamic.appliweb.repositories.VilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

@Service
public class DepartementService {


    @Autowired
    private DepartementDao departementDao;

    @Autowired
    private DepartementMapper mapper;

    @Autowired
    private VilleMapper villeMapper;
    private DepartementRepository departementRepository;
    private VilleRepository villeRepo;
    private final RestTemplate restTemplate = new RestTemplate();
    /**
     * Récupère la liste de tous les départements
     * et convertit chaque entité en DTO.
     */
    public List<DepartementDto> extraire() {

        return mapper.toDtos(departementDao.findAll());
        // dao.extraire() renvoie typiquement un List<Departement>.
    }

    public String getNomDepartement(String codeDepartement) {
        String url = "https://geo.api.gouv.fr/departements/" + codeDepartement + "?fields=nom,code,codeRegion";
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("nom")) {
                return (String) response.get("nom");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API pour le département " + codeDepartement + " : " + e.getMessage());
        }
        return codeDepartement;
    }
    /**
     * Récupère un département par son ID,
     * puis le convertit en DTO.
     */
    public DepartementDto extraireParId(int id) {
        return mapper.toDto(departementDao.findById(id));
    }

    /**
     * Récupère un département par son nom,
     * puis le convertit en DTO.
     */
    public DepartementDto extraireParNom(String nom) {
        return mapper.toDto(departementDao.findByNom(nom));
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

    @Transactional
    public List<DepartementDto> supprimerDepartement(int id) {

        departementDao.deleteDepartement(id);
        return mapper.toDtos(departementDao.findAll());
    }

    public List<VilleDto> extraireVilleParDepMinMax(int id, int min,int max) {
        // TODO controles sur min et max

        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("min et max doivent être positifs.");
        }
        if (min > max) {
            throw new IllegalArgumentException("min doit être inférieur ou égal à max.");
        }

        return villeMapper.toDtos(departementDao.extraireVilleParDepMinMax(id,min,max));
    }

}