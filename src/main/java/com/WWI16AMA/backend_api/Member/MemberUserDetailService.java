package com.WWI16AMA.backend_api.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MemberUserDetailService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // definitiv ungültige ID
        int id = -1;

        // Hier wird die Exception die fliegt _getauscht_
        try {
            id = Integer.parseInt(username);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Ungültiger Username");
        }

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        /*
        Die folgende Zeile ist notwendig, um die `offices` zu initialisieren, sonst
        kommt der lazy-fehler
         */
        member.getOffices().size();
        return new MemberUserDetails(member);
    }
}
