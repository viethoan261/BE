
package hoan.com.springboot.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetDataRequest {
    private String esIndex;
    private Integer page;
    private Integer size;
    private String fields;
    private String filter;
    private String sort;
    private String q;
    private boolean withId;
}

