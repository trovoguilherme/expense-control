package br.com.iug.service;

import br.com.iug.dto.request.FaturaCartaoUpdate;
import br.com.iug.entity.FaturaCartao;
import br.com.iug.exception.NotFoundException;
import br.com.iug.repository.FaturaCartaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FaturaCartaoService {

    private final FaturaCartaoRepository faturaCartaoRepository;

    private final PagamentoService pagamentoService;

    public FaturaCartao save(FaturaCartao faturaCartao) throws NotFoundException {
        var pagamento = pagamentoService.findByNome(faturaCartao.getPagamento().getNome());
        faturaCartao.setPagamento(pagamento);

        return faturaCartaoRepository.save(faturaCartao);
    }

    public List<FaturaCartao> findAll() {
        return faturaCartaoRepository.findAll();
    }

    public FaturaCartao findByName(String nome) {
        return faturaCartaoRepository.findByPagamentoNome(nome).orElseThrow(() -> new NotFoundException("Nome de pagamento não encontrado"));
    }

    public FaturaCartao patchValor(String nome, FaturaCartaoUpdate faturaCartaoUpdate) {
        var faturaCartao = findByName(nome);
        faturaCartao.setValor(faturaCartaoUpdate.getValor());

        return faturaCartaoRepository.save(faturaCartao);
    }

}
