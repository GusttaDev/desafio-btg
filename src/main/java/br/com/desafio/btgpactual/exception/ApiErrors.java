package br.com.desafio.btgpactual.exception;

import java.util.Collections;
import java.util.List;


public class ApiErrors {

    List<String> errorsApi;

    public ApiErrors(String message) {
        this.errorsApi = Collections.singletonList(message);
    }

    public ApiErrors(List<String> errorsApi) {
        this.errorsApi = errorsApi;
    }

    public List<String> getErrorsApi() {
        return errorsApi;
    }
}
