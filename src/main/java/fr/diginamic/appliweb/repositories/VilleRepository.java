package fr.diginamic.appliweb.repositories;

import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VilleRepository extends JpaRepository<Ville, Long> {
    List<Ville> findByDepartementOrderByNbHabitantsDesc(Departement departement);

    @Query("SELECT v FROM Ville v WHERE v.departement.id = :depId " +
            "AND v.nbHabitants BETWEEN :popMin AND :popMax " +
            "ORDER BY v.nbHabitants DESC")
    List<Ville> findByDepartementIdAndPopulation(
            @Param("depId") Long depId,
            @Param("popMin") int min,
            @Param("popMax") int max
    );

}

