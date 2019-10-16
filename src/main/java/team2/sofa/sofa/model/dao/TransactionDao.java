package team2.sofa.sofa.model.dao;

import org.springframework.data.repository.CrudRepository;
import team2.sofa.sofa.model.Transaction;


public interface TransactionDao extends CrudRepository<Transaction, Integer> {

}
