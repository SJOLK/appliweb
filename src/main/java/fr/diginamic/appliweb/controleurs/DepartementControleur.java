package fr.diginamic.appliweb.controleurs;

import fr.diginamic.appliweb.dtos.DepartementDto;
import fr.diginamic.appliweb.dtos.VilleDto;
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
    public ResponseEntity<String> addDepartement(@Valid @RequestBody DepartementDto departementDto) {
        departementService.insertDepartement(departementDto);

        return ResponseEntity.ok("Département inséré avec succès");
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody DepartementDto nvDepartement) {

        if (nvDepartement.getNom() == null ) {
            return ResponseEntity.badRequest().body("Le nom du département est obligatoire.");
        }

        if (nvDepartement.getId() != 0) {
            return ResponseEntity.badRequest().body("L'identifiant du département doit être nul pour une création.");
        }

            return ResponseEntity.ok(departementService.insertDepartement(nvDepartement));
    }

    @PutMapping
    public ResponseEntity<?> modifDepartement(@RequestBody DepartementDto departement) {

        if (departement.getNom() == null || departement.getNom().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Le nom du département est obligatoire.");
        }
        if (departement.getId() == 0) {
            return ResponseEntity
                    .badRequest()
                    .body("L'identifiant du département doit être renseigné et non nul.");
        }
        List<DepartementDto> resultat = departementService.modifierDepartement(departement);
        return ResponseEntity.ok(resultat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartement(@PathVariable int id) {
        departementService.supprimerDepartement(id);

        return ResponseEntity.ok("Département supprimé avec succès");
    }


}

