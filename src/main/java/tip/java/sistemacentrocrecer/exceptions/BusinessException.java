package tip.java.sistemacentrocrecer.exceptions;

//Para errores de logioca de negocios
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
