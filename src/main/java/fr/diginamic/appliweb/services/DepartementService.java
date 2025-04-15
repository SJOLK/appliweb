package fr.diginamic.appliweb.services;

import fr.diginamic.appliweb.daos.DepartementDao;
import fr.diginamic.appliweb.dtos.DepartementDto;
import fr.diginamic.appliweb.dtos.VilleDto;
import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.mappers.DepartementMapper;
import fr.diginamic.appliweb.mappers.VilleMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService {


    @Autowired
    private DepartementDao departementDao;

    @Autowired
    private DepartementMapper mapper;

    @Autowired
    private VilleMapper villeMapper;
    /**
     * Récupère la liste de tous les départements
     * et convertit chaque entité en DTO.
     */
    public List<DepartementDto> extraire() {

        return mapper.toDtos(departementDao.findAll());
        // dao.extraire() renvoie typiquement un List<Departement>.
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
    public List<DepartementDto> insertDepartement(DepartementDto departementFront) {
        Departement dept = mapper.toBean(departementFront);

        Departement deptExistant = departementDao.findByNom(dept.getNomDep());
        if (deptExistant == null) {
            departementDao.insertDepartement(dept);
        }
        else {
            // TODO jeter une exeption s'il existedeja un dept avec le meme non
            throw new IllegalArgumentException("Un département avec le nom "
                    + dept.getNomDep() + " existe déjà.");
        }

        return mapper.toDtos(departementDao.findAll());
    }

    @Transactional
    public List<DepartementDto> modifierDepartement(DepartementDto departementFront) {
        Departement dept = mapper.toBean(departementFront);
        Departement deptExistant = departementDao.findById(Math.toIntExact(dept.getId()));
        if (deptExistant != null) {
            departementDao.updateDepartement(dept);
        }
        else {
            // TODO jeter une exeption s'il le dep n'existe plus
            throw new IllegalArgumentException("Aucun département trouvé avec l'ID "
                    + dept.getId() + " pour la mise à jour.");
        }
        return mapper.toDtos(departementDao.findAll());
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