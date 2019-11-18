package team2.sofa.sofa.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter @NoArgsConstructor
public class Login {

    @Id
    @GeneratedValue(generator = "USER_SEQ")
    private int id;
    private String username;
    private String password;
    private Enum loginRole;
}
