package com.program.app.application.users;


import org.springframework.stereotype.Service;
import com.program.app.interfaces.service.UserReportService;
import com.program.app.persistence.entity.UserEntity;
import reactor.core.publisher.Mono;

@Service
public class GetByIdUserUseCase {
    private final UserReportService service;

    public GetByIdUserUseCase(UserReportService service) {
        this.service = service;
    }

    public Mono<UserEntity> execute(Long id) {
        return service.findById(id);
    }
}