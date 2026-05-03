package tip.java.sistemacentrocrecer.exceptions;

//Problemas de existencia en la base de datos
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Integer id) {
        super(resource + " con id " + id + " no encontrado");
    }
}
