package com.program.app.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.program.app.interfaces.service.CategoryReportService;
import com.program.app.persistence.entity.CategoryEntity;
import com.program.app.persistence.entity.OrdersEntity;
import com.program.app.persistence.repository.OrdersRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReadCategoriesFromExcelUseCase {

    private final CategoryReportService service;

    public ReadCategoriesFromExcelUseCase(CategoryReportService service) {
        this.service = service;
    }

    public Mono<List<CategoryEntity>> execute(String filePath) {
        return service.readCategoriesFromExcel(filePath);
    }
}
