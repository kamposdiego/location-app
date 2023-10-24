package br.com.morsesystems.location.shared.specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SearchCriteria implements Serializable {

    private String key;
    private String operation;
    private Object value;

}
