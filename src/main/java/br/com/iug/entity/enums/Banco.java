package br.com.iug.entity.enums;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum Banco {

    NUBANK(1),
    ITAU(2),
    SANTANDER(3),
    BRADESCO(4),
    INTER(5),
    C6(6),
    OUTROS(7),
    NENHUM(8);

    private final Integer id;

    public static final Map<Integer, Banco> BANCO_ID_MAPPING = new HashMap<>();

    static {
        Arrays.stream(Banco.values()).forEach(banco -> BANCO_ID_MAPPING.put(banco.id, banco));
    }

}
