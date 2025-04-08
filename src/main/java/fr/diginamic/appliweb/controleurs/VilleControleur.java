package fr.diginamic.appliweb.controleurs;

import fr.diginamic.appliweb.Ville;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

    @GetMapping
    public List<Ville> listeVilles() {
        // Création d'une liste de Villes (exemple)
        List<Ville> villes = new ArrayList<>();
        villes.add(new Ville("Paris", 2200000));
        villes.add(new Ville("Marseille", 861635));
        villes.add(new Ville("Lyon", 515695));
        // Ajoutez autant de villes que vous souhaitez

        // Retour de la liste
        return villes;
    }
}
