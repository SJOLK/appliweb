package fr.diginamic.appliweb.mappers;


import fr.diginamic.appliweb.dtos.VilleDto;
import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VilleMapper {

    @Autowired
    private DepartementMapper deptMapper;

    public VilleDto toDto(Ville ville) {
        VilleDto dto = new VilleDto(Math.toIntExact(ville.getId()), ville.getNom(), ville.getNbHabitants());
        dto.setDepartement(deptMapper.toDto(ville.getDepartement()));
        return dto;
    }

    public List<VilleDto> toDtos(List<Ville> villes) {
        return villes.stream()
                .map(v -> toDto(v))
                .toList();
    }

    public Ville toBean(VilleDto dto) {
        Ville ville = new Ville();
        ville.setId((int) dto.getId());
        ville.setNom(dto.getNom());
        ville.setNbHabitants(dto.getNbHabs());
        // Optionally, handle the department here if you want to convert it from dto.getDepartement().
        // Example:
        // ville.setDepartement(deptMapper.toBean(dto.getDepartement()));
        return ville;
    }
}
