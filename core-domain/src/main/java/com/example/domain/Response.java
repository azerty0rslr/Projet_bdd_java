package com.example.domain;

// Pour afficher les messages de classe métier en respectant le format du TP
public class Response {
    // Code métier
    private int code;
    // Message qui décrit le résultat
    private String message;
    private Object data;

    public Response(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Affiche le code en retour
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public Object getData() {
        return data;
    }
}