package ru.don_polesie.back_end.exceptions.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.don_polesie.back_end.exceptions.*;

@ControllerAdvice
public class ExceptionHandlerService {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Information> handleObjectNotFoundException(ObjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new Information("Объект не найден.", ex.getMessage())
        );
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<Information> handleIllegalArgumentException(RequestValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new Information("Некорректный запрос.", ex.getMessage())
        );
    }

    @ExceptionHandler(ConflictDataException.class)
    public ResponseEntity<Information> handleConflictDataException(ConflictDataException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new Information("Конфликт в дате.", ex.getMessage())
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleConflictDataException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Information> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new Information("У вас недостаточно прав для этого действия.", ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Information> handleException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new Information("Возникла ошибка.", ex.getMessage())
        );
    }
}
