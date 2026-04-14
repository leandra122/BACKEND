package com.timeright.tcc.services;

import com.timeright.tcc.model.entity.NivelAcesso;
import com.timeright.tcc.model.entity.Usuario;
import com.timeright.tcc.model.repository.NivelAcessoRepository;
import com.timeright.tcc.model.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

     @Autowired
    private NivelAcessoRepository nivelAcessoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id " + id));
    }

    // 🔐 CADASTRO COM CONFIRMAÇÃO DE EMAIL
    @Transactional
    public Usuario salvar(Usuario usuario) {

        Usuario _usuario = new Usuario();

        _usuario.setNome(usuario.getNome());
        _usuario.setUsername(usuario.getUsername());
        _usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        _usuario.setStatusUsuario("ATIVO");
        _usuario.setDataCadastro(LocalDateTime.now());

        // ✅ BUSCA REAL NO BANCO
        NivelAcesso nivel = nivelAcessoRepository.findById(usuario.getNivelAcesso().getId())
                .orElseThrow(() -> new RuntimeException("Nível de acesso não encontrado"));

        _usuario.setNivelAcesso(nivel);

        return usuarioRepository.save(_usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario usuario) {
        Usuario existente = findById(id);

        existente.setNome(usuario.getNome());
        existente.setUsername(usuario.getUsername());

        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        existente.setStatusUsuario(usuario.getStatusUsuario());
        existente.setNivelAcesso(usuario.getNivelAcesso());

        return usuarioRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

    // 🔐 LOGIN COM VERIFICAÇÃO DE EMAIL
    public Usuario validarLogin(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (passwordEncoder.matches(password, usuario.getPassword()) &&
                    "ATIVO".equals(usuario.getStatusUsuario())) {

                return usuario;
            }
        }

        return null;
    }

    // 🔐 CONFIRMAR EMAIL
    public String confirmarEmail(String token) {
        Usuario usuario = usuarioRepository.findByUsername(token).orElse(null);

        if (usuario != null) {
            usuario.setStatusUsuario("ATIVO");
            usuarioRepository.save(usuario);
            return "Email confirmado com sucesso!";
        }

        return "Token inválido!";
    }
}