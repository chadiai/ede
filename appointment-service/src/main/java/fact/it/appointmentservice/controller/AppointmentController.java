package fact.it.productservice.controller;

import fact.it.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class AppointmentController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createProduct
            (@RequestBody fact.it.productservice.dto.AppointmentRequest appointmentRequest) {
        productService.createProduct(appointmentRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<fact.it.productservice.dto.AppointmentResponse> getAllProductsBySkuCode
            (@RequestParam List<String> skuCode) {
        return productService.getAllProductsBySkuCode(skuCode);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<fact.it.productservice.dto.AppointmentResponse> getAllProducts() {
        return productService.getAllProducts();
    }
}

