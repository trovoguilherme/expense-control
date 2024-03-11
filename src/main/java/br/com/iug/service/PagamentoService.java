package br.com.iug.service;

import br.com.iug.entity.Pagamento;
import br.com.iug.exception.NotFoundException;
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

    public List<Pagamento> findAll() {
        return pagamentoRepository.findAll();
    }

    public Pagamento findByNome(String nome) throws NotFoundException {
        return pagamentoRepository.findByNome(nome).orElseThrow(() -> new NotFoundException("Nome de pagamento não encontrado"));
    }

    public void deleteByName(String nome) {
        var pagamento = findByNome(nome);
        pagamentoRepository.delete(pagamento);
    }

}
