package com.program.app.interfaces.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.program.app.persistence.entity.CategoryEntity;
import com.program.app.persistence.repository.CategoryRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class CategoryWriterService {

	private final CategoryRepository repository;

    public CategoryWriterService(CategoryRepository repository) {
        this.repository = repository;
    }
	
	 /**
     * Obtiene todas las categorías desde la base de datos (R2DBC)
     * y las guarda en un archivo TXT.
     */
    public Mono<Void> exportCategoriesToTxt(String filePath) {
        Flux<CategoryEntity> fluxCategories = repository.findAll();

        return fluxCategories
                .collectList()
                .flatMap(list -> writeToTxt(list, filePath));
    }

    /**
     * Escribe la lista de categorías en un archivo de texto.
     */
    private Mono<Void> writeToTxt(List<CategoryEntity> categories, String filePath) {
        return Mono.fromRunnable(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (CategoryEntity category : categories) {
                    writer.write(formatCategory(category));
                    writer.newLine();
                }
                System.out.println("✅ Archivo generado correctamente en: " + filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error al escribir el archivo TXT", e);
            }
        });
    }

    /**
     * Formatea cada categoría como una línea de texto.
     */
    private String formatCategory(CategoryEntity category) {
        return String.format("ID: %d | Nombre: %s | Título: %s | URL: %s | Imagen: %s | Icono: %s | Vistas: %d",
                category.getId(),
                category.getName(),
                category.getTitleList(),
                category.getUrl(),
                category.getImage(),
                category.getIcon(),
                category.getView());
    }
    
    /**
     * Exporta todas las categorías a un archivo Excel (.xlsx).
     */
    public Mono<Void> exportCategoriesToExcel(String filePath) {
        Flux<CategoryEntity> fluxCategories = repository.findAll();

        return fluxCategories
                .collectList()
                .flatMap(list -> writeToExcel(list, filePath));
    }
    
    /**
     * Escribe la lista de categorías en un archivo Excel usando Apache POI.
     */
    private Mono<Void> writeToExcel(List<CategoryEntity> categories, String filePath) {
        return Mono.fromRunnable(() -> {
            try (org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Categorías");

                // Crear fila de encabezado
                org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
                String[] headers = {"ID", "Nombre", "Título", "URL", "Imagen", "Icono", "Vistas"};
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }

                // Llenar filas con los datos
                int rowNum = 1;
                for (CategoryEntity category : categories) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(category.getId());
                    row.createCell(1).setCellValue(category.getName());
                    row.createCell(2).setCellValue(category.getTitleList());
                    row.createCell(3).setCellValue(category.getUrl());
                    row.createCell(4).setCellValue(category.getImage());
                    row.createCell(5).setCellValue(category.getIcon());
                    if (category.getView() != null)
                        row.createCell(6).setCellValue(category.getView());
                    else
                        row.createCell(6).setCellValue(0);
                }

                // Ajustar el ancho de las columnas automáticamente
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Guardar archivo en disco
                try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(filePath)) {
                    workbook.write(fileOut);
                }

                System.out.println("✅ Archivo Excel generado correctamente en: " + filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error al escribir archivo Excel", e);
            }
        });
    }

}
