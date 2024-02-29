package com.example.textr.security.utils;

import com.example.textr.dto.SubjectDto;
import com.example.textr.dto.UserDto;
import com.example.textr.utils.Constants;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtTokenUtils {
    @Autowired
    private redis.clients.jedis.JedisPool JedisPool;

    @Value("${redis.instance.key}")
    private String instanceKey;

    public String extractTokenValue(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(Constants.SIGNING_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(SubjectDto subjectDto) {
        final long expirationTime = subjectDto.getValidTill();
        return (System.currentTimeMillis()/1000) > expirationTime;
    }

    public String generateToken(UserDto user) {
        return doGenerateToken(user);
    }

    private String doGenerateToken(UserDto user) {

        Jedis jedis = JedisPool.getResource();
        String redisKey = createRedisKey(user.getId());
        long expireAt = (System.currentTimeMillis()/1000) +  Constants.ACCESS_TOKEN_VALIDITY_SECONDS;
        SubjectDto subject = new SubjectDto(user.getId(), (System.currentTimeMillis()/1000), expireAt,
                "");
        String subjectData = new Gson().toJson(subject);
        jedis.set(redisKey, subjectData);
        jedis.expireAt(redisKey, expireAt);
        Claims claims = Jwts.claims().setSubject(redisKey);
        jedis.close();
        return Jwts.builder().setClaims(claims).setIssuer(Constants.JWT_ISSUER).signWith(SignatureAlgorithm.HS256, Constants.SIGNING_KEY)
                .compact();
    }

    private String createRedisKey(Long userId) {
        return instanceKey + "-" + getSaltString() + "-" + userId;
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public SubjectDto getSubject(String redisKey) {
        Jedis jedis = JedisPool.getResource();
        String subjectData = jedis.get(redisKey);
        SubjectDto subject = new Gson().fromJson(subjectData, SubjectDto.class);
        jedis.close();
        return subject;
    }

    public Boolean validateToken(SubjectDto subject) {
        return (subject != null && !isTokenExpired(subject));
    }

    public void deleteSession(String redisKey) {
        Jedis jedis = JedisPool.getResource();
        jedis.del(redisKey);
        jedis.close();
    }

    public void deleteSessions(String keyPattern) {
        Jedis jedis = JedisPool.getResource();
        Set<String> sessionKeys = jedis.keys(keyPattern);
        sessionKeys.iterator().forEachRemaining(key -> deleteSession(key));
        jedis.close();
    }

}
