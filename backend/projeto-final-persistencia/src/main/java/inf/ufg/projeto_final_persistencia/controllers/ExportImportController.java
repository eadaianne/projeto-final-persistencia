package inf.ufg.projeto_final_persistencia.controllers;

import inf.ufg.projeto_final_persistencia.services.ExportImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api")
public class ExportImportController {

    @Autowired
    private ExportImportService service;

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> exportAll(@RequestParam(defaultValue = "json") String formato) throws Exception {
        if (!"json".equalsIgnoreCase(formato)) {
            return ResponseEntity.badRequest().body(("formato "+formato+" não suportado").getBytes());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        service.exportAll(out);

        byte[] bytes = out.toByteArray();
        String filename = "export_pontos.json";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(bytes);
    }

    @PostMapping(value = "/import", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> importData(@RequestPart(required = false, name = "file") MultipartFile file,
                                        @RequestBody(required = false) byte[] rawBody) throws Exception {
        if (file != null) {
            service.importFrom(file.getInputStream());
            return ResponseEntity.ok("Import realizado com sucesso (arquivo).");
        } else if (rawBody != null && rawBody.length > 0) {
            service.importFrom(new java.io.ByteArrayInputStream(rawBody));
            return ResponseEntity.ok("Import realizado com sucesso (body).");
        } else {
            return ResponseEntity.badRequest().body("Nenhum arquivo ou body recebido.");
        }
    }
}
