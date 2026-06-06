package br.com.tauan.agendamento.shared.application.contract;

public interface JwtProvider {
    String generateToken(String userId, String email, String role);
}
