package com.mechanical.workshops.security;


import com.mechanical.workshops.dto.AuthRequestDto;
import com.mechanical.workshops.dto.AuthResponseDto;
import com.mechanical.workshops.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthResponseDto authenticate(AuthRequestDto request){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Obtener el usuario autenticado correctamente
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generar el token con el UserDetails correcto
        var jwtToken = jwtService.generateToken(userDetails);
        return AuthResponseDto.builder()
                .accessToken(jwtToken)
                .build();
    }
}
