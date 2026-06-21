package tip.java.sistemacentrocrecer.biz.services;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.entities.*;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.GrupoRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.NinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ReporteRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableNinioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableRepository;
import tip.java.sistemacentrocrecer.dto.ReporteRequestDTO;
import tip.java.sistemacentrocrecer.dto.ReporteResponseDTO;
import tip.java.sistemacentrocrecer.mapper.DocumentoAdjuntoMapper;
import tip.java.sistemacentrocrecer.mapper.ReporteMapper;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class ReporteService {
    private final ReporteRepository reporteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final GrupoRepository grupoRepository;
    private final NinioRepository ninioRepository;
    private final ResponsableRepository responsableRepository;
    private final ResponsableNinioRepository responsableNinioRepository;

    private final ReporteMapper reporteMapper;
    private final DocumentoAdjuntoMapper documentoAdjuntoMapper;
    private final NotificacionService notificacionService;
    private final EmailService emailService;

    @Transactional
    public ReporteResponseDTO crearReporte(ReporteRequestDTO dto) {
        Reporte reporte = reporteMapper.toEntity(dto);
        reporte.setFechaGeneracion(new Date());
        reporte.setActivo(true);
        reporte.setVisto(false);

        // Funcionario
        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionario no encontrado"));
        reporte.setFuncionario(funcionario);

        // Primero guardar el reporte
        reporte = reporteRepository.save(reporte);

        // Grupos
        List<ReporteGrupo> reporteGrupos = new ArrayList<>();

        if (dto.getGruposIds() != null) {
            for (Integer grupoId : dto.getGruposIds()) {
                Grupo grupo = grupoRepository.findById(grupoId)
                        .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
                ReporteGrupo rg = new ReporteGrupo();
                rg.setReporte(reporte);
                rg.setGrupo(grupo);

                reporteGrupos.add(rg);

            }
        }
        reporte.setReporteGrupos(reporteGrupos);

        // Niños
        List<ReporteNinio> reporteNinios = new ArrayList<>();
        if (dto.getNiniosIds() != null) {
            for (Integer ninioId : dto.getNiniosIds()) {
                Ninio ninio = ninioRepository.findById(ninioId)
                        .orElseThrow(() -> new RuntimeException("Ninio no encontrado"));

                ReporteNinio rn = new ReporteNinio();
                rn.setReporte(reporte);
                rn.setNinio(ninio);

                reporteNinios.add(rn);
            }
        }
        reporte.setReporteNinios(reporteNinios);

        // Documentos
        if (dto.getDocumentos() != null) {

            List<DocumentoAdjunto> documentos = dto.getDocumentos()
                    .stream()
                    .map(documentoAdjuntoMapper::toEntity)
                    .collect(java.util.stream.Collectors.toList());

            for (DocumentoAdjunto doc : documentos) {
                doc.setReporte(reporte);
            }

            reporte.setDocumentos(documentos);
        }
        reporte = reporteRepository.save(reporte);

        notificarResponsablesNuevoReporte(reporte);

        return reporteMapper.toResponseDTO(reporte);

    }

    private void notificarResponsablesNuevoReporte(Reporte reporte) {
        Set<String> emailsNotificados = new LinkedHashSet<>();

        if (reporte.getReporteNinios() != null) {
            for (ReporteNinio reporteNinio : reporte.getReporteNinios()) {
                notificarResponsablesDeNinio(
                        reporteNinio.getNinio(),
                        reporte,
                        emailsNotificados
                );
            }
        }

        if (reporte.getReporteGrupos() != null) {
            for (ReporteGrupo reporteGrupo : reporte.getReporteGrupos()) {
                Grupo grupo = reporteGrupo.getGrupo();

                if (grupo.getNinios() != null) {
                    for (Ninio ninio : grupo.getNinios()) {
                        notificarResponsablesDeNinio(ninio, reporte, emailsNotificados);
                    }
                }
            }
        }
    }

    private void notificarResponsablesDeNinio(Ninio ninio, Reporte reporte, Set<String> emailsNotificados) {
        if (ninio == null || ninio.getId() == 0) {
            return;
        }

        List<ResponsableNinio> responsables = responsableNinioRepository.findByNinioId(ninio.getId());

        for (ResponsableNinio responsableNinio : responsables) {
            Responsable responsable = responsableNinio.getResponsable();

            if (responsable == null || Boolean.FALSE.equals(responsable.getActivo())) {
                continue;
            }

            String email = responsable.getEmail();

            if (email == null || email.isBlank() || !emailsNotificados.add(email)) {
                continue;
            }

            emailService.enviarNotificacionNuevoReporte(
                    email,
                    responsable.getNombre() + " " + responsable.getApellido(),
                    reporte.getTitulo(),
                    ninio.getNombre() + " " + ninio.getApellido()
            );
        }
    }

    public List<ReporteResponseDTO> listarTodos(){
        return reporteRepository.findAll()
                .stream()
                .map(reporteMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public ReporteResponseDTO actualizarReporte(Integer id, ReporteRequestDTO dto) {
        //Buscar reporte
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        reporte.setTitulo(dto.getTitulo());
        reporte.setDescripcion(dto.getDescripcion());

        // Funcionario
        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionario no encontrado"));
        reporte.setFuncionario(funcionario);

        // Grupos — limpiar la colección existente y repoblarla (nunca reemplazar la referencia con set)
        reporte.getReporteGrupos().clear();
        if (dto.getGruposIds() != null) {
            for (Integer grupoId : dto.getGruposIds()) {
                Grupo grupo = grupoRepository.findById(grupoId)
                        .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
                ReporteGrupo rg = new ReporteGrupo();
                rg.setReporte(reporte);
                rg.setGrupo(grupo);
                reporte.getReporteGrupos().add(rg);
            }
        }

        // Niños — igual que grupos, limpiar y repoblar
        reporte.getReporteNinios().clear();
        if (dto.getNiniosIds() != null) {
            for (Integer ninioId : dto.getNiniosIds()) {
                Ninio ninio = ninioRepository.findById(ninioId)
                        .orElseThrow(() -> new RuntimeException("Niño no encontrado"));
                ReporteNinio rn = new ReporteNinio();
                rn.setReporte(reporte);
                rn.setNinio(ninio);
                reporte.getReporteNinios().add(rn);
            }
        }

        // Documentos — igual
        if (dto.getDocumentos() != null) {
            reporte.getDocumentos().clear();
            final Reporte reporteRef = reporte;
            dto.getDocumentos().stream()
                    .map(documentoAdjuntoMapper::toEntity)
                    .forEach(doc -> {
                        doc.setReporte(reporteRef);
                        reporteRef.getDocumentos().add(doc);
                    });
        }

        reporte = reporteRepository.save(reporte);
        return reporteMapper.toResponseDTO(reporte);
    }


    @Transactional(readOnly = true)
    public ReporteResponseDTO obtenerPorId(Integer id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        return reporteMapper.toResponseDTO(reporte);
    }

    @Transactional
    public void darDeBaja(Integer id){
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        reporte.setActivo(false);
        reporte.setFechaBaja(LocalDateTime.now());
        reporteRepository.save(reporte);

    }

    public List<ReporteResponseDTO> listarActivos(){
        return reporteRepository.findByActivoTrue().stream()
                .map(reporteMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void eliminar(Integer id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        reporteRepository.delete(reporte);
    }

    @Transactional(readOnly = true)
    public byte[] exportarExcel(Integer id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte");

            // Estilos
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFFont headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            CellStyle labelStyle = workbook.createCellStyle();
            XSSFFont labelFont = workbook.createFont();
            labelFont.setBold(true);
            labelStyle.setFont(labelFont);

            // Título
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Reporte: " + reporte.getTitulo());
            CellStyle titleStyle = workbook.createCellStyle();
            XSSFFont titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

            // Metadatos
            String fechaStr = reporte.getFechaGeneracion() != null
                    ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(reporte.getFechaGeneracion())
                    : "-";
            String funcionarioNombre = reporte.getFuncionario() != null
                    ? reporte.getFuncionario().getNombre() + " " + reporte.getFuncionario().getApellido()
                    : "-";

            int rowIdx = 2;
            rowIdx = agregarFila(sheet, rowIdx, labelStyle, "Fecha de generación", fechaStr);
            rowIdx = agregarFila(sheet, rowIdx, labelStyle, "Funcionario", funcionarioNombre);
            rowIdx = agregarFila(sheet, rowIdx, labelStyle, "Descripción",
                    reporte.getDescripcion() != null ? reporte.getDescripcion() : "-");

            rowIdx++;

            // Grupos
            Row gruposHeader = sheet.createRow(rowIdx++);
            Cell ghCell = gruposHeader.createCell(0);
            ghCell.setCellValue("Grupos asociados");
            ghCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIdx - 1, rowIdx - 1, 0, 3));

            if (reporte.getReporteGrupos() != null && !reporte.getReporteGrupos().isEmpty()) {
                for (ReporteGrupo rg : reporte.getReporteGrupos()) {
                    Row r = sheet.createRow(rowIdx++);
                    r.createCell(0).setCellValue(rg.getGrupo().getNombre());
                }
            } else {
                sheet.createRow(rowIdx++).createCell(0).setCellValue("Sin grupos asociados");
            }

            rowIdx++;

            // Niños
            Row niniosHeader = sheet.createRow(rowIdx++);
            Cell nhCell = niniosHeader.createCell(0);
            nhCell.setCellValue("Niños asociados");
            nhCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIdx - 1, rowIdx - 1, 0, 3));

            if (reporte.getReporteNinios() != null && !reporte.getReporteNinios().isEmpty()) {
                Row nHeader = sheet.createRow(rowIdx++);
                String[] cols = {"Nombre", "Apellido", "Cédula", "Fecha Nac."};
                for (int i = 0; i < cols.length; i++) {
                    Cell c = nHeader.createCell(i);
                    c.setCellValue(cols[i]);
                    c.setCellStyle(labelStyle);
                }
                for (ReporteNinio rn : reporte.getReporteNinios()) {
                    Ninio n = rn.getNinio();
                    Row r = sheet.createRow(rowIdx++);
                    r.createCell(0).setCellValue(n.getNombre());
                    r.createCell(1).setCellValue(n.getApellido());
                    r.createCell(2).setCellValue(n.getCedula() != null ? n.getCedula() : "-");
                    r.createCell(3).setCellValue(n.getFechaNacimiento() != null ? n.getFechaNacimiento().toString() : "-");
                }
            } else {
                sheet.createRow(rowIdx++).createCell(0).setCellValue("Sin niños asociados");
            }

            for (int i = 0; i < 4; i++) sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }

    private int agregarFila(Sheet sheet, int rowIdx, CellStyle labelStyle, String label, String value) {
        Row row = sheet.createRow(rowIdx);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(labelStyle);
        row.createCell(1).setCellValue(value);
        return rowIdx + 1;
    }

    @Transactional(readOnly = true)
    public byte[] exportarPdf(Integer id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 40, 40, 50, 50);
            PdfWriter.getInstance(document, out);
            document.open();

            // Fuentes
            Font fontTitulo   = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(13, 71, 161));
            Font fontSeccion  = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);
            Font fontLabel    = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font fontValor    = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font fontColHeader= new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);

            Color colorPrimario  = new Color(13, 71, 161);   // azul oscuro
            Color colorSecundario= new Color(33, 150, 243);  // azul medio
            Color colorFila      = new Color(227, 242, 253); // azul claro

            // Título
            Paragraph titulo = new Paragraph(reporte.getTitulo(), fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(4);
            document.add(titulo);

            // Línea separadora (tabla de 1 celda como borde)
            PdfPTable linea = new PdfPTable(1);
            linea.setWidthPercentage(100);
            PdfPCell lineaCell = new PdfPCell();
            lineaCell.setBackgroundColor(colorPrimario);
            lineaCell.setFixedHeight(3);
            lineaCell.setBorder(Rectangle.NO_BORDER);
            linea.addCell(lineaCell);
            linea.setSpacingAfter(12);
            document.add(linea);

            // Metadatos
            String fechaStr = reporte.getFechaGeneracion() != null
                    ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(reporte.getFechaGeneracion()) : "-";
            String funcionarioNombre = reporte.getFuncionario() != null
                    ? reporte.getFuncionario().getNombre() + " " + reporte.getFuncionario().getApellido() : "-";

            PdfPTable metaTable = new PdfPTable(new float[]{2, 5});
            metaTable.setWidthPercentage(100);
            metaTable.setSpacingAfter(14);
            agregarFilaPdf(metaTable, "Fecha de generación:", fechaStr, fontLabel, fontValor);
            agregarFilaPdf(metaTable, "Funcionario:", funcionarioNombre, fontLabel, fontValor);
            agregarFilaPdf(metaTable, "Descripción:",
                    reporte.getDescripcion() != null ? reporte.getDescripcion() : "-", fontLabel, fontValor);
            document.add(metaTable);

            // Sección Grupos
            document.add(cabeceraSección("Grupos asociados", fontSeccion, colorSecundario));
            PdfPTable gruposTable = new PdfPTable(1);
            gruposTable.setWidthPercentage(100);
            gruposTable.setSpacingAfter(14);
            if (reporte.getReporteGrupos() != null && !reporte.getReporteGrupos().isEmpty()) {
                boolean par = false;
                for (ReporteGrupo rg : reporte.getReporteGrupos()) {
                    PdfPCell cell = new PdfPCell(new Phrase(rg.getGrupo().getNombre(), fontValor));
                    cell.setPadding(5);
                    cell.setBorderColor(new Color(200, 200, 200));
                    cell.setBackgroundColor(par ? colorFila : Color.WHITE);
                    gruposTable.addCell(cell);
                    par = !par;
                }
            } else {
                PdfPCell cell = new PdfPCell(new Phrase("Sin grupos asociados", fontValor));
                cell.setPadding(5);
                cell.setBorderColor(new Color(200, 200, 200));
                gruposTable.addCell(cell);
            }
            document.add(gruposTable);

            // Sección Niños
            document.add(cabeceraSección("Niños asociados", fontSeccion, colorSecundario));
            PdfPTable niniosTable = new PdfPTable(new float[]{3, 3, 2, 2});
            niniosTable.setWidthPercentage(100);
            niniosTable.setSpacingAfter(14);
            String[] headers = {"Nombre", "Apellido", "Cédula", "Fecha Nac."};
            for (String h : headers) {
                PdfPCell hCell = new PdfPCell(new Phrase(h, fontColHeader));
                hCell.setBackgroundColor(colorPrimario);
                hCell.setPadding(6);
                hCell.setBorder(Rectangle.NO_BORDER);
                niniosTable.addCell(hCell);
            }
            if (reporte.getReporteNinios() != null && !reporte.getReporteNinios().isEmpty()) {
                boolean par = false;
                for (ReporteNinio rn : reporte.getReporteNinios()) {
                    Ninio n = rn.getNinio();
                    Color bg = par ? colorFila : Color.WHITE;
                    String[] vals = {
                            n.getNombre(),
                            n.getApellido(),
                            n.getCedula() != null ? n.getCedula() : "-",
                            n.getFechaNacimiento() != null ? new SimpleDateFormat("dd/MM/yyyy").format(n.getFechaNacimiento()) : "-"
                    };
                    for (String v : vals) {
                        PdfPCell c = new PdfPCell(new Phrase(v, fontValor));
                        c.setPadding(5);
                        c.setBackgroundColor(bg);
                        c.setBorderColor(new Color(200, 200, 200));
                        niniosTable.addCell(c);
                    }
                    par = !par;
                }
            } else {
                PdfPCell cell = new PdfPCell(new Phrase("Sin niños asociados", fontValor));
                cell.setColspan(4);
                cell.setPadding(5);
                niniosTable.addCell(cell);
            }
            document.add(niniosTable);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el archivo PDF", e);
        }
    }

    private PdfPTable cabeceraSección(String texto, Font font, Color color) throws DocumentException {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        t.setSpacingBefore(6);
        t.setSpacingAfter(6);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(color);
        cell.setPadding(6);
        cell.setBorder(Rectangle.NO_BORDER);
        t.addCell(cell);
        return t;
    }

    private void agregarFilaPdf(PdfPTable table, String label, String value, Font fontLabel, Font fontValor) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, fontLabel));
        labelCell.setPadding(5);
        labelCell.setBorderColor(new Color(200, 200, 200));
        table.addCell(labelCell);
        PdfPCell valueCell = new PdfPCell(new Phrase(value, fontValor));
        valueCell.setPadding(5);
        valueCell.setBorderColor(new Color(200, 200, 200));
        table.addCell(valueCell);
    }

    @Transactional
    public void marcarComoVisto(Integer id, Integer responsableId) {
        Reporte reporte = reporteRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        Responsable responsable = responsableRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new AccessDeniedException("Responsable autenticado no encontrado"));

        if (!responsable.getId().equals(responsableId)) {
            throw new AccessDeniedException("No puede marcar reportes en nombre de otro responsable");
        }

        boolean puedeVerReporte =
                reporteRepository.existeParaResponsableViaNinio(id, responsableId) ||
                reporteRepository.existeParaResponsableViaGrupo(id, responsableId);

        if (!puedeVerReporte) {
            throw new AccessDeniedException("No tiene permisos para ver este reporte");
        }

        if (Boolean.TRUE.equals(reporte.getVisto())) {
            return;
        }

        reporte.setVisto(true);
        reporteRepository.save(reporte);

        String nombreResponsable = responsable.getNombre() + " " + responsable.getApellido();
        String nombreNinio = obtenerNombreNinioDelReporte(reporte);

        // Notificación interna al funcionario
        Funcionario funcionario = reporte.getFuncionario();
        if (funcionario != null) {
            notificacionService.crearNotificacion(
                    funcionario,
                    reporte,
                    nombreResponsable,
                    nombreNinio
            );

            // Email al funcionario
            if (funcionario.getEmail() != null && !funcionario.getEmail().isBlank()) {
                emailService.enviarNotificacionVisto(
                        funcionario.getEmail(),
                        funcionario.getNombre() + " " + funcionario.getApellido(),
                        reporte.getTitulo(),
                        nombreResponsable
                );
            }
        }
    }

    private String obtenerNombreNinioDelReporte(Reporte reporte) {
        if (reporte.getReporteNinios() == null || reporte.getReporteNinios().isEmpty()) {
            return null;
        }

        Ninio ninio = reporte.getReporteNinios().getFirst().getNinio();
        if (ninio == null) {
            return null;
        }

        return ninio.getNombre() + " " + ninio.getApellido();
    }

    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> listarActivosPorResponsable(Integer responsableId) {
        List<Reporte> porNinio = reporteRepository.findActivosByResponsableIdViaNinio(responsableId);
        List<Reporte> porGrupo = reporteRepository.findActivosByResponsableIdViaGrupo(responsableId);

        // Unir sin duplicados, preservando orden por fechaGeneracion DESC
        java.util.Map<Integer, Reporte> merged = new java.util.LinkedHashMap<>();
        for (Reporte r : porNinio) merged.put(r.getId(), r);
        for (Reporte r : porGrupo) merged.putIfAbsent(r.getId(), r);

        return merged.values().stream()
                .sorted(java.util.Comparator.comparing(
                        Reporte::getFechaGeneracion,
                        java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder())
                ))
                .map(reporteMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> listarPorNinio(Integer ninioId) {
        return reporteRepository.findByNinioId(ninioId)
                .stream()
                .map(reporteMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> listarActivosPorNinio(Integer ninioId) {
        return reporteRepository.findByNinioIdAndActivoTrue(ninioId)
                .stream()
                .map(reporteMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> listarPorGrupo(Integer grupoId) {
        return reporteRepository.findByGrupoId(grupoId)
                .stream()
                .map(reporteMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> listarActivosPorGrupo(Integer grupoId) {
        return reporteRepository.findByGrupoIdAndActivoTrue(grupoId)
                .stream()
                .map(reporteMapper::toResponseDTO)
                .toList();
    }
}
