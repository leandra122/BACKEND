package com.timeright.tcc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.timeright.tcc.model.entity.Salao;
import com.timeright.tcc.services.SalaoService;

@RestController
@RequestMapping("/saloes")
public class SalaoController {

    private final SalaoService salaoService;

    public SalaoController(SalaoService salaoService) {
        this.salaoService = salaoService;
    }

    // 🔹 LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Salao>> findAll() {
        return ResponseEntity.ok(salaoService.listarTodos());
    }

    // 🔹 BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            return ResponseEntity.ok(salaoService.findById(idLong));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "error", "Bad Request", "message", "O id informado não é válido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "error", "Not Found", "message", "Salão não encontrado com o id: " + id));
        }
    }

    // 🔹 SALVAR
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Salao salao) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(salaoService.salvar(salao));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("status", 500, "error", "Internal Server Error", "message", "Erro ao salvar salão: " + e.getMessage()));
        }
    }

    // 🔹 ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable String id, @RequestBody Salao salao) {
        try {
            Long idLong = Long.parseLong(id);
            return ResponseEntity.ok(salaoService.atualizar(idLong, salao));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "error", "Bad Request", "message", "O id informado não é válido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "error", "Not Found", "message", "Salão não encontrado com o id: " + id));
        }
    }

    // 🔹 ATUALIZAR STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<Object> atualizarStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            Long idLong = Long.parseLong(id);
            String novoStatus = body.get("status");
            if (novoStatus == null || (!novoStatus.equals("ATIVO") && !novoStatus.equals("INATIVO"))) {
                return ResponseEntity.badRequest().body(Map.of("message", "Status inválido. Use ATIVO ou INATIVO."));
            }
            return ResponseEntity.ok(salaoService.atualizarStatus(idLong, novoStatus));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Id inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("message", "Salão não encontrado com id: " + id));
        }
    }

    // 🔹 DELETAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            salaoService.deletar(idLong);
            return ResponseEntity.ok(Map.of("status", 200, "message", "Salão deletado com sucesso!"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "error", "Bad Request", "message", "O id informado não é válido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "error", "Not Found", "message", "Salão não encontrado com o id " + id));
        }
    }
}
