package com.WWI16AMA.backend_api.Member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class MemberUserDetails extends Member implements UserDetails {

    public MemberUserDetails(final Member member) {
        super(member);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<SimpleGrantedAuthority> authorities = getOffices()
                .stream()
                .map(off -> new SimpleGrantedAuthority("ROLE_" + off.toString()))
                .collect(Collectors.toList());

        if (getStatus() == Status.ACTIVE) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ACTIVE"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_PASSIVE"));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return getId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted();
    }
}
