package com.timeright.tcc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.timeright.tcc.model.entity.Salao;
import com.timeright.tcc.model.repository.SalaoRepository;

import jakarta.transaction.Transactional;

@Service
@SuppressWarnings("null")
public class SalaoService {

    private final SalaoRepository salaoRepository;

    public SalaoService(SalaoRepository salaoRepository) {
        this.salaoRepository = salaoRepository;
    }

    public List<Salao> listarTodos() {
        return salaoRepository.findAll();
    }

    public Salao findById(Long id) {
        return salaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salão não encontrado com id " + id));
    }

    @Transactional
    public Salao salvar(Salao salao) {
        salao.setStatus("ATIVO");
        return salaoRepository.save(salao);
    }

    @Transactional
    public Salao atualizar(Long id, Salao salao) {
        Salao existente = findById(id);

        if (salao.getNome() != null && !salao.getNome().isBlank())
            existente.setNome(salao.getNome());
        if (salao.getCnpj() != null && !salao.getCnpj().isBlank())
            existente.setCnpj(salao.getCnpj());
        if (salao.getTelefone() != null && !salao.getTelefone().isBlank())
            existente.setTelefone(salao.getTelefone());
        if (salao.getEmail() != null && !salao.getEmail().isBlank())
            existente.setEmail(salao.getEmail());
        if (salao.getEndereco() != null && !salao.getEndereco().isBlank())
            existente.setEndereco(salao.getEndereco());

        return salaoRepository.save(existente);
    }

    @Transactional
    public Salao atualizarStatus(Long id, String novoStatus) {
        Salao existente = findById(id);
        existente.setStatus(novoStatus);
        return salaoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        salaoRepository.delete(findById(id));
    }
}
