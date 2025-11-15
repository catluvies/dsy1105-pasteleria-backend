package pasteleria.com.pasteleria.service;

import pasteleria.com.pasteleria.model.Product;
import pasteleria.com.pasteleria.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElseThrow();
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Integer id, Product updatedProduct) {
        Product existing = getProductById(id);

        if (updatedProduct.getName() != null) {
            existing.setName(updatedProduct.getName());
        }
        if (updatedProduct.getDescription() != null) {
            existing.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getPrice() != null) {
            existing.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getImageResId() != null) {
            existing.setImageResId(updatedProduct.getImageResId());
        }

        return productRepository.save(existing);
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
