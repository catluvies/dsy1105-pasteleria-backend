package pasteleria.com.pasteleria.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000) 
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "image_res_id")
    private String imageUrl;

}