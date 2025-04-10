package fr.diginamic.appliweb.controleurs;

import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;
import fr.diginamic.appliweb.dtos.VilleDto;
import fr.diginamic.appliweb.repositories.DepartementRepository;
import fr.diginamic.appliweb.repositories.VilleRepository;
import fr.diginamic.appliweb.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

    @Autowired
    private VilleService villeService;
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private DepartementRepository departementRepository;
    /**
     * GET #1 : Retourne la liste de toutes les villes
     * Exemple d'appel : GET /villes
     */
    @GetMapping
    public List<Ville> getAllVilles() {
        return villeService.extractVilles();
    }

    /**
     * GET #2 : Retourne une ville en fonction de son ID
     * Exemple d'appel : GET /villes/10
     */
    @GetMapping("/{id}")
    public Ville getVilleById(@PathVariable int id) {
        return villeService.extractVille(id);
    }

    /**
     * GET #3 : Retourne une ville en fonction de son nom
     * Exemple d'appel : GET /villes/byName?nom=Paris
     * Ici, on passe par un paramètre de requête (query param),
     * ex: /villes/byName?nom=Paris
     */
    @GetMapping("/byName")
    public Ville getVilleByName(@RequestParam String nom) {
        return villeService.extractVille(nom);
    }

    /**
     * GET #4 : Retourne une liste des n plus grandes villes d’un département
     * Exemple d'appel : GET /villes/{depId}/top/{n}
     * ex: /villes/34/top/5
     */
    @GetMapping("/{depId}/top/{n}")
    public ResponseEntity<List<Ville>> getTopNVilles(@PathVariable Long depId, @PathVariable int n) {
        Departement dep = departementRepository.findById(depId).orElse(null);
        if (dep == null) {
            return ResponseEntity.notFound().build();
        }
        List<Ville> toutes = villeRepository.findByDepartementOrderByNbHabitantsDesc(dep);

        // si n > toutes.size(), on retournera toute la liste, sinon un sous-ensemble
        List<Ville> topN = toutes.stream().limit(n).toList();
        return ResponseEntity.ok(topN);
    }

    /**
     * GET #5 : Retourne une liste des villes ayant une population comprise entre min et max dans un département donné
     * Exemple d'appel : GET /villes/{depId}/villesByPop
     * ex: /villes/34/villesByPop?min=100000&max=500000
     */
    @GetMapping("/{depId}/villesByPop")
    public ResponseEntity<List<Ville>> getVillesByPopulation(@PathVariable Long depId,
                                                             @RequestParam int min,
                                                             @RequestParam int max) {
        // Vérifier le département
        if (!departementRepository.existsById(depId)) {
            return ResponseEntity.notFound().build();
        }
        // Récupérer les villes
        List<Ville> villes = villeRepository.findByDepartementIdAndPopulation(depId, min, max);
        return ResponseEntity.ok(villes);
    }



    /**
     * POST : Insère une nouvelle ville en base de données
     * Exemple d'appel : POST /villes
     * Avec un JSON dans le body, ex:
     * {
     *   "nom": "Toulouse",
     *   "nbHabitants": 500000
     * }
     *
     * La méthode retourne la liste des villes après insertion
     */
    @PostMapping
    public ResponseEntity<String> addVille(@Valid @RequestBody  Ville ville, BindingResult result) {
        boolean existe = villeService.extractVilles().stream()
                .anyMatch(v -> v.getNom().equalsIgnoreCase(ville.getNom()));

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().getFirst().getDefaultMessage());
        }

        if (existe) {
            return ResponseEntity.badRequest().body("La ville existe déjà");
        }

        boolean idExistant =  villeService.extractVilles().stream().anyMatch(v -> v.getId() == ville.getId());

        if (idExistant) {
            return ResponseEntity.badRequest().body("L'identifiant existe déjà");
        }

        villeService.insertVille(ville);

        return ResponseEntity.ok("Ville insérée avec succès");
    }

    /**
     * PUT : Met à jour une ville existante
     * Exemple d'appel : PUT /villes
     * Avec un JSON dans le body, ex:
     * {
     *   "id": 3,
     *   "nom": "Lyon modifié",
     *   "nbHabitants": 600000
     * }
     *
     * La méthode retourne la liste des villes après modification
     */
    @PutMapping
    public List<Ville> updateVille(@RequestBody Ville villeModifiee) {
        return villeService.modifierVille(villeModifiee.getId().intValue(), villeModifiee);
    }

    /**
     * DELETE : Supprime une ville en fonction de son id
     * Exemple d'appel : DELETE /villes/10
     *
     * La méthode retourne la liste des villes après suppression
     */
    @DeleteMapping("/{id}")
    public List<Ville> deleteVilleById(@PathVariable int id) {
        return villeService.supprimerVille(id);
    }
}