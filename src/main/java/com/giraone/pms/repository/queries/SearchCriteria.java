package com.giraone.pms.repository.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
