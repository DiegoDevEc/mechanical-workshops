package com.mechanical.workshops.security;

import com.mechanical.workshops.constants.Constants;
import com.mechanical.workshops.dto.AuthRequestDto;
import com.mechanical.workshops.dto.AuthResponseDto;
import com.mechanical.workshops.dto.ChangePasswordDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.models.User;
import com.mechanical.workshops.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthResponseDto authenticate(AuthRequestDto request){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Obtener el usuario autenticado correctamente
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByUsernameOrPhoneOrEmailOrIdentificationAndStatus(request.getUsername(), Status.ACT)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not exists by Username or Email or Phone or Identification"));

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", userDetails.getAuthorities());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("phone", user.getPhone());
        extraClaims.put("identification", user.getIdentification());
        extraClaims.put("firstname", user.getPerson().getFirstname());
        extraClaims.put("lastname", user.getPerson().getLastname());
        extraClaims.put("address", user.getPerson().getAddress());
        extraClaims.put("changePassword", user.getMustChangePassword());
        extraClaims.put("key-master", user.getPerson().getId());

        // Generar el token con el UserDetails correcto
        var jwtToken = jwtService.generateToken(extraClaims, userDetails);
        return AuthResponseDto.builder()
                .accessToken(jwtToken)
                .build();
    }

    public ResponseEntity<ResponseDto> changePassword(ChangePasswordDto changePasswordDto) {

        User user = userRepository.findByEmail(changePasswordDto.getEmail())
                .orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format(Constants.USER_NOT_FOUND, changePasswordDto.getEmail())
        ));

        String encryptedPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());
        user.setPassword(encryptedPassword);
        user.setMustChangePassword(false);
        userRepository.save(user);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message(String.format(Constants.USER_PASSWORD_CHANGED, user.getUsername()))
                        .build());
    }
}
