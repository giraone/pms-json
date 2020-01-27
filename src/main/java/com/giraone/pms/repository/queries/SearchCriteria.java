package com.giraone.pms.repository.queries;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Generated
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
