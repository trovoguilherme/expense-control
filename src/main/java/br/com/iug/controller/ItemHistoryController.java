package br.com.iug.controller;

import br.com.iug.entity.history.ItemHistory;
import br.com.iug.service.ItemHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item-history")
public class ItemHistoryController {

    private final ItemHistoryService itemHistoryService;

    @GetMapping
    public ResponseEntity<List<ItemHistory>> findHistory() {
        return ResponseEntity.ok(itemHistoryService.findAll());
    }

}
