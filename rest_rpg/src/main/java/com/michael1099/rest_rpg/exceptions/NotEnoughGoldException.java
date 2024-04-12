package com.michael1099.rest_rpg.exceptions;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Tydzień 9 - wyjątki
// Stworzone zostały wyjątki w celu wyrzucania kodów błędu dla FE.
// Przykładowo zamiast błędu 500 z bazy danych, że wartość nie może być ujemna,
// wyrzucony zostanie błąd 403 i odpowiedni komunikat z enum ErrorCodes
public class NotEnoughGoldException extends ResponseStatusException {

    public NotEnoughGoldException() {
        super(HttpStatus.FORBIDDEN, ErrorCodes.NOT_ENOUGH_GOLD.toString());
    }
}
// Koniec Tydzień 9 - wyjątki
