package com.gsmhrm.anything_back.domain.auth.service;

import com.gsmhrm.anything_back.domain.auth.exception.DuplicatedEmailException;
import com.gsmhrm.anything_back.domain.auth.presentation.dto.request.SignUpRequest;
import com.gsmhrm.anything_back.domain.email.entity.Email;
import com.gsmhrm.anything_back.domain.kakao.presentation.dto.KakaoUserInfo;
import com.gsmhrm.anything_back.domain.member.entity.Member;
import com.gsmhrm.anything_back.domain.member.repository.MemberRepository;
import com.gsmhrm.anything_back.global.annotation.RollbackService;
import com.gsmhrm.anything_back.global.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@RollbackService
@RequiredArgsConstructor
public class MemberSignUpService {

     private final MemberRepository memberRepository;
     private final PasswordEncoder passwordEncoder;
     private final EmailUtil emailUtil;

     public void execute(SignUpRequest request) {
         if (memberRepository.existsByEmail(request.getEmail())) {
             throw new DuplicatedEmailException();
         }

         Email emailAuth = emailUtil.getEmail(request.getEmail());

         emailUtil.checkEmailAuthentication(emailAuth);

         Member member = Member.builder()
                 .email(request.getEmail())
                 .name(request.getName())
                 .password(passwordEncoder.encode(request.getPassword()))
                 .build();

         memberRepository.save(member);
     }

     public Member execute(KakaoUserInfo kakaoUserInfo) {

         String kakaoEmail = kakaoUserInfo.getEmail();
         String nickname = kakaoUserInfo.getNickname();

         Member kakaoMember = memberRepository.findByEmail(kakaoEmail)
                 .orElse(null);

         if (kakaoMember == null) {
             String password = UUID.randomUUID().toString();
             String encodePassword = passwordEncoder.encode(password);

            kakaoMember = Member.builder()
                     .email(kakaoEmail)
                     .name(nickname)
                     .password(encodePassword)
                     .build();

             memberRepository.save(kakaoMember);
         } else return kakaoMember;

         return kakaoMember;
     }
}
