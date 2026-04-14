package com.timeright.tcc.controller;

import com.timeright.tcc.model.dto.LoginRequest;
import com.timeright.tcc.model.entity.Usuario;
import com.timeright.tcc.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 🔹 LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // 🔹 BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            return ResponseEntity.ok(usuarioService.findById(idLong));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "O id informado não é válido: " + id
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "Usuário não encontrado com o id: " + id
                    )
            );
        }
    }

    // 🔹 SALVAR
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Usuario usuario) {
        try {
            
            Usuario novoUsuario = usuarioService.salvar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status", 500,
                            "error", "Internal Server Error",
                            "message", "Erro ao salvar usuário: " + e.getMessage()
                    )
            );
        }
    }

    // 🔹 ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable String id, @RequestBody Usuario usuario) {
        try {
            Long idLong = Long.parseLong(id);
            return ResponseEntity.ok(usuarioService.atualizar(idLong, usuario));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "O id informado não é válido: " + id
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "Usuário não encontrado com o id: " + id
                    )
            );
        }
    }

    // 🔹 DELETAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable String id) {
        try {
            Long idLong = Long.parseLong(id);
            usuarioService.deletar(idLong);

            return ResponseEntity.ok(
                    Map.of(
                            "status", 200,
                            "message", "Usuário deletado com sucesso!"
                    )
            );

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "O id informado não é válido: " + id
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "Usuário não encontrado com o id " + id
                    )
            );
        }
    }

    // 🔹 LOGIN
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {

        Usuario usuario = usuarioService.validarLogin(
                loginRequest.getEmail(),
                loginRequest.getSenha()
        );

        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of(
                        "status", 401,
                        "error", "Unauthorized",
                        "message", "Username ou senha inválidos"
                )
        );
    }
}