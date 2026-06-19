package com.timeright.tcc.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.timeright.tcc.model.entity.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByUsuarioId(Long usuarioId);

    List<Agendamento> findByFuncionarioId(Long funcionarioId);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.funcionario.id = :funcionarioId AND a.dataHora = :dataHora AND a.status <> 'CANCELADO' AND (:id IS NULL OR a.id <> :id)")
    boolean existeConflito(@Param("funcionarioId") Long funcionarioId,
                           @Param("dataHora") LocalDateTime dataHora,
                           @Param("id") Long id);
}
