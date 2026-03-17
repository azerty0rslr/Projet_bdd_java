package com.example.domain;

// Classe générique de retour structuré pour tous les endpoints
// Respecte le format imposé par le TP : Code | Message | Data
public class Response {

    private int code;       // Code métier (ex: 2002 = succès, 7001 = non trouvé)
    private String message; // Message lisible décrivant le résultat
    private Object data;    // Données retournées, peut être null

    public Response(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode()        { return code; }
    public String getMessage()  { return message; }
    public Object getData()     { return data; }
}