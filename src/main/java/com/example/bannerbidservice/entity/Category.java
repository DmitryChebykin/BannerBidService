package com.example.bannerbidservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@DynamicUpdate
@Table(name = "category")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String categoryName;

    @Size(max = 255)
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255)", name = "req_name")
    private String reqName;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean deleted;

    @JsonIgnore
    @OneToMany(mappedBy = "category", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Banner> banners;

    public static final class CategoryBuilder {
        private final Category category;

        private CategoryBuilder() {
            category = new Category();
        }

        public static CategoryBuilder aCategory() {
            return new CategoryBuilder();
        }

        public CategoryBuilder name(String name) {
            category.setCategoryName(name);
            return this;
        }

        public CategoryBuilder reqName(String reqName) {
            category.setReqName(reqName);
            return this;
        }

        public CategoryBuilder deleted(Boolean deleted) {
            category.setDeleted(deleted);
            return this;
        }

        public Category build() {
            return category;
        }
    }
}