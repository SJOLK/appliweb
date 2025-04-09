package fr.diginamic.appliweb.daos;

import fr.diginamic.appliweb.entities.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VilleDaoImpl implements VilleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Ville> extractAll() {
        return em.createQuery("SELECT v FROM Ville v", Ville.class)
                .getResultList();
    }

    @Override
    public Ville extractById(int idVille) {
        return em.find(Ville.class, (long) idVille);
    }

    @Override
    public Ville extractByName(String nom) {
        return em.createQuery("SELECT v FROM Ville v WHERE v.nom = :nom", Ville.class)
                .setParameter("nom", nom)
                .getResultStream()
                .findFirst()
                .orElse(null); // renvoie null si aucun résultat
    }

    @Override
    @Transactional
    public Ville insertVille(Ville ville) {
        em.persist(ville);
        return ville;
    }

    @Override
    @Transactional
    public Ville updateVille(int idVille, Ville villeModifiee) {
        Ville villeEnBase = em.find(Ville.class, (long) idVille);
        if (villeEnBase == null) {
            return null;
        }
        villeEnBase.setNom(villeModifiee.getNom());
        villeEnBase.setNbHabitants(villeModifiee.getNbHabitants());
        return villeEnBase;
    }

    @Override
    @Transactional
    public void deleteVille(int idVille) {
        Ville ville = em.find(Ville.class, (long) idVille);
        if (ville != null) {
            em.remove(ville);
        }
    }
}