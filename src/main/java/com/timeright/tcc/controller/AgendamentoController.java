package com.timeright.tcc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.timeright.tcc.model.entity.Agendamento;
import com.timeright.tcc.services.AgendamentoService;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    // 🔹 LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Agendamento>> findAll() {
        return ResponseEntity.ok(agendamentoService.listarTodos());
    }

    // 🔹 BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            return ResponseEntity.ok(agendamentoService.findById(idLong));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "error", "Bad Request", "message", "O id informado não é válido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "error", "Not Found", "message", "Agendamento não encontrado com o id: " + id));
        }
    }

    // 🔹 SALVAR
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Agendamento agendamento) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.salvar(agendamento));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", 400, "error", "Bad Request", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("status", 500, "error", "Internal Server Error", "message", "Erro ao salvar agendamento: " + e.getMessage()));
        }
    }

    // 🔹 ATUALIZAR (remarcar)
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable String id, @RequestBody Agendamento agendamento) {
        try {
            Long idLong = Long.parseLong(id);
            return ResponseEntity.ok(agendamentoService.atualizar(idLong, agendamento));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "error", "Bad Request", "message", "O id informado não é válido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("status", 400, "error", "Bad Request", "message", e.getMessage()));
        }
    }

    // 🔹 CANCELAR
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Object> cancelar(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            return ResponseEntity.ok(agendamentoService.cancelar(idLong));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Id inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("message", "Agendamento não encontrado com id: " + id));
        }
    }

    // 🔹 ATUALIZAR STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<Object> atualizarStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            Long idLong = Long.parseLong(id);
            String novoStatus = body.get("status");
            if (novoStatus == null || novoStatus.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Status não informado."));
            }
            return ResponseEntity.ok(agendamentoService.atualizarStatus(idLong, novoStatus));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Id inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("message", "Agendamento não encontrado com id: " + id));
        }
    }

    // 🔹 DELETAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            agendamentoService.deletar(idLong);
            return ResponseEntity.ok(Map.of("status", 200, "message", "Agendamento deletado com sucesso!"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "error", "Bad Request", "message", "O id informado não é válido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "error", "Not Found", "message", "Agendamento não encontrado com o id " + id));
        }
    }
}
