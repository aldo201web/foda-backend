package mx.uv.foda.foda_backend.controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import mx.uv.foda.foda_backend.entitys.Factor;
import mx.uv.foda.foda_backend.repository.FactorRepository;
import mx.uv.foda.foda_backend.security.AuthService;
import mx.uv.foda.foda_backend.security.AuthSession;


import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reporte")
public class ReporteController {

    private final FactorRepository factorRepository;
    private final AuthService authService;

    public ReporteController(FactorRepository factorRepository, AuthService authService) {
        this.factorRepository = factorRepository;
        this.authService = authService;
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generarPdf(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        String token = AuthService.extractBearer(authHeader);
        AuthSession session = (token == null) ? null : authService.getSession(token);

        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        // Traer factores por empresa del usuario logueado
        List<Factor> factores = factorRepository.findByEmpresaIdOrderByCreatedAtDesc(session.getEmpresaId());

        byte[] pdfBytes = crearPdf(session.getUserName(), session.getEmpresaId(), factores);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        // Para que el navegador lo descargue con nombre
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("reporte_foda.pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private byte[] crearPdf(String nombreUsuario, Long empresaId, List<Factor> factores) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, baos);

        document.open();

        Font titulo = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 11, Font.NORMAL);

        document.add(new Paragraph("Reporte de Factores FODA", titulo));
        document.add(new Paragraph("Usuario: " + nombreUsuario, normal));
        document.add(new Paragraph("Empresa ID: " + empresaId, normal));

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        document.add(new Paragraph("Fecha: " + fecha, normal));
        document.add(Chunk.NEWLINE);

        // Tabla
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{20f, 60f, 20f});

        agregarHeader(table, "Tipo");
        agregarHeader(table, "Descripción");
        agregarHeader(table, "Impacto");

        for (Factor f : factores) {
            table.addCell(celda(f.getTipo()));
            table.addCell(celda(f.getDescripcion()));
            table.addCell(celda(String.valueOf(f.getImpacto())));
        }

        document.add(table);

        document.close();
        return baos.toByteArray();
    }

    private void agregarHeader(PdfPTable table, String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 11, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new Color(230, 230, 230));
        cell.setPadding(6);
        table.addCell(cell);
    }

    private PdfPCell celda(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 11, Font.NORMAL)));
        cell.setPadding(6);
        return cell;
    }
}