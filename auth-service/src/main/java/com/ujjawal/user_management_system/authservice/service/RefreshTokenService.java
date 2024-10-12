//package com.ujjawal.user_management_system.authservice.service;
//
//import com.ujjawal.user_management_system.authservice.model.RefreshTokenModel;
//import com.ujjawal.user_management_system.authservice.repository.RefreshTokenRepository;
//import com.ujjawal.user_management_system.authservice.exception.TokenRefreshException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Instant;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class RefreshTokenService {
//   @Value("${app.jwt.refresh.duration}")
//   private Long refreshTokenDurationMs;
//
//   @Autowired
//   private RefreshTokenRepository refreshTokenRepository;
//
//
//   @Autowired
//   private JwtService jwtService;
//
//   public Optional<RefreshTokenModel> findByToken(String token) {
//       return refreshTokenRepository.findByToken(token);
//   }
//
//   public RefreshTokenModel createRefreshToken(UUID userId) {
//       RefreshTokenModel refreshToken = new RefreshTokenModel();
//
//       refreshToken.setUserId(userId);
//       refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
//       refreshToken.setToken(UUID.randomUUID().toString());
//
//       refreshToken = refreshTokenRepository.save(refreshToken);
//       return refreshToken;
//   }
//
//   public RefreshTokenModel verifyExpiration(RefreshTokenModel token) {
//       if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
//           refreshTokenRepository.delete(token);
//           throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
//       }
//
//       return token;
//   }
//
//   @Transactional
//   public int deleteByUserId(UUID userId) {
//       return refreshTokenRepository.deleteByUserId(userId);
//   }
//
//   public String generateNewToken(User user) {
//       return jwtService.generateToken(user);
//   }
//}
