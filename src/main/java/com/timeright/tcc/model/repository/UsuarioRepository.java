package com.timeright.tcc.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timeright.tcc.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
}
