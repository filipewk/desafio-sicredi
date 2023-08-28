package br.com.sicredi.coopvote.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record ApiResult<T>(boolean error, int status, String message, T content) {}
