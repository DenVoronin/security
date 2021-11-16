package ru.ibs.trainee.spring.securityjwt.controller;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import io.jsonwebtoken.JwtException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ibs.trainee.spring.securityjwt.jwt.JwtProvider;
import ru.ibs.trainee.spring.securityjwt.model.Employee;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@RestController

public class NewTokensController {



    @GetMapping("login/token")

    public Map<String, String> getTokens(HttpServletRequest request)
    {
        try{
        JwtProvider jwtProvider = new JwtProvider();

        String rftoken = jwtProvider.resolveToken(request);


            if (rftoken != null)
            {

        String tokenNew = jwtProvider.createToken(rftoken);
        String rftokenNew = jwtProvider.createRefreshToken(rftoken);
        HashMap<String, String> map = new HashMap<>();
        map.put("NEW ACCESS TOKEN", tokenNew);
        map.put("NEW REFRESH TOKEN", rftokenNew);
                map.put("NEW REFRESH TOKEN", rftokenNew);
        return map;
            }
           else {
                HashMap<String, String> map = new HashMap<>();
                map.put("Error", "empty token");
               return map;
            }
    } catch (Exception e) {
            HashMap<String, String> map = new HashMap<>();
            map.put("Error", "Not refresh token or damaged token ");
            return map;
        }

    } }
