package org.ubicuosoft;

import org.ubicuosoft.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public static final List<Examen> EXAMENES = Arrays.asList(
            new Examen(1L,"Matematicas"),
            new Examen(2L,"Lenguaje"),
            new Examen(3L,"Historia"));


    public static final List<String> PREGUNTAS = Arrays.asList(
            "Pregunta 1",
            "Pregunta 2",
            "Pregunta 3",
            "Pregunta 4",
            "Pregunta 5") ;

    public static final Examen EXAMEN = new Examen(4L,"Fisica");

    public static final List<Examen> EXAMENES_ID_NULL = Arrays.asList(
            new Examen(null,"Matematicas"),
            new Examen(null,"Lenguaje"),
            new Examen(null,"Historia"));

}

