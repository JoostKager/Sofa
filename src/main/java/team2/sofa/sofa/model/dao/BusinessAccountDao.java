package team2.sofa.sofa.model.dao;

import org.springframework.data.repository.CrudRepository;
import team2.sofa.sofa.model.BusinessAccount;

import java.util.List;

public interface BusinessAccountDao extends CrudRepository <BusinessAccount, Integer> {

        List<BusinessAccount> findTop10ByBusinessIsNotNullOrderByBalanceDesc();


}
