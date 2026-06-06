package br.com.tauan.agendamento.user.application.contract;

public interface JwtProvider {
    String generateToken(String userId, String email, String role);
}
