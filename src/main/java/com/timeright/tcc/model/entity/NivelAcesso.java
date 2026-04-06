package com.timeright.tcc.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "NivelAcesso")
public class NivelAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nomeNivelAcesso", nullable = false, length = 50)
    private String nomeNivelAcesso;

    @Column(name = "statusNivelAcesso", nullable = false, length = 50)
    private String statusNivelAcesso;

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeNivelAcesso() {
        return nomeNivelAcesso;
    }

    public void setNomeNivelAcesso(String nomeNivelAcesso) {
        this.nomeNivelAcesso = nomeNivelAcesso;
    }

    public String getStatusNivelAcesso() {
        return statusNivelAcesso;
    }

    public void setStatusNivelAcesso(String statusNivelAcesso) {
        this.statusNivelAcesso = statusNivelAcesso;
    }
   
    

}