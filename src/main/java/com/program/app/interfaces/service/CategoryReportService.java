package com.program.app.interfaces.service;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.program.app.persistence.entity.CategoryEntity;
import com.program.app.persistence.repository.CategoryRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryReportService {

    private final CategoryRepository repository;

    public CategoryReportService(CategoryRepository repository) {
        this.repository = repository;
    }

    // Obtener todas las categorías
    public Flux<CategoryEntity> findAll() {
        return repository.findAll();
    }

    // Buscar categoría por ID
    public Mono<CategoryEntity> findById(Long id) {
        return repository.findById(id);
    }

    // Buscar categoría por nombre
    public Flux<CategoryEntity> findByName(String name) {
        return repository.findByName(name);
    }

    // Buscar categoría por URL (única)
    public Mono<CategoryEntity> findByUrl(String url) {
        return repository.findByUrl(url);
    }
    
    
    /**
     * Lee las categorías desde un archivo Excel (.xlsx) y las convierte a una lista de CategoryEntity.
     */
    public Mono<List<CategoryEntity>> readCategoriesFromExcel(String filePath) {
        return Mono.fromCallable(() -> {
            List<CategoryEntity> categories = new ArrayList<>();

            try (FileInputStream file = new FileInputStream(filePath);
                 org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook(file)) {

                org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0); // Primera hoja
                boolean isHeader = true;

                for (org.apache.poi.ss.usermodel.Row row : sheet) {
                    // Saltar la fila de encabezados
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }

                    CategoryEntity category = new CategoryEntity();

                    // Leer cada celda según el orden de columnas
                    category.setId(getLongValue(row.getCell(0)));
                    category.setName(getStringValue(row.getCell(1)));
                    category.setTitleList(getStringValue(row.getCell(2)));
                    category.setUrl(getStringValue(row.getCell(3)));
                    category.setImage(getStringValue(row.getCell(4)));
                    category.setIcon(getStringValue(row.getCell(5)));
                    category.setView(getIntValue(row.getCell(6)));

                    categories.add(category);
                }

                System.out.println("✅ Se leyeron " + categories.size() + " categorías desde " + filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error al leer archivo Excel", e);
            }

            return categories;
        });
    }

    /**
     * Métodos auxiliares para convertir valores de celdas de forma segura.
     */
    private String getStringValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) return "";
        cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private Long getLongValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) return null;
        try {
            return (long) cell.getNumericCellValue();
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getIntValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) return null;
        try {
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            return null;
        }
    }

    
    
    
    
    
    
    
   
    
}
