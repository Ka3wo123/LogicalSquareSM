package pl.logicalsquare.IOproject.domain;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class State {

    private Integer id;

    private AirplaneTrafficState state;
    private String name;
}
