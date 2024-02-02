package com.niit.REST_API_MONGODB_MC_1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT , reason = "Track Already Exists")
public class TrackAlreadyExistException extends Exception {
}
