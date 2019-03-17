package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.MemberUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "loginCheck")
public class LoginCheckController {

    @GetMapping
    public AppLoginResponse handleRequest(Authentication auth) {
        MemberUserDetails user = (MemberUserDetails) auth.getPrincipal();
        return new AppLoginResponse(user.getId().toString());
    }

    public class AppLoginResponse {

        private String memberID;

        public AppLoginResponse() {

        }

        public AppLoginResponse(String memberID) {
            this.memberID = memberID;
        }

        public String getMemberID() {
            return memberID;
        }

        public void setMemberID(String memberID) {
            this.memberID = memberID;
        }
    }
}
