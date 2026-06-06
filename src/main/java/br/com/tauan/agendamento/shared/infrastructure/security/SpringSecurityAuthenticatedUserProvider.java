package br.com.tauan.agendamento.shared.infrastructure.security;

import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.UnauthenticatedUserException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SpringSecurityAuthenticatedUserProvider implements AuthenticatedUserProvider {

    @Override
    public UUID getUserId() {
        Authentication auth = getAuthentication();

        return UUID.fromString(auth.getPrincipal().toString());
    }

    @Override
    public String getRole() {
        Authentication auth = getAuthentication();

        return auth.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .orElseThrow(UnauthenticatedUserException::new);
    }

    @Override
    public boolean hasRole(String role) {
        return getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    private Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null
                || !auth.isAuthenticated()
                || auth instanceof AnonymousAuthenticationToken) {
            throw new UnauthenticatedUserException();
        }


        return auth;
    }
}