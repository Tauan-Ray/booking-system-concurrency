package br.com.tauan.agendamento.user.presentation.controller;

import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import br.com.tauan.agendamento.user.application.dto.UserOutput;
import br.com.tauan.agendamento.user.application.usecase.CreateUserUseCase;
import br.com.tauan.agendamento.user.application.usecase.DeactivateUserUseCase;
import br.com.tauan.agendamento.user.application.usecase.GetUserByIdUseCase;
import br.com.tauan.agendamento.user.application.usecase.ListUsersUseCase;
import br.com.tauan.agendamento.user.presentation.docs.UserControllerDocs;
import br.com.tauan.agendamento.user.presentation.dto.request.CreateUserRequest;
import br.com.tauan.agendamento.user.presentation.dto.response.UserResponse;
import br.com.tauan.agendamento.user.presentation.mapper.UserApiMapper;
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
public class UserController implements UserControllerDocs {

    private final ListUsersUseCase listUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> listAllUsers() {
        List<UserOutput> users = listUsersUseCase.execute();

        return ResponseEntity.ok(
                ApiResponse.success(
                        UserApiMapper.toResponseList(users)
                )
        );
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable UUID id) {
       UserOutput user = getUserByIdUseCase.execute(id);

       return ResponseEntity.ok(
               ApiResponse.success(
                       UserApiMapper.toResponse(user)
               )
       );
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        UserOutput user = createUserUseCase.execute(UserApiMapper.toInput(request));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(UserApiMapper.toResponse(user)));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable UUID id) {
        deactivateUserUseCase.execute(id);

        return ResponseEntity.ok(
                ApiResponse.success(null)
        );
    }
}
