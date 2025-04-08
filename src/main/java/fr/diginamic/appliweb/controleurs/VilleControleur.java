package fr.diginamic.appliweb.controleurs;

import fr.diginamic.appliweb.Ville;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {
    @Autowired
    private VilleValidator villeValidator;

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



    @PostMapping
    public ResponseEntity<String> createVille(@Valid @RequestBody Ville nouvelleVille, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            return ResponseEntity.badRequest().body("Erreurs de validation : " + bindingResult.getAllErrors());
        }

        boolean idExiste = villes.stream()
                .anyMatch(v -> v.getId() == nouvelleVille.getId());

        if (idExiste) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Une ville avec cet ID existe déjà !");
        }

        villes.add(nouvelleVille);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Ville créée avec succès !");
    }

    // PUT : localhost:8080/villes
    @PutMapping
    public ResponseEntity<String> modifVille(@RequestBody Ville ville) {
        Errors result = villeValidator.validateObject(ville);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }

        for (Ville villeExistante : villes) {
            if (villeExistante.getId() == ville.getId()) {
                villeExistante.setNom(ville.getNom());
                villeExistante.setNbHabitants(ville.getNbHabitants());
                return ResponseEntity.ok("Ville modifiée avec succès");
            }
        }

        return ResponseEntity
                .badRequest()
                .body("Ville inexistante pour l'id suivant : " + ville.getId());
    }

}

