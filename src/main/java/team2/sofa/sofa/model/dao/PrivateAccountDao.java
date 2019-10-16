package team2.sofa.sofa.model.dao;

import org.springframework.data.repository.CrudRepository;
import team2.sofa.sofa.model.PrivateAccount;

import java.util.List;

public interface PrivateAccountDao extends CrudRepository<PrivateAccount, Integer> {

    List<PrivateAccount> findTop10ByOrderByBalanceDesc();
}
