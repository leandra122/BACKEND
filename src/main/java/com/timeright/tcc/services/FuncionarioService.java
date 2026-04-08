package com.timeright.tcc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timeright.tcc.model.entity.Funcionario;
import com.timeright.tcc.model.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🔹 LISTAR TODOS
    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    // 🔹 BUSCAR POR ID
    @SuppressWarnings("null")
    public Funcionario findById(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionario não encontrado com id " + id));
    }

    // 🔹 SALVAR
    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        funcionario.setCodStatus("ATIVO");

        // 🔐 criptografar senha
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));

        return funcionarioRepository.save(funcionario);
    }

    // 🔹 ATUALIZAR
    @SuppressWarnings("null")
    @Transactional
    public Funcionario atualizar(Long id, Funcionario funcionario) {
        Funcionario existente = findById(id);

        existente.setNome(funcionario.getNome());
        existente.setEmail(funcionario.getEmail());

        // 🔐 só criptografa se vier nova senha
        if (funcionario.getSenha() != null && !funcionario.getSenha().isEmpty()) {
            existente.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        }

        existente.setObservacoes(funcionario.getObservacoes());
        existente.setCodStatus(funcionario.getCodStatus());
        existente.setServico(funcionario.getServico());

        return funcionarioRepository.save(existente);
    }

    // 🔹 DELETAR
    @SuppressWarnings("null")
    @Transactional
    public void deletar(Long id) {
        Funcionario funcionario = findById(id);
        funcionarioRepository.delete(funcionario);
    }

    // 🔹 LOGIN
    public Funcionario validarLogin(String email, String senha) {
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findByEmail(email);

        if (funcionarioOpt.isPresent()) {
            Funcionario funcionario = funcionarioOpt.get();

            if (passwordEncoder.matches(senha, funcionario.getSenha()) &&
                "ATIVO".equals(funcionario.getCodStatus())) {

                return funcionario;
            }
        }

        return null;
    }
}