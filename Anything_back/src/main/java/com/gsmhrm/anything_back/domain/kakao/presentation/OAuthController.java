package com.gsmhrm.anything_back.domain.kakao.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gsmhrm.anything_back.domain.auth.presentation.dto.response.NewTokenResponse;
import com.gsmhrm.anything_back.domain.auth.presentation.dto.response.SignInResponse;
import com.gsmhrm.anything_back.domain.auth.service.MemberLogoutService;
import com.gsmhrm.anything_back.domain.auth.service.NewTokenService;
import com.gsmhrm.anything_back.domain.kakao.presentation.dto.KakaoUserInfo;
import com.gsmhrm.anything_back.domain.kakao.service.KakaoUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoUserService kakaoUserService;
    private final MemberLogoutService memberLogoutService;
    private final NewTokenService newTokenService;

    @RequestMapping("/oauth2/kakao")
    @ResponseBody
    @GetMapping
    public ResponseEntity<SignInResponse> handleKakao(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        SignInResponse signInResponse = kakaoUserService.kakaoLogin(code, response);
        return new ResponseEntity<>(signInResponse, HttpStatus.OK);
    }

    @RequestMapping("/oauth2/logout")
    @DeleteMapping
    public ResponseEntity<Void> kakaoLogout(@RequestHeader("Authorization") String accessToken) throws JsonProcessingException {
        memberLogoutService.execute(accessToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping("oauth2/newToken")
    @PatchMapping
    public ResponseEntity<NewTokenResponse> kakaoReToken(@RequestHeader("RefreshToken") String token) throws JsonProcessingException {
        NewTokenResponse newTokenResponse = newTokenService.execute(token);
        return new ResponseEntity<>(newTokenResponse, HttpStatus.OK);
    }
}
