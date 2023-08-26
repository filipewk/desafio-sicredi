package br.com.sicredi.coopvote.record;

public record ApiResult<T>(int status, String message, T content) {}
