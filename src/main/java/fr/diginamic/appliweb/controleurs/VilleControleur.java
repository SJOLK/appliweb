package fr.diginamic.appliweb.controleurs;

import fr.diginamic.appliweb.Ville;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

    private List<Ville> villes = new ArrayList<>();

    public VilleControleur() {
        villes.add(new Ville(1, "Paris", 2200000));
        villes.add(new Ville(2, "Marseille", 861635));
        villes.add(new Ville(3, "Lyon", 515695));
    }

    @GetMapping
    public List<Ville> listeVilles() {
        return villes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ville> getVilleById(@PathVariable int id) {
        for (Ville v : villes) {
            if (v.getId() == id) {
                return ResponseEntity.ok(v);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> createVille(@RequestBody Ville nouvelleVille) {

        boolean idExiste = villes.stream()
                .anyMatch(v -> v.getId() == nouvelleVille.getId());

        if (idExiste) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Une ville avec cet ID existe déjà !");
        }

        villes.add(nouvelleVille);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Ville créée avec succès !");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVille(@PathVariable int id,
                                              @RequestBody Ville villeMiseAJour) {
        for (int i = 0; i < villes.size(); i++) {
            if (villes.get(i).getId() == id) {
                villes.get(i).setNom(villeMiseAJour.getNom());
                villes.get(i).setNbHabitants(villeMiseAJour.getNbHabitants());
                return ResponseEntity
                        .ok("Ville mise à jour avec succès.");
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVille(@PathVariable int id) {
        for (int i = 0; i < villes.size(); i++) {
            if (villes.get(i).getId() == id) {
                villes.remove(i);
                return ResponseEntity.ok("Ville supprimée avec succès.");
            }
        }
        return ResponseEntity.notFound().build();
    }

}

