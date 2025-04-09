package fr.diginamic.appliweb.daos;

import fr.diginamic.appliweb.entities.Ville;
import java.util.List;

public interface VilleDao {

    List<Ville> extractAll();
    Ville extractById(int idVille);
    Ville extractByName(String nom);

    Ville insertVille(Ville ville);
    Ville updateVille(int idVille, Ville villeModifiee);
    void deleteVille(int idVille);
}
