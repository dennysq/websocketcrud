/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamj.websocketcrud;

import java.util.Objects;

/**
 *
 * @author dennys
 */
public class Persona {

    private Integer personaId;
    private String nombre;
    private String email;

    public Integer getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Integer personaId) {
        this.personaId = personaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Persona(Integer personaId, String nombre, String email) {
        this.personaId = personaId;
        this.nombre = nombre;
        this.email = email;
    }

    public Persona(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public Persona() {
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.personaId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Persona other = (Persona) obj;
        if (!Objects.equals(this.personaId, other.personaId)) {
            return false;
        }
        return true;
    }

}
