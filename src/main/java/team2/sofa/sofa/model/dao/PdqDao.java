package team2.sofa.sofa.model.dao;

import org.springframework.data.repository.CrudRepository;
import team2.sofa.sofa.model.Pdq;

public interface PdqDao extends CrudRepository<Pdq, Integer> {

    boolean existsPdqByFiveDigitcode(String fiveDigitCode);

    Pdq findPdqByFiveDigitcode(String fiveDigitCode);



}
