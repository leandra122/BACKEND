package com.timeright.tcc.services;

import com.timeright.tcc.model.entity.Usuario;
import com.timeright.tcc.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

    @Service
    public class UsuarioService {

        @Autowired  // Injeção de dependência automática
        private UsuarioRepository usuarioRepository;

        // listar usuarios cadastrados

        public List<Usuario> listarTodos() {
            return usuarioRepository.findAll();
        }

        // Método responsável em CRIAR o SERVIÇO no banco de dados

            public Usuario save(Usuario usuario) {
                usuario.setCodStatus(true);
                return usuarioRepository.save(usuario);
            }

            // listar por id

            public Usuario findById(Long id) {
                return usuarioRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id " + id));
            }

            @Transactional
            public Usuario salvarUsuario(Usuario usuario) {
                return usuarioRepository.save(usuario);
            }

            // atualizar usuario
            @Transactional
            public Usuario update(Long id, Usuario usuario) {
               Usuario usuarioExistente = findById(id);
                usuarioExistente.setNome(usuario.getNome());
                usuarioExistente.setEmail(usuario.getEmail());
                usuarioExistente.setSenha(usuario.getSenha());
                usuarioExistente.setCodStatus(usuario.getCodStatus());

                return usuarioRepository.save(usuarioExistente);
            }

            @Transactional
            public void deletar(Long id) {
                Usuario usuarioExistente = findById(id);
                usuarioRepository.delete(usuarioExistente);
            }

            public Usuario validarLogin(String email, String senha) {
                Usuario usuario = usuarioRepository.findByEmail(email);
                if (usuario != null && usuario.getSenha().equals(senha) && usuario.getCodStatus()) {
                    return usuario;
                }
                return null;
            }
        }





