package com.timeright.tcc.controller;

import com.timeright.tcc.model.dto.LoginRequest;
import com.timeright.tcc.model.entity.Usuario;
import com.timeright.tcc.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
        @RequestMapping("/api/usuario")
        @CrossOrigin(origins = {"http://localhost:8080"})
        public class UsuarioController {

            private final UsuarioService usuarioService;

            public UsuarioController(UsuarioService usuarioService){ this.usuarioService = usuarioService; }

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll(){

        List<Usuario> usuarios = usuarioService.listarTodos();

        return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);

    }

            @GetMapping("/{id}")
            public ResponseEntity<Object> findById(@PathVariable String id) {
                try{
                    return ResponseEntity.ok(usuarioService.findById(Long.valueOf(Long.parseLong(id))));
                } catch (NumberFormatException e ) {
                    return ResponseEntity.badRequest().body (
                            Map.of(
                                    "status", 400,
                                    "error", "Bad Request",
                                    "message", "O id informado não é válido: " +id
                            )
                    );
                } catch (RuntimeException e ) {
                    return ResponseEntity.status(404).body(
                            Map.of(
                                    "status", 404,
                                    "error", "Not Found",
                                    "message", "Produto não encontrado com o id: " + id
                            )
                    );
                }


            }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
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



    @PutMapping("/{id}")
            public ResponseEntity<Object> atualizar(@PathVariable String id, @RequestBody Usuario usuario) {
                try {
                    return ResponseEntity.ok(usuarioService.update(Long.valueOf(Long.parseLong(id)), usuario));
                } catch (NumberFormatException e ) {
                    return ResponseEntity.badRequest().body (
                            Map.of(
                                    "status", 400,
                                    "error", "Bad Request",
                                    "message", "O id informado não é válido: " +id
                            )
                    );
                } catch (RuntimeException e ) {
                    return ResponseEntity.status(404).body (
                            Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "Usuário não encontrado com o id: " +id
                    )
                    );
                }
                }

            @DeleteMapping("/{id}")
            public ResponseEntity<Object> deletar(@PathVariable String id) {
                try {
                usuarioService.deletar(Long.valueOf(Long.parseLong(id)));
                return ResponseEntity.ok().body(
                        Map.of(
                                "status", 200,
                                "message",
                                "Usuário deletado com sucesso!")
                );
            } catch (NumberFormatException e ) {
                return ResponseEntity.badRequest().body (
                        Map.of(
                                "status", 400,
                                "error", "Bad Request",
                                "message", "O id informado não é válido: " +id
                        )
                );
            } catch (RuntimeException e ) {
                return ResponseEntity.status(404).body (
                        Map.of(
                                "status", 404,
                                "error", "Not Found",
                                "message", "Usuário não encontrado com o id " +id
                        )
                );
            }
        }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioService.validarLogin(loginRequest.getEmail(), loginRequest.getSenha());
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("status", 401, "error", "Unauthorized", "message", "Email ou senha inválidos")
        );
    }
}



