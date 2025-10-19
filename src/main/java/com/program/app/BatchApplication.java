package com.program.app;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import com.program.app.application.ExportCategoriesToTxtUseCase;
import com.program.app.application.GetAllCategoriesUseCase;
import com.program.app.persistence.entity.CategoryEntity;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "com.program.app.persistence.repository")
public class BatchApplication implements CommandLineRunner {
	@Autowired
    public GetAllCategoriesUseCase getAllCategoriesUseCase;
	@Autowired
    public ExportCategoriesToTxtUseCase exportCategoriesToTxtUseCase;
//  private final GetCategoryByIdUseCase getCategoryByIdUseCase;
//  private final UpdateCategoryUseCase updateCategoryUseCase;
//  private final DeleteCategoryUseCase deleteCategoryUseCase;

  public BatchApplication() {

  }
    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            String outputPath = new File("categories.txt").getAbsolutePath();
            String outputPathExcel = new File("categories.xlsx").getAbsolutePath();
            List<CategoryEntity> listCategories = getAllCategoriesUseCase.execute()
                    .collectList()
                    .block(); // Esto sí ejecuta la query

            if (listCategories == null || listCategories.isEmpty()) {
                System.out.println("⚠️ No se encontraron categorías en la base de datos.");
                return;
            }

            exportCategoriesToTxtUseCase.execute(outputPathExcel).block(); // <-- AQUÍ está la clave
            System.out.println("✅ Archivo generado correctamente en: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
