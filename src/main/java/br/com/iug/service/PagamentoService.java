package br.com.iug.service;

import br.com.iug.entity.Pagamento;
import br.com.iug.repository.PagamentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public Pagamento save(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    public boolean existsByNome(String nome) {
        return pagamentoRepository.existsByNome(nome);
    }

    public List<Pagamento> findAll() {
        return pagamentoRepository.findAll();
    }

}
