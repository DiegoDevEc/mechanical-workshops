package com.mechanical.workshops.service;

import com.mechanical.workshops.constants.Constants;
import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.UserResponseDto;
import com.mechanical.workshops.dto.UserSaveRequestDTO;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.exception.NotFoundException;
import com.mechanical.workshops.models.Person;
import com.mechanical.workshops.models.User;
import com.mechanical.workshops.repository.PersonRepository;
import com.mechanical.workshops.repository.UserRepository;
import com.mechanical.workshops.utils.IndentificationUtil;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public String login(String username, String password) {
        return "";
    }

    @Override
    public ResponseEntity<PageResponseDto> getAllUserActive(String text, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersActives = userRepository.findByStatusAndText(Status.ACT, text, pageable);

        List<UserResponseDto> userDtoList = usersActives.getContent().stream()
                .map(user -> {
                    UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
                    if (user.getPerson() != null) {
                        dto.setFirstname(user.getPerson().getFirstname());
                        dto.setLastname(user.getPerson().getLastname());
                        dto.setAddress(user.getPerson().getAddress());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(PageResponseDto.builder()
                        .content(userDtoList)
                        .pageNumber(usersActives.getNumber())
                        .pageSize(usersActives.getSize())
                        .totalElements(usersActives.getTotalElements())
                        .totalPages(usersActives.getTotalPages())
                .build());
    }

    @Override
    public ResponseEntity<String> delete(UUID userId) {
        User userData = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.USER_NOT_FOUND, userId))
        );
        userData.setStatus(Status.INA);

        userRepository.save(userData);

        return new ResponseEntity<>(String.format(Constants.USER_DELETED, userData.getUsername()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String>  register(UserSaveRequestDTO userSaveRequestDTO) {
        log.info("User {} trying to register", userSaveRequestDTO.getUsername());
        String encryptedPassword = passwordEncoder.encode(userSaveRequestDTO.getPassword());
        Person person = modelMapper.map(userSaveRequestDTO, Person.class);
        person.setStatus(Status.ACT);
        person = personRepository.save(person);

        User user = modelMapper.map(userSaveRequestDTO, User.class);
        user.setPassword(encryptedPassword);
        user.setStatus(Status.ACT);
        user.setPerson(person);

        userRepository.save(user);

        return ResponseEntity.ok(String.format(Constants.USER_CREATED, user.getUsername()));
    }

    @Override
    public ResponseEntity<String>  update(UUID userId, UserSaveRequestDTO userSaveRequestDTO) {

        User userData = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

        userData.getPerson().setFirstname(userSaveRequestDTO.getFirstname());
        userData.getPerson().setLastname(userSaveRequestDTO.getLastname());
        userData.getPerson().setAddress(userSaveRequestDTO.getAddress());
        userData.setUsername(userSaveRequestDTO.getUsername());
        userData.setPhone(userSaveRequestDTO.getPhone());
        userData.setEmail(userSaveRequestDTO.getEmail());

        userRepository.save(userData);

        return ResponseEntity.ok(String.format(Constants.USER_UPDATED, userData.getUsername()));
    }

    @Override
    public ResponseEntity<String> validateEmail(UUID userId, String email) {
        return validateField(userId, email, "email");
    }

    @Override
    public ResponseEntity<String> validatePhone(UUID userId, String phone) {
        return validateField(userId, phone, "phone");
    }

    @Override
    public ResponseEntity<String> validateUsername(UUID userId, String username) {
        return validateField(userId, username, "username");
    }

    @Override
    public ResponseEntity<String> validateIdentification(UUID userId, String identification) {
        return validateField(userId, identification, "identification");
    }

    @Override
    public ResponseEntity<String> validateValueIdentification(String value) {

        boolean isValid = IndentificationUtil.isValidCedula(value);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format(Constants.IDENTIFICATION_FORMAT_INVALID, value));
        }

        return ResponseEntity.ok(String.format(Constants.IDENTIFICATION_FORMAT_VALID, value));
    }

    private ResponseEntity<String> validateField(UUID userId, String value, String fieldName) {
        Optional<User> user = Optional.empty();
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
                        .body("Campo no válido para la validación");
        }

        if (user.isPresent()) {
            if (userId == null || !user.get().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(String.format(Constants.USER_ALREADY_EXISTS, fieldMessage));
            }
        }
        return ResponseEntity.ok(String.format(Constants.USER_VALID, fieldMessage));
    }

}
