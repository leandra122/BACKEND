package com.timeright.tcc.model.repository;

import com.timeright.tcc.model.entity.NivelAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NivelAcessoRepository extends JpaRepository<NivelAcesso, Integer> {
    

}
