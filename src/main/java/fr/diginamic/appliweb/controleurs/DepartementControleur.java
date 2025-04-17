package fr.diginamic.appliweb.controleurs;

import fr.diginamic.appliweb.dtos.DepartementDto;
import fr.diginamic.appliweb.dtos.VilleDto;
import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.exceptions.ExceptionFonctionnelle;
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
    public Departement getDepartementById(@PathVariable int id) {

        return departementService.extractDepartement(id);
    }

    @GetMapping("/{nom}")
    public DepartementDto getDepartementByNom(@PathVariable String nom) {
        return departementService.extraireParNom(nom);
    }

    @GetMapping("/id/{id}min/{min}max/{max}")
    public List<VilleDto> extractByNomEtMinMax(@PathVariable int id,@PathVariable int min,@PathVariable int max) {
        return departementService.extraireVilleParDepMinMax(id, min, max);
    }

    @PostMapping
    public ResponseEntity<String> addDepartement(@Valid @RequestBody Departement departement) throws ExceptionFonctionnelle {
        departementService.insertDepartement(departement);

        return ResponseEntity.ok("Département inséré avec succès");
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Departement nvDepartement) throws ExceptionFonctionnelle {
        if (nvDepartement.getNomDep() == null ) {
            return ResponseEntity.badRequest().body("Le nom du département est obligatoire.");
        }

        if (nvDepartement.getId() != 0) {
            return ResponseEntity.badRequest().body("L'identifiant du département doit être nul pour une création.");
        }

        return ResponseEntity.ok(departementService.insertDepartement(nvDepartement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDepartement(@PathVariable long id, @Valid @RequestBody Departement departement) throws ExceptionFonctionnelle {
        departement.setId((int) id);
        departementService.modifierDepartement((long) id, departement);

        return ResponseEntity.ok("Département modifié avec succès");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartement(@PathVariable int id) {
        departementService.supprimerDepartement(id);

        return ResponseEntity.ok("Département supprimé avec succès");
    }


}

