package com.example.models;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Categories {
    private Long id;
    private String nameCategory;
    private String description;//чисто сводка правил будет по типу, что туда подходит
}
