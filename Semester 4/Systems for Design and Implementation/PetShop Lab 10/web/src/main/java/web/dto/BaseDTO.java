package web.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BaseDTO<ID extends Serializable> implements Serializable {
    public ID id;
}
