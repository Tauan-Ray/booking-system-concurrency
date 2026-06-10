package br.com.tauan.agendamento.user.presentation.controller;

import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import br.com.tauan.agendamento.user.application.usecase.CreateUserUseCase;
import br.com.tauan.agendamento.user.application.usecase.DeactivateUserUseCase;
import br.com.tauan.agendamento.user.application.usecase.GetUserByIdUseCase;
import br.com.tauan.agendamento.user.application.usecase.ListUsersUseCase;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.presentation.dto.request.CreateUserRequest;
import br.com.tauan.agendamento.user.presentation.dto.response.UserResponse;
import br.com.tauan.agendamento.user.presentation.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(
        value = "/user",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class UserController {

    private final ListUsersUseCase listUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> listAllUsers() {
        List<User> users = listUsersUseCase.execute();

        return ResponseEntity.ok(
                ApiResponse.success(
                        UserMapper.toResponseList(users)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(
            @PathVariable UUID id
    ) {
       User user = getUserByIdUseCase.execute(id);

       return ResponseEntity.ok(
               ApiResponse.success(
                       UserMapper.toResponse(user)
               )
       );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        User user = createUserUseCase.execute(UserMapper.toInput(request));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(UserMapper.toResponse(user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable UUID id) {
        deactivateUserUseCase.execute(id);

        return ResponseEntity.ok(
                ApiResponse.success(null)
        );
    }
}
