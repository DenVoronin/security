package ru.ibs.trainee.spring.securityjwt.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.ibs.trainee.spring.securityjwt.auth.ApplicationUser;
import ru.ibs.trainee.spring.securityjwt.auth.ApplicationUserDao;
import ru.ibs.trainee.spring.securityjwt.auth.ApplicationUserService;
import ru.ibs.trainee.spring.securityjwt.auth.FakeApplicationUserDao;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final static String BEARER_PREFIX = "Bearer ";
    private final static String KEY = "securesecuresecuresecuresecuresecuresecures";
    private final static String REFRESH_KEY = "@#kBokOpfkf39(9iJij22002jf!Lk35ffg4gg4###f_";
    public String createToken(Authentication authentication) {
        return BEARER_PREFIX +  Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authentication.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDateTime.now().toLocalDate().plusWeeks(1)))
                .signWith(Keys.hmacShaKeyFor(KEY.getBytes()))
                .compact();
    }

    public String createToken(String token) {

        JwtProvider jwtProvider = new JwtProvider();
FakeApplicationUserDao fakeApplicationUserDao = new FakeApplicationUserDao(new PasswordEncoder() {
    @Override
    public String encode(CharSequence charSequence) {
        return null;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return false;
    }
});
ApplicationUserService applicationUserService = new ApplicationUserService(fakeApplicationUserDao);


        return BEARER_PREFIX +  Jwts.builder()
                .setSubject(jwtProvider.getRFUsername(token))
                .claim("authorities", applicationUserService.
                        loadUserByUsername(jwtProvider.getRFUsername(token)).getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDateTime.now().toLocalDate().plusWeeks(1)))
                .signWith(Keys.hmacShaKeyFor(KEY.getBytes()))
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        return BEARER_PREFIX +  Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDateTime.now().toLocalDate().plusDays(30)))
                .signWith(Keys.hmacShaKeyFor(REFRESH_KEY.getBytes()))
                .compact();
    }
    public String createRefreshToken(String rftoken) {
        JwtProvider jwtProvider = new JwtProvider();
        return BEARER_PREFIX +  Jwts.builder()
                .setSubject(jwtProvider.getRFUsername(rftoken))
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDateTime.now().toLocalDate().plusDays(30)))
                .signWith(Keys.hmacShaKeyFor(REFRESH_KEY.getBytes()))
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(KEY.getBytes()))
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
    public String getRFUsername(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(REFRESH_KEY.getBytes()))
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
    public List<GrantedAuthority> getAuthorities(String token) {
        return ((List<Map<String, String>>) Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(KEY.getBytes()))
                .parseClaimsJws(token)
                .getBody()
                .get("authorities"))
                    .stream()
                    .map(map -> new SimpleGrantedAuthority(map.get("authority")))
                    .collect(Collectors.toList());

    }


    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.isNullOrEmpty(header) || !header.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return header.replace(BEARER_PREFIX, "");
    }
}
