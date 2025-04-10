package com.raxrot.sproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 3,message = "Category should contain at least 3 characters")
    private String categoryName;
}
