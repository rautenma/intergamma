package nl.intergamma.poc.dto;

import lombok.Data;

@Data
public class CreateProductObject {
    private String productCode;
    private String productName;
    private int available;
}
