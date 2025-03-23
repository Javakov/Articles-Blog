package org.articlesblog.dto;

import lombok.Builder;

@Builder
public record ErrorDto(
        String error,
        String errorDescription
) { }
