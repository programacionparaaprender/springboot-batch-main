package com.program.app.application;

import org.springframework.stereotype.Service;

import com.program.app.interfaces.service.CategoryReportService;
import com.program.app.persistence.entity.CategoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ExportCategoriesToTxtUseCase {

    private final CategoryReportService service;

    public ExportCategoriesToTxtUseCase(CategoryReportService service) {
        this.service = service;
    }

    public Mono<Void> execute(String filePath) {
        //return service.exportCategoriesToTxt(filePath);
    	return service.exportCategoriesToExcel(filePath);
    }
}