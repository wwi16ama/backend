package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.MemberUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "loginCheck")
public class LoginCheckController {

    @GetMapping
    public Collection<String> handleRequest(Authentication auth) {
        MemberUserDetails user = (MemberUserDetails) auth.getPrincipal();
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map((str) -> str.substring(5))
                .collect(Collectors.toList());
    }

}
