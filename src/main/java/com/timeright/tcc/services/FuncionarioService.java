package com.timeright.tcc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timeright.tcc.model.entity.Funcionario;
import com.timeright.tcc.model.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    // 🔹 LISTAR TODOS
    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    // 🔹 BUSCAR POR ID
    public Funcionario findById(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com id " + id));
    }

    // 🔹 SALVAR
    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        funcionario.setCodStatus("ATIVO"); // ✔️ agora correto
        return funcionarioRepository.save(funcionario);
    }

    // 🔹 ATUALIZAR
    @Transactional
    public Funcionario atualizar(Long id, Funcionario funcionario) {
        Funcionario existente = findById(id);

        existente.setNome(funcionario.getNome());
        existente.setEmail(funcionario.getEmail());
        existente.setSenha(funcionario.getSenha());
        existente.setServico(funcionario.getServico());
        existente.setObservacoes(funcionario.getObservacoes());
        existente.setCodStatus(funcionario.getCodStatus());

        return funcionarioRepository.save(existente);
    }

    // 🔹 DELETAR
    @Transactional
    public void deletar(Long id) {
        Funcionario funcionario = findById(id);
        funcionarioRepository.delete(funcionario);
    }

    // 🔹 LOGIN
    public Funcionario validarLogin(String email, String senha) {
        Funcionario funcionario = funcionarioRepository.findByEmail(email);

        if (funcionario != null &&
            funcionario.getSenha().equals(senha) &&
            "ATIVO".equals(funcionario.getCodStatus())) {

            return funcionario;
        }

        return null;
    }
}