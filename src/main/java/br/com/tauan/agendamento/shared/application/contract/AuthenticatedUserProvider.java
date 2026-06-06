package br.com.tauan.agendamento.shared.application.contract;

import java.util.UUID;

public interface AuthenticatedUserProvider {
    UUID getUserId();
    String getRole();
    boolean hasRole(String role);
}
