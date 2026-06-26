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
    private static final String STATUS_INATIVO = "INATIVO";

    private final FuncionarioRepository funcionarioRepository;
    private final SalaoRepository salaoRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository,
                              SalaoRepository salaoRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.salaoRepository = salaoRepository;
    }

    // LISTAR
    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    // BUSCAR
    public Funcionario buscarPorId(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Funcionário não encontrado com id " + id));
    }

    // SALVAR
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

    // ATUALIZAR
    @Transactional
    public Funcionario atualizar(Long id, Funcionario funcionario) {

        Funcionario existente = buscarPorId(id);

        if (funcionario.getNome() != null && !funcionario.getNome().isBlank()) {
            existente.setNome(funcionario.getNome());
        }

        if (funcionario.getTelefone() != null && !funcionario.getTelefone().isBlank()) {
            existente.setTelefone(funcionario.getTelefone());
        }

        if (funcionario.getEmail() != null && !funcionario.getEmail().isBlank()) {
            existente.setEmail(funcionario.getEmail());
        }

        if (funcionario.getEspecialidade() != null && !funcionario.getEspecialidade().isBlank()) {
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

    // STATUS
    @Transactional
    public Funcionario atualizarStatus(Long id, String status) {

        Funcionario existente = buscarPorId(id);
        existente.setStatus(status);

        return funcionarioRepository.save(existente);
    }

    // DELETE
    @Transactional
    public void deletar(Long id) {

        Funcionario existente = buscarPorId(id);
        funcionarioRepository.delete(existente);
    }
}