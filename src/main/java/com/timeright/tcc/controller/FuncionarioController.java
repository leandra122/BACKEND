package com.timeright.tcc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.timeright.tcc.model.entity.Funcionario;
import com.timeright.tcc.services.FuncionarioService;

@RestController
@RequestMapping("/api/funcionario")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    
    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos() {
        return ResponseEntity.ok(funcionarioService.listarTodos());
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) {
        Funcionario funcionario = funcionarioService.findById(id);
        return ResponseEntity.ok(funcionario);
    }

  
    @PostMapping
    public ResponseEntity<Funcionario> salvar(@RequestBody Funcionario funcionario) {
        Funcionario novo = funcionarioService.salvar(funcionario);
        return ResponseEntity.ok(novo);
    }

   
    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(
            @PathVariable Long id,
            @RequestBody Funcionario funcionario) {

        Funcionario atualizado = funcionarioService.atualizar(id, funcionario);
        return ResponseEntity.ok(atualizado);
    }

  
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/login")
    public ResponseEntity<Funcionario> login(@RequestParam String email,
                                             @RequestParam String senha) {

        Funcionario funcionario = funcionarioService.validarLogin(email, senha);

        if (funcionario != null) {
            return ResponseEntity.ok(funcionario);
        }

        return ResponseEntity.status(401).build();
    }
}