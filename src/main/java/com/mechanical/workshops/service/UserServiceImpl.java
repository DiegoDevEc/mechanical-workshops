package com.mechanical.workshops.service;

import com.mechanical.workshops.constants.Constants;
import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.dto.UserResponseDto;
import com.mechanical.workshops.dto.UserSaveRequestDTO;
import com.mechanical.workshops.enums.Role;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.exception.NotFoundException;
import com.mechanical.workshops.models.Person;
import com.mechanical.workshops.models.User;
import com.mechanical.workshops.repository.PersonRepository;
import com.mechanical.workshops.repository.UserRepository;
import com.mechanical.workshops.utils.EmailUtil;
import com.mechanical.workshops.utils.IndentificationUtil;
import com.mechanical.workshops.utils.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ResponseEntity<PageResponseDto> getAllUserActive(Role role, String text, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersActives = userRepository.findByStatusAndTextAndRole(Status.ACT, text, role, pageable);

        List<UserResponseDto> userDtoList = usersActives.getContent().stream()
                .map(user -> {
                    UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
                    if (user.getPerson() != null) {
                        dto.setFirstname(user.getPerson().getFirstname());
                        dto.setLastname(user.getPerson().getLastname());
                        dto.setAddress(user.getPerson().getAddress());
                        dto.setPersonId(user.getPerson().getId());
                    }

                    return dto;
                })
                .toList();

        return ResponseEntity.ok(PageResponseDto.builder()
                        .content(userDtoList)
                        .pageNumber(usersActives.getNumber())
                        .pageSize(usersActives.getSize())
                        .totalElements(usersActives.getTotalElements())
                        .totalPages(usersActives.getTotalPages())
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> delete(UUID userId) {
        User userData = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.USER_NOT_FOUND, userId))
        );
        userData.setStatus(Status.INA);

        userRepository.save(userData);

        return new ResponseEntity<>(
                ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message(String.format(Constants.USER_DELETED, userData.getUsername()))
                        .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseDto>  register(UserSaveRequestDTO userSaveRequestDTO) {
        log.info("User {} trying to register", userSaveRequestDTO.getUsername());

        if(userSaveRequestDTO.getMustChangePassword()){
            String password = PasswordUtil.generateRandomPassword();
            userSaveRequestDTO.setPassword(password);
            sendEmail(userSaveRequestDTO);
        }

        String encryptedPassword = passwordEncoder.encode(userSaveRequestDTO.getPassword());
        Person person = modelMapper.map(userSaveRequestDTO, Person.class);
        person.setStatus(Status.ACT);
        person = personRepository.save(person);

        User user = modelMapper.map(userSaveRequestDTO, User.class);
        user.setPassword(encryptedPassword);
        user.setStatus(Status.ACT);
        user.setPerson(person);

        userRepository.save(user);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message(String.format(Constants.USER_CREATED, user.getUsername()))
                        .build());
    }

    @Override
    public ResponseEntity<ResponseDto>  update(UUID userId, UserSaveRequestDTO userSaveRequestDTO) {

        User userData = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

        userData.getPerson().setFirstname(userSaveRequestDTO.getFirstname());
        userData.getPerson().setLastname(userSaveRequestDTO.getLastname());
        userData.getPerson().setAddress(userSaveRequestDTO.getAddress());
        userData.setUsername(userSaveRequestDTO.getUsername());
        userData.setPhone(userSaveRequestDTO.getPhone());
        userData.setEmail(userSaveRequestDTO.getEmail());

        userRepository.save(userData);

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.USER_UPDATED, userData.getUsername()))
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> validateEmail(UUID userId, String email) {
        return validateField(userId, email, "email");
    }

    @Override
    public ResponseEntity<ResponseDto> validatePhone(UUID userId, String phone) {
        return validateField(userId, phone, "phone");
    }

    @Override
    public ResponseEntity<ResponseDto> validateUsername(UUID userId, String username) {
        return validateField(userId, username, "username");
    }

    @Override
    public ResponseEntity<ResponseDto> validateIdentification(UUID userId, String identification) {
        return validateField(userId, identification, "identification");
    }

    @Override
    public ResponseEntity<ResponseDto> validateValueIdentification(String value) {

        boolean isValid = IndentificationUtil.isValidCedula(value);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(String.format(Constants.IDENTIFICATION_FORMAT_INVALID, value))
                                    .build());
        }

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message(String.format(Constants.IDENTIFICATION_FORMAT_VALID, value))
                        .build());
    }

    private ResponseEntity<ResponseDto> validateField(UUID userId, String value, String fieldName) {
        Optional<User> user;
        String fieldMessage = "";

        switch (fieldName) {
            case "phone":
                fieldMessage = Constants.PHONE;
                user = userRepository.findByPhone(value);
                break;
            case "email":
                fieldMessage = Constants.EMAIL;
                user = userRepository.findByEmail(value);
                break;
            case "username":
                fieldMessage = Constants.USERNAME;
                user = userRepository.findByUsername(value);
                break;
            case "identification":
                fieldMessage = Constants.IDENTIFICATION;
                user = userRepository.findByIdentification(value);
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .message("Campo no válido para la validación").build());
        }


            if (user.isPresent() &&  (userId == null || !user.get().getId().equals(userId))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .message(String.format(Constants.USER_ALREADY_EXISTS, fieldMessage))
                                        .build());
            }

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message(String.format(Constants.USER_VALID, fieldMessage))
                        .build());
    }

    private void sendEmail(UserSaveRequestDTO userSaveRequestDTO) {

        String toEmail = userSaveRequestDTO.getEmail();
        String subject = "Bienvenido";
        Map<String, String> parameters = new HashMap<>();

        parameters.put("password", userSaveRequestDTO.getPassword());
        parameters.put("name", userSaveRequestDTO.getFirstname() + " " + userSaveRequestDTO.getLastname());

        EmailUtil.sendEmail(toEmail, subject, "welcome_user_email_template", parameters);

    }


}
