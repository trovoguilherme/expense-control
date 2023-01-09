package br.com.iug.controller;

import br.com.iug.entity.Usuario;
import br.com.iug.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

}
