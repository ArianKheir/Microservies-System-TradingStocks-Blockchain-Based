package com.stockbrokerage.user.service;

import com.stockbrokerage.user.dto.AuthResponse;
import com.stockbrokerage.user.dto.LoginRequest;
import com.stockbrokerage.user.dto.RegisterRequest;
import com.stockbrokerage.user.model.User;
import com.stockbrokerage.user.model.UserWallet;
import com.stockbrokerage.user.repository.UserRepository;
import com.stockbrokerage.user.repository.UserWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserWalletRepository walletRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEthereumAddress(request.getEthereumAddress());

        user = userRepository.save(user);

        // Create wallet for user
        UserWallet wallet = new UserWallet();
        wallet.setUserId(user.getId());
        wallet.setBalance(java.math.BigDecimal.valueOf(10000.00)); // Initial balance
        wallet.setCurrency("USD");
        walletRepository.save(wallet);

        String accessToken = jwtService.generateToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getEmail());

        return new AuthResponse(accessToken, refreshToken, "Bearer", user.getId(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOpt.get();
        String accessToken = jwtService.generateToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getEmail());

        return new AuthResponse(accessToken, refreshToken, "Bearer", user.getId(), user.getEmail());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

