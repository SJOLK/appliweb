package fr.diginamic.appliweb.controleurs;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fr.diginamic.appliweb.entities.Departement;
import fr.diginamic.appliweb.entities.Ville;
import fr.diginamic.appliweb.exceptions.ExceptionFonctionnelle;
import fr.diginamic.appliweb.repositories.DepartementRepository;
import fr.diginamic.appliweb.services.DepartementService;
import fr.diginamic.appliweb.services.VilleService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/departements")
public class DepartementControleur {
    @Autowired
    private DepartementService departementService;

    @Autowired
    private DepartementRepository departementRepository;

    private final RestTemplate restTemplate;
    private final VilleService service;

    public DepartementControleur(RestTemplate restTemplate, VilleService service) {
        this.restTemplate = restTemplate;
        this.service = service;
    }

    @GetMapping("/{code}/export/pdf")
    public void exportPdf(
            @PathVariable String code,
            HttpServletResponse response
    ) throws DocumentException, IOException {
        // 1. Récupérer le nom du département via l’API externe
        String url = "https://geo.api.gouv.fr/departements/"
                + code
                + "?fields=nom,code,codeRegion";
        @SuppressWarnings("unchecked")
        Map<String, Object> dto = restTemplate.getForObject(url, Map.class);
        String nomDept = (dto != null && dto.get("nom") != null)
                ? dto.get("nom").toString()
                : "Inconnu";

        // 2. Préparer le PDF
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=departement_" + code + ".pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // 3. Titre
        document.add(new Paragraph("Département : "
                + nomDept
                + " (" + code + ")"));
        document.add(new Paragraph(" "));

        // 4. Table des villes
        List<Ville> villes = service.listerVillesDuDepartement(code);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        // En-têtes
        PdfPCell h1 = new PdfPCell(new Paragraph("Ville"));
        PdfPCell h2 = new PdfPCell(new Paragraph("Population"));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h1);
        table.addCell(h2);

        // Lignes
        for (Ville v : villes) {
            table.addCell(v.getNom());
            table.addCell(String.valueOf(v.getNbHabitants()));
        }

        document.add(table);
        document.close();
    }


    @GetMapping
    public List<Departement> listeDepartements() {
        return departementService.extractDepartements();
    }

    @GetMapping("/{id}")
    public Departement getDepartement(@PathVariable int id) {
        return departementService.extractDepartement(id);
    }

    @PostMapping
    public ResponseEntity<String> addDepartement(@Valid @RequestBody Departement departement) throws ExceptionFonctionnelle {
        departementService.insertDepartement(departement);

        return ResponseEntity.ok("Département inséré avec succès");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDepartement(@PathVariable long id, @Valid @RequestBody Departement departement) throws ExceptionFonctionnelle {
        departement.setId((int) id);
        departementService.modifierDepartement((long) id, departement);

        return ResponseEntity.ok("Département modifié avec succès");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartement(@PathVariable int id) {
        departementService.supprimerDepartement(id);

        return ResponseEntity.ok("Département supprimé avec succès");
    }
}

