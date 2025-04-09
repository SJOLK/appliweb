package fr.diginamic.appliweb.repositories;

import fr.diginamic.appliweb.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {

}
