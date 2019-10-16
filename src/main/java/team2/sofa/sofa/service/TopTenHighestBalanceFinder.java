package team2.sofa.sofa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team2.sofa.sofa.model.BusinessAccount;
import team2.sofa.sofa.model.PrivateAccount;
import team2.sofa.sofa.model.dao.BusinessAccountDao;
import team2.sofa.sofa.model.dao.PrivateAccountDao;

import java.util.List;

@Service
public class TopTenHighestBalanceFinder {

    public TopTenHighestBalanceFinder(){super();}

    @Autowired
    PrivateAccountDao privateAccountDao;

    @Autowired
    BusinessAccountDao businessAccountDao;

    /** vind de tien particuliere rekeningen met het hoogste saldo
     * @return geeft een geordende lijst terug van PrivateAccount-objecten */

    public List<PrivateAccount> getTopTenHighestBalance(){
        List<PrivateAccount> topTen = privateAccountDao.findTop10ByOrderByBalanceDesc();
        return topTen;
    }


    /** de tien zakelijke rekeningen met het hoogste saldo - zie hierboven */
    public List<BusinessAccount> getTopTenHighestBalanceBusiness(){
        List<BusinessAccount> topTen = businessAccountDao.findTop10ByBusinessIsNotNullOrderByBalanceDesc();
        return topTen;
    }
}
