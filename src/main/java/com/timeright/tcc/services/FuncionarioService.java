package com.timeright.tcc.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timeright.tcc.model.entity.Funcionario;
import com.timeright.tcc.model.entity.Salao;
import com.timeright.tcc.model.repository.FuncionarioRepository;
import com.timeright.tcc.model.repository.SalaoRepository;

@Service
public class FuncionarioService {

    private static final String STATUS_ATIVO = "ATIVO";

    private final FuncionarioRepository funcionarioRepository;
    private final SalaoRepository salaoRepository;

    public FuncionarioService(
            FuncionarioRepository funcionarioRepository,
            SalaoRepository salaoRepository) {

        this.funcionarioRepository = funcionarioRepository;
        this.salaoRepository = salaoRepository;
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public Funcionario findById(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Funcionário não encontrado com id " + id));
    }

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {

        if (funcionario.getSalao() == null ||
                funcionario.getSalao().getId() == null) {
            throw new RuntimeException("Salão é obrigatório");
        }

        Salao salao = salaoRepository.findById(funcionario.getSalao().getId())
                .orElseThrow(() ->
                        new RuntimeException("Salão não encontrado"));

        Funcionario novo = new Funcionario();
        novo.setNome(funcionario.getNome());
        novo.setTelefone(funcionario.getTelefone());
        novo.setEmail(funcionario.getEmail());
        novo.setEspecialidade(funcionario.getEspecialidade());
        novo.setStatus(STATUS_ATIVO);
        novo.setSalao(salao);

        return funcionarioRepository.save(novo);
    }

    @Transactional
    public Funcionario atualizar(Long id, Funcionario funcionario) {

        Funcionario existente = findById(id);

        if (funcionario.getNome() != null &&
                !funcionario.getNome().isBlank()) {
            existente.setNome(funcionario.getNome());
        }

        if (funcionario.getTelefone() != null &&
                !funcionario.getTelefone().isBlank()) {
            existente.setTelefone(funcionario.getTelefone());
        }

        if (funcionario.getEmail() != null &&
                !funcionario.getEmail().isBlank()) {
            existente.setEmail(funcionario.getEmail());
        }

        if (funcionario.getEspecialidade() != null &&
                !funcionario.getEspecialidade().isBlank()) {
            existente.setEspecialidade(funcionario.getEspecialidade());
        }

        if (funcionario.getSalao() != null &&
                funcionario.getSalao().getId() != null) {

            Salao salao = salaoRepository.findById(funcionario.getSalao().getId())
                    .orElseThrow(() ->
                            new RuntimeException("Salão não encontrado"));

            existente.setSalao(salao);
        }

        return funcionarioRepository.save(existente);
    }

    @Transactional
    public Funcionario atualizarStatus(Long id, String novoStatus) {

        Funcionario existente = findById(id);
        existente.setStatus(novoStatus);

        return funcionarioRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {

        Funcionario existente = findById(id);
        funcionarioRepository.delete(existente);
    }
}