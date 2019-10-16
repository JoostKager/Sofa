package team2.sofa.sofa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team2.sofa.sofa.model.dao.AccountDao;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectorAnalyzer {

    @Autowired
    AccountDao accountDao;

    public SectorAnalyzer() {
    }

    public List<BalancePerSectorData> getAverageBalancePerSector(){
        List<String> balancePerSectorStrings = accountDao.getBalancePerSector();
        List<BalancePerSectorData> balancePerSectorList = new ArrayList<>();
        for (String item : balancePerSectorStrings){
            String[]parts = item.split(",");
            double averageBalance = (double)((int)(Double.valueOf(parts[1])*100))/100;
            BalancePerSectorData balancePerSector = new BalancePerSectorData(parts[0].toLowerCase(),averageBalance);
            balancePerSectorList.add(balancePerSector);
        }
        return  balancePerSectorList;
    }

}


