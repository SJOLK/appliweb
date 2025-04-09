package fr.diginamic.appliweb.daos;

import fr.diginamic.appliweb.entities.Departement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementDaoImpl implements DepartementDao {

    @PersistenceContext
    private EntityManager em;

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
