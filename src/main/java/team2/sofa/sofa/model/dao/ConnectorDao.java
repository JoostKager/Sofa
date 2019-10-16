package team2.sofa.sofa.model.dao;

import org.springframework.data.repository.CrudRepository;
import team2.sofa.sofa.model.Connector;

import java.util.List;

public interface ConnectorDao extends CrudRepository<Connector, Integer> {

    boolean existsConnectorByUsername(String username);
    List<Connector> findConnectorsByUsername(String username);
    boolean existsConnectorByIban(String iban);

}
