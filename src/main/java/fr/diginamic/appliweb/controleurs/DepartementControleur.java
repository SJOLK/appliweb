package fr.diginamic.appliweb.controleurs;

import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.repositories.DepartementRepository;
import fr.diginamic.appliweb.services.DepartementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departements")
public class DepartementControleur {
    @Autowired
    private DepartementService departementService;

    @Autowired
    private DepartementRepository departementRepository;

    @GetMapping
    public List<Departement> listeDepartements() {
        return departementService.extractDepartements();
    }

    @GetMapping("/{id}")
    public Departement getDepartement(@PathVariable int id) {
        return departementService.extractDepartement(id);
    }

    @PostMapping
    public ResponseEntity<String> addDepartement(@Valid @RequestBody Departement departement) {
        departementService.insertDepartement(departement);

        return ResponseEntity.ok("Département inséré avec succès");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDepartement(@PathVariable long id, @Valid @RequestBody Departement departement) {
        departement.setId(id);
        departementService.modifierDepartement((int) id, departement);

        return ResponseEntity.ok("Département modifié avec succès");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartement(@PathVariable int id) {
        departementService.supprimerDepartement(id);

        return ResponseEntity.ok("Département supprimé avec succès");
    }
}

