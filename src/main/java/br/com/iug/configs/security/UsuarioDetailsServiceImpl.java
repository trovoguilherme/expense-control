package br.com.iug.configs.security;

import br.com.iug.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: "+username));
        return new User(usuario.getUsername(), usuario.getPassword(), true, true, true, true, usuario.getAuthorities());
    }

}
