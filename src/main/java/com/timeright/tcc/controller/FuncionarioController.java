package com.timeright.tcc.controller;

import com.timeright.tcc.model.dto.LoginRequest;
import com.timeright.tcc.model.entity.Funcionario;
import com.timeright.tcc.services.FuncionarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/funcionario")
@CrossOrigin(origins = {"http://localhost:5173"})
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    // 🔹 LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Funcionario>> findAll() {
        return ResponseEntity.ok(funcionarioService.listarTodos());
    }

    // 🔹 BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(funcionarioService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "Funcionário não encontrado com o id: " + id
                    )
            );
        }
    }

    // 🔹 SALVAR
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Funcionario funcionario) {
        try {
            Funcionario novo = funcionarioService.salvar(funcionario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status", 500,
                            "error", "Internal Server Error",
                            "message", "Erro ao salvar funcionário: " + e.getMessage()
                    )
            );
        }
    }

    // 🔹 ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody Funcionario funcionario) {
        try {
            return ResponseEntity.ok(funcionarioService.atualizar(id, funcionario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "Funcionário não encontrado com o id: " + id
                    )
            );
        }
    }

    // 🔹 DELETAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable Long id) {
        try {
            funcionarioService.deletar(id);
            return ResponseEntity.ok(
                    Map.of(
                            "status", 200,
                            "message", "Funcionário deletado com sucesso!"
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "Funcionário não encontrado com o id: " + id
                    )
            );
        }
    }

    // 🔹 LOGIN
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {

        Funcionario funcionario = funcionarioService.validarLogin(
                loginRequest.getEmail(),
                loginRequest.getSenha()
        );

        if (funcionario != null) {
            return ResponseEntity.ok(funcionario);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of(
                        "status", 401,
                        "error", "Unauthorized",
                        "message", "Email ou senha inválidos"
                )
        );
    }
}