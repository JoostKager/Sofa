package team2.sofa.sofa.service;

import org.springframework.stereotype.Service;

@Service
public class BalancePerSectorData {


        private String sector;
        private double averageBalance;

        public BalancePerSectorData() {
            super();
            this.sector = null;
            this.averageBalance = 0;
        }

        public BalancePerSectorData(String sector, double averageBalance) {
            this.sector = sector;
            this.averageBalance = averageBalance;
        }

        public String getSector() {
            return sector;
        }

        public void setSector(String sector) {
            this.sector = sector;
        }

        public double getAverageBalance() {
            return averageBalance;
        }

        public void setAverageBalance(double averageBalance) {
            this.averageBalance = averageBalance;
        }


}
