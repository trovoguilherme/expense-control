package br.com.iug.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelaResponse {

    private long id;

    private int qtdPaga;

    private int qtdRestante;

}
