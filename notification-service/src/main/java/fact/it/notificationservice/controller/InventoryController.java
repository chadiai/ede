package fact.it.notificationservice.controller;

import fact.it.notificationservice.dto.InventoryResponse;
import fact.it.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final NotificationService notificationService;

    // http://localhost:8082/api/inventory?skuCode=tube6in&skuCode=beam10ft
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock
    (@RequestParam List<String> skuCode) {
        return notificationService.isInStock(skuCode);
    }
}