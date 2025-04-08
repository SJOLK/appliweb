package fr.diginamic.appliweb.controleurs;

import fr.diginamic.appliweb.Ville;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

    private List<Ville> villes = new ArrayList<>();

    public VilleControleur() {
        villes.add(new Ville("Paris", 2200000));
        villes.add(new Ville("Marseille", 861635));
        villes.add(new Ville("Lyon", 515695));
    }

    @GetMapping
    public List<Ville> listeVilles() {
        return villes;
    }

    @PostMapping
    public ResponseEntity<String> ajouterVille(@RequestBody Ville nouvelleVille) {
        boolean existeDeja = villes.stream()
                .anyMatch(v -> v.getNom().equalsIgnoreCase(nouvelleVille.getNom()));

        if (existeDeja) {
            return ResponseEntity
                    .badRequest()
                    .body("Cette ville existe déjà");
        } else {
            villes.add(nouvelleVille);
            return ResponseEntity
                    .ok("Ville enregistrée avec succès");
        }
    }
}

