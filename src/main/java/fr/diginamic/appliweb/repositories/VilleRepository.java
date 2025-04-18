package fr.diginamic.appliweb.repositories;

import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface VilleRepository extends CrudRepository<Ville, Integer> {
    List<Ville> findByDepartementOrderByNbHabitantsDesc(Departement departement);

    @Query("SELECT v FROM Ville v WHERE v.departement.id = :depId " +
            "AND v.nbHabitants BETWEEN :popMin AND :popMax " +
            "ORDER BY v.nbHabitants DESC")
    List<Ville> findByDepartementIdAndPopulation(
            @Param("depId") Long depId,
            @Param("popMin") int min,
            @Param("popMax") int max
    );


    Ville findByName(String nom);


    // 1. Recherche de toutes les villes dont le nom commence par une chaîne de caractères donnée
    List<Ville> findByNomStartingWith(String prefix);

    // 2. Recherche de toutes les villes dont la population est supérieure à min
    List<Ville> findByNbHabitantsGreaterThan(int min);

    // 3. Recherche de toutes les villes dont la population est supérieure à min et inférieure à max.
    List<Ville> findByNbHabitantsGreaterThanAndNbHabitantsLessThan(int min, int max);

    // 4. Recherche de toutes les villes d’un département dont la population est supérieure à min
    List<Ville> findByDepartement_IdAndNbHabitantsGreaterThan(Long depId, int min);

    // 5. Recherche de toutes les villes d’un département dont la population est supérieure à min et inférieure à max.
    List<Ville> findByDepartement_IdAndNbHabitantsGreaterThanAndNbHabitantsLessThan(Long depId, int min, int max);

    // 6. Recherche des n villes les plus peuplées d’un département donné (utilisation de Pageable pour limiter le résultat)
    List<Ville> findByDepartement_IdOrderByNbHabitantsDesc(Long depId, Pageable pageable);

    Page<Ville> findAll(org.springframework.data.domain.Pageable pageable);

    List<Ville> findByDepartementAndNom(Departement dep, String nom);

    List<Ville> findByCodeDepartement(String codeDepartement);
}

