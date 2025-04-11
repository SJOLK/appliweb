package fr.diginamic.appliweb.controleurs;

import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;
import fr.diginamic.appliweb.repositories.DepartementRepository;
import fr.diginamic.appliweb.repositories.VilleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private DepartementRepository departementRepository;

    // ---------------------------
    // GET - LIST ALL VILLES PAGINATED
    // ---------------------------
    @GetMapping
    public ResponseEntity<Page<Ville>> getAllVillesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ville> villes = villeRepository.findAll(pageable);
        return ResponseEntity.ok(villes);
    }


    // ---------------------------
    // GET - Ville par ID
    // ---------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Ville> getVilleById(@PathVariable int id) {
        return villeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------
    // GET - Villes dont le nom commence par une chaîne donnée
    // Exemple : /villes/byPrefix?prefix=Pa
    // ---------------------------
    @GetMapping("/byPrefix")
    public ResponseEntity<List<Ville>> getVillesByPrefix(@RequestParam String prefix) {
        List<Ville> villes = villeRepository.findByNomStartingWith(prefix);
        return ResponseEntity.ok(villes);
    }

    // ---------------------------
    // GET - Villes globales avec population > min
    // Exemple : /villes/population/min?min=50000
    // ---------------------------
    @GetMapping("/population/min")
    public ResponseEntity<List<Ville>> getVillesWithPopulationGreaterThan(@RequestParam int min) {
        List<Ville> villes = villeRepository.findByNbHabitantsGreaterThan(min);
        return ResponseEntity.ok(villes);
    }

    // ---------------------------
    // GET - Villes globales avec population entre min et max
    // Exemple : /villes/population/range?min=50000&max=200000
    // ---------------------------
    @GetMapping("/population/range")
    public ResponseEntity<List<Ville>> getVillesWithPopulationBetween(
            @RequestParam int min, @RequestParam int max) {
        List<Ville> villes = villeRepository.findByNbHabitantsGreaterThanAndNbHabitantsLessThan(min, max);
        return ResponseEntity.ok(villes);
    }

    // ---------------------------
    // GET - Villes d'un département avec population > min
    // Exemple : /villes/departement/3/population/min?min=50000
    // ---------------------------
    @GetMapping("/departement/{depId}/population/min")
    public ResponseEntity<List<Ville>> getVillesByDepartementAndPopulationGreaterThan(
            @PathVariable Long depId, @RequestParam int min) {
        if (!departementRepository.existsById(depId)) {
            return ResponseEntity.notFound().build();
        }
        List<Ville> villes = villeRepository.findByDepartement_IdAndNbHabitantsGreaterThan(depId, min);
        return ResponseEntity.ok(villes);
    }

    // ---------------------------
    // GET - Villes d'un département avec population entre min et max
    // Exemple : /villes/departement/3/population/range?min=50000&max=200000
    // ---------------------------
    @GetMapping("/departement/{depId}/population/range")
    public ResponseEntity<List<Ville>> getVillesByDepartementAndPopulationRange(
            @PathVariable Long depId, @RequestParam int min, @RequestParam int max) {
        if (!departementRepository.existsById(depId)) {
            return ResponseEntity.notFound().build();
        }
        List<Ville> villes = villeRepository.findByDepartement_IdAndNbHabitantsGreaterThanAndNbHabitantsLessThan(depId, min, max);
        return ResponseEntity.ok(villes);
    }

    // ---------------------------
    // GET - Les n villes les plus peuplées d'un département (pagination)
    // Exemple : /villes/departement/3/top/5
    // ---------------------------
    @GetMapping("/departement/{depId}/top/{n}")
    public ResponseEntity<List<Ville>> getTopNVilles(
            @PathVariable Long depId, @PathVariable int n) {
        if (!departementRepository.existsById(depId)) {
            return ResponseEntity.notFound().build();
        }
        Pageable pageable = PageRequest.of(0, n);
        List<Ville> villes = villeRepository.findByDepartement_IdOrderByNbHabitantsDesc(depId, (java.awt.print.Pageable) pageable);
        return ResponseEntity.ok(villes);
    }


    // ---------------------------
    // POST - Insère une nouvelle ville
    // Exemple : POST /villes
    // Body JSON :
    // {
    //   "nom": "Toulouse",
    //   "nbHabitants": 500000,
    //   "departement": { "id": 4 }
    // }
    // ---------------------------
    @PostMapping
    public ResponseEntity<String> addVille(@Valid @RequestBody Ville ville, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        // Vérifier que le département est fourni
        if (ville.getDepartement() == null || ville.getDepartement().getId() == null) {
            return ResponseEntity.badRequest().body("Le département est obligatoire");
        }
        Long depId = Long.valueOf(ville.getDepartement().getId());
        Departement dep = departementRepository.findById(depId).orElse(null);
        if (dep == null) {
            return ResponseEntity.badRequest().body("Département introuvable");
        }
        // Associer le département complet à la ville
        ville.setDepartement(dep);
        villeRepository.save(ville);
        return ResponseEntity.ok("Ville insérée avec succès");
    }

    // ---------------------------
    // PUT - Met à jour une ville existante
    // Exemple : PUT /villes
    // Body JSON :
    // {
    //   "id": 3,
    //   "nom": "Lyon modifié",
    //   "nbHabitants": 600000,
    //   "departement": { "id": 3 }
    // }
    // ---------------------------
    @PutMapping
    public ResponseEntity<String> updateVille(@Valid @RequestBody Ville ville, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        if (!villeRepository.existsById(ville.getId())) {
            return ResponseEntity.notFound().build();
        }
        // Vérifier le département s'il est fourni
        if (ville.getDepartement() != null && ville.getDepartement().getId() != null) {
            Departement dep = departementRepository.findById(ville.getDepartement().getId()).orElse(null);
            if (dep == null) {
                return ResponseEntity.badRequest().body("Département introuvable");
            }
            ville.setDepartement(dep);
        }
        villeRepository.save(ville);
        return ResponseEntity.ok("Ville mise à jour avec succès");
    }

    // ---------------------------
    // DELETE - Supprime une ville par son id
    // Exemple : DELETE /villes/10
    // ---------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVilleById(@PathVariable Long id) {
        if (!villeRepository.existsById(Math.toIntExact(id))) {
            return ResponseEntity.notFound().build();
        }
        villeRepository.deleteById(Math.toIntExact(id));
        return ResponseEntity.ok("Ville supprimée avec succès");
    }
}
