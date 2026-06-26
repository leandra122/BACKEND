package com.timeright.tcc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.timeright.tcc.model.entity.Funcionario;
import com.timeright.tcc.services.FuncionarioService;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    // LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Funcionario>> findAll() {
        return ResponseEntity.ok(funcionarioService.listarTodos());
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            return ResponseEntity.ok(funcionarioService.buscarPorId(idLong));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", 400,
                           "error", "Bad Request",
                           "message", "Id inválido: " + id)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of("status", 404,
                           "error", "Not Found",
                           "message", e.getMessage())
            );
        }
    }

    // SALVAR
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Funcionario funcionario) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(funcionarioService.salvar(funcionario));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("status", 500,
                           "error", "Internal Server Error",
                           "message", e.getMessage())
            );
        }
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable String id,
                                            @RequestBody Funcionario funcionario) {
        try {
            Long idLong = Long.parseLong(id);
            return ResponseEntity.ok(funcionarioService.atualizar(idLong, funcionario));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Id inválido: " + id)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of("message", e.getMessage())
            );
        }
    }

    // STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<Object> atualizarStatus(@PathVariable String id,
                                                  @RequestBody Map<String, String> body) {
        try {
            Long idLong = Long.parseLong(id);
            String novoStatus = body.get("status");

            if (novoStatus == null ||
                (!novoStatus.equals("ATIVO") && !novoStatus.equals("INATIVO"))) {
                return ResponseEntity.badRequest().body(
                        Map.of("message", "Status inválido. Use ATIVO ou INATIVO.")
                );
            }

            return ResponseEntity.ok(
                    funcionarioService.atualizarStatus(idLong, novoStatus)
            );

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Id inválido: " + id)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of("message", e.getMessage())
            );
        }
    }

    // DELETAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            funcionarioService.deletar(idLong);

            return ResponseEntity.ok(
                    Map.of("status", 200,
                           "message", "Funcionário deletado com sucesso!")
            );

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Id inválido: " + id)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of("message", e.getMessage())
            );
        }
    }
}