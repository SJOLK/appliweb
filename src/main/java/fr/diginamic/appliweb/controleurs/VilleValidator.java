package fr.diginamic.appliweb.controleurs;

import fr.diginamic.appliweb.entities.Ville;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class VilleValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Ville.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ville ville = (Ville) target;

        // 1) id > 0
        if (ville.getId() <= 0) {
            errors.rejectValue("id", "id.positif", "L'id doit être strictement positif");
        }

        // 2) nom non nul, taille >= 2
        if (ville.getNom() == null || ville.getNom().length() < 2) {
            errors.rejectValue("nom", "nom.tropCourt", "Le nom doit contenir au moins 2 caractères");
        }

        // 3) nbHabitants >= 1
        if (ville.getNbHabitants() < 1) {
            errors.rejectValue("nbHabitants", "nbHabitants.min", "Le nombre d'habitants doit être >= 1");
        }
    }
}
