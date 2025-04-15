package fr.diginamic.appliweb.daos;

import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementDaoImpl implements DepartementDao {

    @PersistenceContext
    private EntityManager em;
    private EntityManager entityManager;

    @Override
    public List<Ville> extraireVilleParDepMinMax(int id, int min, int max) {
        String jpql = "SELECT v FROM Ville v " +
                "WHERE v.departement.id = :deptId " +
                "AND v.population BETWEEN :minPop AND :maxPop";

        return entityManager
                .createQuery(jpql, Ville.class)
                .setParameter("deptId", id)
                .setParameter("minPop", min)
                .setParameter("maxPop", max)
                .getResultList();
    }

    @Override
    public List<Departement> findAll() {
        return em.createQuery("SELECT d FROM Departement d", Departement.class)
                .getResultList();
    }

    @Override
    public Departement findById(int id) {
        return em.find(Departement.class, id);
    }
    @Override
    public Departement findByNom(String depNom) {
        String jpql = "SELECT d FROM Departement d WHERE d.nomDep = :nom";
        List<Departement> list = em.createQuery(jpql, Departement.class)
                .setParameter("nom", depNom)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    @Transactional
    public void insertDepartement(Departement departement) {
        em.persist(departement);
    }

    @Override
    @Transactional
    public void updateDepartement(Departement departement) {
        em.merge(departement);
    }

    @Override
    @Transactional
    public void deleteDepartement(int id) {
        Departement departement = em.find(Departement.class, id);
        if (departement != null) {
            em.remove(departement);
        }
    }
}
