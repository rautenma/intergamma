package nl.intergamma.poc.controller;

import nl.intergamma.poc.Repositories.ProductRepository;
import nl.intergamma.poc.Repositories.ReservedProductsRepository;
import nl.intergamma.poc.dto.CreateProductObject;
import nl.intergamma.poc.entities.Product;
import nl.intergamma.poc.entities.ReservedProducts;
import nl.intergamma.poc.exception.CustomException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class StockController {

    private final ProductRepository productRepository;
    private final ReservedProductsRepository reservedProductsRepository;

    public StockController(ProductRepository productRepository, ReservedProductsRepository reservedProductsRepository) {
        this.productRepository = productRepository;
        this.reservedProductsRepository = reservedProductsRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findAll());
        CreateProductObject createProductObject = new CreateProductObject();
        model.addAttribute("createProductObject", createProductObject);
        return "home";
    }

    @PostMapping("/createProduct")
    public String createProduct(@ModelAttribute CreateProductObject createProductObject) {
        Product product = new Product();
        product.setAvailable(createProductObject.getAvailable());
        product.setCode(createProductObject.getProductCode());
        product.setName(createProductObject.getProductName());
        productRepository.save(product);
        return "redirect:/";
    }

    @PostMapping("delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
        }
        return "redirect:/";
    }

    @PostMapping("/increaseAvailableStock/{id}")
    public String increaseAvailableStock(@PathVariable("id") Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.incrementAvailableCount(product.get().getId());
        }
        return "redirect:/";
    }

    @PostMapping("/decreaseAvailableStock/{id}")
    public String decreaseAvailableStock(@PathVariable("id") Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            if (product.get().getAvailable() < 1) {
                throw new CustomException("No More products available");
            }
            productRepository.decrementAvailableCount(product.get().getId());
        }
        return "redirect:/";
    }

    @PostMapping("/reserveStock/{id}")
    public String reserveStock(@PathVariable("id") Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            if (product.get().getAvailable() < 1) {
                throw new CustomException("No More products available to reserve");
            }
            ReservedProducts reservedProducts = new ReservedProducts();
            reservedProducts.setProduct(product.get());
            reservedProducts.setReservedTime(new Timestamp(new Date().getTime()));
            reservedProductsRepository.save(reservedProducts);
            productRepository.incrementReservedCount(product.get().getId());
            productRepository.decrementAvailableCount(product.get().getId());
        }
        return "redirect:/";
    }

    @PostMapping("/removeProduct/{id}")
    public String removeProduct(@PathVariable("id") Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            List<ReservedProducts> reservedProductsList = reservedProductsRepository.findAllByProduct(product.get());
            if (reservedProductsList.size() != 0) {
                reservedProductsRepository.deleteAll(reservedProductsList);
            }
            productRepository.delete(product.get());
        }
        return "redirect:/";
    }

}
