package team2.sofa.sofa.model.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import team2.sofa.sofa.model.Account;
import java.util.List;

public interface AccountDao extends CrudRepository<Account, Integer> {


    Account findAccountById(int id);

    Account findAccountByIban(String iban);

    boolean existsAccountByIban(String iban);

    @Query(value = "select business.sector, avg(account.balance) as averageBalance from Account account " +
            "left join account.owners client " +
            "join Business business on client = business.owner " +
            "where account.isBusinessAccount=1 group by business.sector")
    List<String> getBalancePerSector();

}
