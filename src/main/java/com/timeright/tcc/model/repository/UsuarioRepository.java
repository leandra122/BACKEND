package com.timeright.tcc.model.repository;

import com.timeright.tcc.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByUsername(String username);

    //Usuario findByTokenConfirmacao(String token);
}
