package com.prprv.token.exception;

import com.prprv.token.common.R;
import org.springframework.http.ResponseEntity;

/**
 * @author Yoooum
 */
public class AppException extends RuntimeException {
    R<Void> response;

    public AppException(R<Void> of) {
        super(of.message());
        this.response = of;
    }

    public AppException(Exception ex) {
        super(ex.getMessage());
        this.response = R.fail(ex.getMessage());
    }

    public R<Void> getR() {
        return response;
    }
}
