package com.timeright.tcc.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timeright.tcc.model.entity.Agendamento;
import com.timeright.tcc.model.entity.Funcionario;
import com.timeright.tcc.model.entity.Servico;
import com.timeright.tcc.model.entity.Usuario;
import com.timeright.tcc.model.repository.AgendamentoRepository;
import com.timeright.tcc.model.repository.FuncionarioRepository;
import com.timeright.tcc.model.repository.ServicoRepository;
import com.timeright.tcc.model.repository.UsuarioRepository;

@Service
@SuppressWarnings("null")
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ServicoRepository servicoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              UsuarioRepository usuarioRepository,
                              FuncionarioRepository funcionarioRepository,
                              ServicoRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.servicoRepository = servicoRepository;
    }

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento findById(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com id " + id));
    }

    @Transactional
    public Agendamento salvar(Agendamento agendamento) {
        validarAntecedencia(agendamento.getDataHora());

        Usuario usuario = usuarioRepository.findById(agendamento.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Funcionario funcionario = funcionarioRepository.findById(agendamento.getFuncionario().getId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Servico servico = servicoRepository.findById(agendamento.getServico().getId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (agendamentoRepository.existeConflito(funcionario.getId(), agendamento.getDataHora(), null)) {
            throw new RuntimeException("Funcionário já possui agendamento neste horário");
        }

        Agendamento novo = new Agendamento();
        novo.setDataHora(agendamento.getDataHora());
        novo.setObservacoes(agendamento.getObservacoes());
        novo.setStatus("AGENDADO");
        novo.setUsuario(usuario);
        novo.setFuncionario(funcionario);
        novo.setServico(servico);

        return agendamentoRepository.save(novo);
    }

    @Transactional
    public Agendamento atualizar(Long id, Agendamento agendamento) {
        Agendamento existente = findById(id);

        if (agendamento.getDataHora() != null) {
            validarAntecedencia(agendamento.getDataHora());
            if (agendamentoRepository.existeConflito(existente.getFuncionario().getId(), agendamento.getDataHora(), id)) {
                throw new RuntimeException("Funcionário já possui agendamento neste horário");
            }
            existente.setDataHora(agendamento.getDataHora());
        }

        if (agendamento.getObservacoes() != null && !agendamento.getObservacoes().isBlank())
            existente.setObservacoes(agendamento.getObservacoes());

        if (agendamento.getFuncionario() != null && agendamento.getFuncionario().getId() != null) {
            Funcionario funcionario = funcionarioRepository.findById(agendamento.getFuncionario().getId())
                    .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
            existente.setFuncionario(funcionario);
        }

        if (agendamento.getServico() != null && agendamento.getServico().getId() != null) {
            Servico servico = servicoRepository.findById(agendamento.getServico().getId())
                    .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
            existente.setServico(servico);
        }

        return agendamentoRepository.save(existente);
    }

    @Transactional
    public Agendamento cancelar(Long id) {
        Agendamento existente = findById(id);
        existente.setStatus("CANCELADO");
        return agendamentoRepository.save(existente);
    }

    @Transactional
    public Agendamento atualizarStatus(Long id, String novoStatus) {
        Agendamento existente = findById(id);
        existente.setStatus(novoStatus);
        return agendamentoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        agendamentoRepository.delete(findById(id));
    }

    private void validarAntecedencia(LocalDateTime dataHora) {
        if (dataHora.isBefore(LocalDateTime.now().plusHours(12))) {
            throw new RuntimeException("O agendamento deve ser feito com no mínimo 12 horas de antecedência");
        }
    }
}
