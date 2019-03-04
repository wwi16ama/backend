package com.WWI16AMA.backend_api.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberUserDetailService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OfficeRepository officeRepository;

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        member = memberRepository.findById(member.getId()).get();
        System.out.println(member.getOffices().get(0).getTitle());
//        optMember.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
//        return optMember.map(MemberUserDetails::new).get();

        return new MemberUserDetails(member);
    }
}
