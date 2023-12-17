package com.example.pruebasjavafx;

import com.example.pruebasjavafx.models.Persona;

import java.util.ArrayList;

public class Sesion {
    private static Persona persona = null;

    private static Integer pos = null;

    private static ArrayList<Persona> personas = new ArrayList<>(0);

    public static Persona getPersona() {
        return persona;
    }

    public static void setPersona(Persona persona) {
        Sesion.persona = persona;
    }

    public static Integer getPos() {
        return pos;
    }

    public static void setPos(Integer pos) {
        Sesion.pos = pos;
    }

    public static ArrayList<Persona> getPersonas() {
        return personas;
    }

    public static void setPersonas(ArrayList<Persona> personas) {
        Sesion.personas = personas;
    }
}
