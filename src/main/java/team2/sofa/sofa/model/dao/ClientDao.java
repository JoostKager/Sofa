package team2.sofa.sofa.model.dao;

import org.springframework.data.repository.CrudRepository;
import team2.sofa.sofa.model.Client;

import java.util.List;

public interface ClientDao extends CrudRepository<Client, Integer> {

    int countClientsByAddressId(int id);
    boolean existsClientByUsername(String username);
    Client findClientByUsername(String username);
    Client findClientBySsn(String ssn);

    Client findClientById(int id);

    List<Client> findTop10ByOrderByTotalNumberOfTransactionsDesc();

    List<Client> findClientsByAccountsNull();

}
