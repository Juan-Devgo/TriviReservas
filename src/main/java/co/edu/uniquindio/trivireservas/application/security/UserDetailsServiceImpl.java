package co.edu.uniquindio.trivireservas.application.security;

import co.edu.uniquindio.trivireservas.domain.AbstractUser;
import co.edu.uniquindio.trivireservas.infrastructure.persistence.repository.AbstractUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AbstractUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
        AbstractUser user = userRepository.getUserByUUID(UUID.fromString(uuid));

        if (user == null) {
            throw new UsernameNotFoundException("User not found with UUID: " + uuid);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new User(uuid, user.getPassword(), authorities);
    }
}
