package tip.java.sistemacentrocrecer.exceptions;

public class CedulaNotFoundException extends RuntimeException {
    public CedulaNotFoundException(String message) {
        super("No se encontró ningún registro con la cédula: " + message);
    }
}
