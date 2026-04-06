package com.timeright.tcc.services;

import com.timeright.tcc.model.entity.Usuario;
import com.timeright.tcc.model.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔹 LISTAR TODOS
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // 🔹 BUSCAR POR ID
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id " + id));
    }

    // 🔹 SALVAR
    @Transactional
    public Usuario salvar(Usuario usuario) {
        usuario.setStatusUsuario("ATIVO");
        return usuarioRepository.save(usuario);
    }

    // 🔹 ATUALIZAR
    @Transactional
    public Usuario atualizar(Long id, Usuario usuario) {
        Usuario existente = findById(id);

        existente.setNome(usuario.getNome());
        existente.setUsername(usuario.getUsername());
        existente.setPassword(usuario.getPassword());
        existente.setStatusUsuario(usuario.getStatusUsuario());
        existente.setNivelAcesso(usuario.getNivelAcesso());

        return usuarioRepository.save(existente);
    }

    // 🔹 DELETAR
    @Transactional
    public void deletar(Long id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

    // 🔹 LOGIN (AGORA CORRETO)
    public Usuario validarLogin(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario != null &&
            usuario.getPassword().equals(password) &&
            "ATIVO".equals(usuario.getStatusUsuario())) {

            return usuario;
        }

        return null;
    }
}