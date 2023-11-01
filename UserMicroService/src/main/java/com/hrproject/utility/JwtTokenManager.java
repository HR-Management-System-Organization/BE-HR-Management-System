package com.hrproject.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hrproject.exception.ErrorType;
import com.hrproject.exception.UserManagerException;
import com.hrproject.repository.enums.ERole;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenManager {

    String secretKey = "secretKey";

    String issuer = "issuer";

    public Optional<String> createToken(Long id) {

        String token = null;

        Date date = new Date(System.currentTimeMillis() + (1000 * 60 * 50));

        try {

            token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("myId", id)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC512(secretKey));

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        return Optional.ofNullable(token);
    }

    public Optional<String> createToken(Long id, ERole role) {

        String token = null;

        Date date = new Date(System.currentTimeMillis() + (1000 * 60 * 5));

        try {

            token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("myId", id)
                    .withClaim("role", role.toString())
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC512(secretKey));

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        return Optional.ofNullable(token);
    }

    public Optional<Long> getIdFromToken(String token) {

        try {

            Algorithm algorithm = Algorithm.HMAC512(secretKey);

            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();

            DecodedJWT decodedJWT = verifier.verify(token);

            if (decodedJWT == null) {
                throw new UserManagerException(ErrorType.INVALID_TOKEN);
            }

            Long id = decodedJWT.getClaim("myId").asLong();

            return Optional.of(id);

        } catch (Exception e) {

            System.out.println(e.toString());

            throw new UserManagerException(ErrorType.INVALID_TOKEN);

        }
    }

    public Optional<String> getRoleFromToken(String token) {

        try {

            Algorithm algorithm = Algorithm.HMAC512(secretKey);

            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();

            DecodedJWT decodedJWT = verifier.verify(token);

            if (decodedJWT == null) {
                throw new UserManagerException(ErrorType.INVALID_TOKEN);
            }

            String role = decodedJWT.getClaim("role").asString();

            return Optional.of(role);

        } catch (Exception e) {

            System.out.println(e.toString());

            throw new UserManagerException(ErrorType.INVALID_TOKEN);

        }

    }

    public Optional<String> createToken(Long id, String activationCode) {

        String token = null;

        Date date = new Date(System.currentTimeMillis() + (1000 * 60 * 10));

        try {

            token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("myId", id)
                    .withClaim("activation", activationCode)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC512(secretKey));

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        return Optional.ofNullable(token);
    }

    public Optional<String> getActivationCode(String token) {

        try {

            Algorithm algorithm = Algorithm.HMAC512(secretKey);

            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();

            DecodedJWT decodedJWT = verifier.verify(token);

            if (decodedJWT == null) {
                throw new UserManagerException(ErrorType.INVALID_TOKEN);
            }


            String activationCode = decodedJWT.getClaim("activation").asString();

            return Optional.of(activationCode);

        } catch (Exception e) {

            System.out.println(e.toString());

            throw new UserManagerException(ErrorType.INVALID_TOKEN);

        }
    }

    public Optional<String> getUsername(String token) {

        try {

            Algorithm algorithm = Algorithm.HMAC512(secretKey);

            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();

            DecodedJWT decodedJWT = verifier.verify(token);

            if (decodedJWT == null) {
                throw new UserManagerException(ErrorType.INVALID_TOKEN);
            }


            String activationCode = decodedJWT.getClaim("name").asString();

            return Optional.of(activationCode);

        } catch (Exception e) {

            System.out.println(e.toString());

            throw new UserManagerException(ErrorType.INVALID_TOKEN);

        }
    }

    public Boolean verifyToken(String token) {

        try {

            Algorithm algorithm = Algorithm.HMAC512(secretKey);

            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();

            DecodedJWT decodedJWT = verifier.verify(token);

            if (decodedJWT == null) return false;

        } catch (Exception e) {

            return false;
        }

        return true;
    }


}