package com.prosigmaka.catra.diglett.assembler;

public interface InterfaceAssembler <A, B> {

    A fromDto(B dto);

    B fromEntity(A entity);
}
