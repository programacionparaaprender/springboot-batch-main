package com.program.app.application.users;


import org.springframework.stereotype.Service;
import com.program.app.interfaces.service.UserRemovableService;
import com.program.app.interfaces.service.UserReportService;
import com.program.app.persistence.entity.UserEntity;
import reactor.core.publisher.Mono;

@Service
public class UserRemovableUseCase {
    private final UserRemovableService service;

    public UserRemovableUseCase(UserRemovableService service) {
        this.service = service;
    }

    public Mono<Void> execute(Long id) {
        return service.delete(id);
    }
}