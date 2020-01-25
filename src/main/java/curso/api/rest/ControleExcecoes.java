package curso.api.rest;

import java.sql.SQLException;

import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {
	
	//tratamento da maioria dos erros
	@Override
	@ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		ObjetoError objetoError = new ObjetoError();
		objetoError.setError(ex.getMessage());
		//codigo mais a descricao
		objetoError.setCode(status.value() + " ==> " + status.getReasonPhrase()); //200, 400, 500
		
		return new ResponseEntity<>(objetoError, headers, status);
	}
	
	
	//tratamento da maioria dos erros a nível de banco de dados
	@ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, PSQLException.class, SQLException.class})
	protected ResponseEntity<Object> handleExceptioDataIntegry(Exception ex) {
		
		String msg = "";
		
		if (ex instanceof DataIntegrityViolationException) {
			msg = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
		}		
		else if (ex instanceof ConstraintViolationException) {
			msg = ((ConstraintViolationException) ex).getCause().getCause().getMessage();
		}		
		else if (ex instanceof PSQLException) {
			msg = ((PSQLException) ex).getCause().getCause().getMessage();
		}		
		else if (ex instanceof SQLException) {
			msg = ((SQLException) ex).getCause().getCause().getMessage();
		}
		else {
			msg = ex.getMessage();
		}
		
		ObjetoError objetoError = new ObjetoError();
		objetoError.setError(msg);
		//codigo mais a descricao
		objetoError.setCode(HttpStatus.INTERNAL_SERVER_ERROR + " ==> " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()); //200, 400, 500
		
		return new ResponseEntity<>(objetoError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
