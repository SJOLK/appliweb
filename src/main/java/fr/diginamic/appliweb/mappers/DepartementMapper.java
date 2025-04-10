package fr.diginamic.appliweb.mappers;

import fr.diginamic.appliweb.dtos.DepartementDto;
import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepartementMapper {

    public DepartementDto toDto(Departement dept) {
        DepartementDto dto = new DepartementDto(dept.getId(), dept.getNomDep());
        return dto;
    }

    public List<DepartementDto> toDtos(List<Departement> departements) {
        return departements.stream()
                .map(d -> toDto(d))
                .toList();
    }

    public Departement toBean(DepartementDto dto) {
        Departement bean = new Departement();
        bean.setId(dto.getId());
        bean.setNomDep(dto.getNom());
        return bean;
    }
}
