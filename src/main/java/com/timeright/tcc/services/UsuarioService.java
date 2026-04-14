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

    // 🔹 LISTAR TODOS
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // 🔹 BUSCAR POR ID
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id " + id));
    }

    // 🔐 SALVAR
    @Transactional
    public Usuario salvar(Usuario usuario) {

        Usuario novo = new Usuario();

        novo.setNome(usuario.getNome());
        novo.setUsername(usuario.getUsername());
        novo.setPassword(passwordEncoder.encode(usuario.getPassword()));
        novo.setStatusUsuario("ATIVO");
        novo.setDataCadastro(LocalDateTime.now());

        // 🔗 busca nível corretamente
        NivelAcesso nivel = nivelAcessoRepository
                .findById(usuario.getNivelAcesso().getId())
                .orElseThrow(() -> new RuntimeException("Nível de acesso não encontrado"));

        novo.setNivelAcesso(nivel);

        return usuarioRepository.save(novo);
    }

    // 🔄 ATUALIZAR (CORRIGIDO)
    @Transactional
public Usuario atualizar(Long id, Usuario usuario) {

    Usuario existente = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    existente.setNome("TESTE ALTERACAO FORCADA");

    Usuario salvo = usuarioRepository.save(existente);

    System.out.println("SALVO NO BANCO: " + salvo.getNome());

    return salvo;
}
    // 🔹 DELETAR
    @Transactional
    public void deletar(Long id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

    // 🔐 LOGIN
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