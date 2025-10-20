package ru.don_polesie.back_end.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.auth.JwtAuthRequest;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.dto.auth.RegisterRequest;
import ru.don_polesie.back_end.model.Role;
import ru.don_polesie.back_end.model.User;
import ru.don_polesie.back_end.repository.RoleRepository;
import ru.don_polesie.back_end.repository.UserRepository;
import ru.don_polesie.back_end.security.admin.JwtTokenProvider;
import ru.don_polesie.back_end.service.AuthService;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userServiceImpl;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public JwtAuthResponse login(JwtAuthRequest loginRequest) {
        var jwtResponse = new JwtAuthResponse();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword())
        );

        var user = userServiceImpl.getByUsername(loginRequest.getUsername());

        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(
                user.getId(), user.getUsername(), user.getRoles())
        );
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(
                user.getId(), user.getUsername())
        );
        return jwtResponse;
    }


    @Override
    public JwtAuthResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserToken(refreshToken);
    }

    @Override
    @Transactional
    public void save(RegisterRequest request) {
        var user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        user.setRoles(roles);
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        userRepository.save(user);
    }
}
