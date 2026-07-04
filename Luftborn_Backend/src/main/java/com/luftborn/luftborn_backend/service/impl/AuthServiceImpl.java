package com.luftborn.luftborn_backend.service.impl;

import com.luftborn.luftborn_backend.dto.request.LoginRequest;
import com.luftborn.luftborn_backend.dto.request.RegisterRequest;
import com.luftborn.luftborn_backend.dto.response.AuthResult;
import com.luftborn.luftborn_backend.dto.response.UserResponse;
import com.luftborn.luftborn_backend.exception.DuplicateResourceException;
import com.luftborn.luftborn_backend.mapper.UserMapper;
import com.luftborn.luftborn_backend.model.User;
import com.luftborn.luftborn_backend.repository.UserRepository;
import com.luftborn.luftborn_backend.security.JwtService;
import com.luftborn.luftborn_backend.security.RefreshTokenService;
import com.luftborn.luftborn_backend.security.UserDetailsImpl;
import com.luftborn.luftborn_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResult register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username()))
            throw new DuplicateResourceException("Username: "+request.username()+ " already exists.");
        if (userRepository.existsByEmail(request.email()))
                throw new DuplicateResourceException("Email: "+request.email()+ " already exists.");
        User user = userMapper.toEntity(request);
        user.setHashedPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);

        UserDetailsImpl userDetails = new UserDetailsImpl(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getHashedPassword(),
                List.of()
        );
        String accessToken = jwtService.generateJwtToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.id());
        return new AuthResult(refreshToken, accessToken, userMapper.toResponse(savedUser));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResult login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.usernameOrEmail(), request.password())
        );
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtService.generateJwtToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.id());
        UserResponse userResponse = userMapper.toResponseFromDetails(userDetails);
        return new AuthResult(refreshToken, accessToken, userResponse);
    }

    @Override
    public Optional<AuthResult> rotateToken(String oldRawRefreshToken) {
        return refreshTokenService.rotateToken(oldRawRefreshToken)
                .map(rotatedToken -> {
                    Long userId = rotatedToken.userId();
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UsernameNotFoundException("User with ID: "+userId+" not found."));

                    UserDetailsImpl userDetails = new UserDetailsImpl(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getHashedPassword(),
                            List.of()
                    );
                    String accessToken = jwtService.generateJwtToken(userDetails);
                    UserResponse userResponse = userMapper.toResponse(user);
                    return new AuthResult(rotatedToken.newRawToken(), accessToken, userResponse);
                });
    }

    @Override
    public void logout(String refreshTokenFromCookie) {
        refreshTokenService.logout(refreshTokenFromCookie);
    }
    @Override
    public ResponseCookie generateEmptyCookie() {
        return refreshTokenService.generateEmptyCookie();
    }

    @Override
    public ResponseCookie generateCookie(String refreshToken) {
        return refreshTokenService.generateRefreshTokenCookie(refreshToken);
    }
}
