package team2.sofa.sofa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import team2.sofa.sofa.model.*;
import team2.sofa.sofa.model.dao.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class DbInitializer {
    //verschillende statics afhankelijk van hoeveelheid data die je wil inladen.
    //zie ook comment in Controller DatabaseController
    private final String BIG = "Data7000.csv";
    private final String MEDIUM = "Data5000.csv";
    private final String SMALL = "Data99.csv";
    private final int MAX_AMOUNT_BALANCE = 1000;
    private final int MIN_AMOUNT_BALANCE = 100;

    @Autowired
    ClientDao clientDao;
    @Autowired
    AddressDao addressDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    EmployeeDao employeeDao;
    @Autowired
    BusinessDao businessDao;
    @Autowired
    TransactionDao transactionDao;
    @Autowired
    FundTransfer fundTransfer;

    private final List<String[]> rawData;
    private final Deque<String> ssnStack;
    private final Deque<String> ibanStack;
    private final Deque<String> companyList;
    private final Random random;

    public DbInitializer() {
        super();
        rawData = makeDataList(BIG);
        ssnStack = SSNFunctionality.bsnStack(rawData.size());
        this.ibanStack = new IBANGenerator().ibanStack(rawData.size()*2);
        this.companyList = makeCompanyList();
        this.random = new Random();
    }
    /**
     * helper methode om data in CSV bestand in te laden en in lijst te plaatsen
     *
     * @return Lijst van iedere line in CSV bestand als ruwe String inclusief comma's.
     */
    private List<String[]> makeDataList(String size) {
        Scanner fileReader;
        List<String[]> data = new ArrayList<>();
        try {
            Resource resource = new ClassPathResource(size);
            File customerList = resource.getFile();
            fileReader = new Scanner(customerList);
            fileReader.nextLine();
            while (fileReader.hasNextLine()) {
                String[] split = fileReader.nextLine().split(",");
                data.add(split);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Helper methode genereert bedijfsnamen uit data.csv bestand.
     *
     * @return lijst bedrijfsnamen om in het model Business te gebruiken
     */
    private Deque<String> makeCompanyList() {
        Deque<String> company = new ArrayDeque<>();
        for (String[] raw : rawData) {
            if (!(raw.length == 11))
                company.push(raw[11].replace("\"", ""));
        }
        return company;
    }

    /**
     * Genereert business objects om op te slaan in de DB
     *
     * @param nrOfBussiness aantal te creëren businesses
     */
    public void makeBusiness(int nrOfBussiness, int nrOfClients) {
        for (int i = 0; i < nrOfBussiness; i++) {
            Business business = new Business();
            business.setBusinessName(companyList.pop());
            BusinessSector[] businessSectors = (BusinessSector.values());
            business.setSector(businessSectors[i%11]);
            int randomClient = random.nextInt(nrOfClients) + 1;
            Client client = clientDao.findClientById(randomClient);
            business.setOwner(client);
            businessDao.save(business);
            makeBusinessAccount(business,client);
        }
    }

    /**
     * maakt businessaccounts aan en koppelt deze aan clients
     *
     */
    private void makeBusinessAccount(Business business, Client client) {
            BusinessAccount ba = new BusinessAccount();
            ba.setIban(ibanStack.pop());
            BigDecimal balance = new BigDecimal(random.nextInt(MAX_AMOUNT_BALANCE)+ MIN_AMOUNT_BALANCE);
            ba.setBalance(balance);
            ba.setBusiness(business);
            connectAccount(client, ba);
    }
    /**
     * maakt clients en slaat deze op in DB
     *
     * @param count aantal te genereren clients
     */
    public void makeClient(int count) {
        for (int i = 0; i < count; i++) {
            Client client = new Client();
            String[] data = rawData.get(i);
            setName(client, data);
            Address address = makeAddress(data);
            addressDao.save(address);
            client.setAddress(address);
            client.setEmail(data[5]);
            client.setTelephoneNr(data[6]);
            client.setBirthday(data[7]);
            client.setGender(data[8]);
            setUsername(client, data);
            setPassword(client);
            client.setSsn(ssnStack.pop());
            clientDao.save(client);
        }
    }
    private void setUsername(Client client, String[] data){
        String username = client.getFirstName().substring(0,1) + client.getLastName();
        if (clientDao.existsClientByUsername(username)){
            client.setUsername(data[9]);}
        else {client.setUsername(username);}
    }
    private void setPassword(Client client){
        String passWord =  client.getLastName() + client.getFirstName().substring(0,1);
        client.setPassword(passWord);}

    /**
     * maakt Employees en slaat deze op in DB
     *
     */
    public void makeEmployees(int count) {
        String[] username = {"", "Aat", "Joost", "Danielle", "Bas", "Kirsten", "Rene"};
        for (int i = count; i > 0; i--) {
            int index = random.nextInt(rawData.size());
            Employee employee = new Employee();
            String[] emp = rawData.get(index);
            setName(employee, emp);
            Address address = makeAddress(emp);
            if (addressDao.existsAddressByZipCodeAndHouseNumber(address.getZipCode(),address.getHouseNumber())){
                address = addressDao.findAddressByZipCodeAndHouseNumber(address.getZipCode(),address.getHouseNumber());
            }
            addressDao.save(address);
            employee.setAddress(address);
            employee.setRole(null);
            employee.setEmail(emp[5]);
            employee.setTelephoneNr(emp[6]);
            employee.setBirthday(emp[7]);
            employee.setGender(emp[8]);
            employee.setUsername(username[i]);
            employee.setPassword(username[i]);
            employee.setSsn(ssnStack.pop());
            employeeDao.save(employee);
        }
    }
    public void assignEmployeeRoles(){
        EmployeeRole[] roles = EmployeeRole.values();
        List<Employee> list = (List<Employee>) employeeDao.findAll();
        list.get(0).setRole(roles[0]);
        list.get(1).setRole(roles[1]);
        for (int i = 2; i < list.size(); i++) {
            list.get(i).setRole(roles[2]);
        }
    }

    /**
     * helper methode voor het het splitsen en setten van firstname, prefix en lastname van de User (client, danwel employee)
     *
     * @param user  Kan Client, danwel Employee zijn.
     * @param split de String Array met data uit CSV bestand.
     */
    private void setName(User user, String[] split) {
        user.setFirstName(split[0].replace("\"", ""));
        //in CSV bestand zijn de voorvoegsels opgenomen in dezelfde kolom als de achternaam, vandaar onderstaande IF
        if (split[1].startsWith("\"")) {
            String cleanString = split[1].replace("\"", "");
            String[] splits = cleanString.split(" ");
            String prefix = makePrefix(splits);
            user.setPrefix(prefix);
            user.setLastName(splits[splits.length - 1]);
        } else {
            user.setLastName(split[1]);
        }
    }
    private String makePrefix(String[] splits){
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < splits.length - 1; i++) {
            prefix.append(splits[i]);
            if (i < splits.length - 2) {
                prefix.append(" ");
            }
        }
        return prefix.toString();
    }

    /**
     * helper methode om een Address te maken die gekoppeld is aan de User.
     * En deze vast weg te schrijven in DB zodat er een juiste koppeling ontstaat tussen de tabel address en user
     *
     * @param split de String Array met data uit CSV bestand.
     * @return een Address om te gebruiken in de concrete klasses van User
     */
    private Address makeAddress(String[] split) {
        Address address = new Address();
        StringBuilder street = new StringBuilder();
        String cleanString = split[2].replace("\"", "");
        String[] splits = cleanString.split(" ");
        for (int i = 0; i < splits.length - 1; i++) {
            street.append(splits[i]);
            if (i < splits.length - 2) {
                street.append(" ");
            }
        }
        address.setStreet(street.toString());
        address.setHouseNumber(Integer.parseInt(splits[splits.length - 1]));
        address.setZipCode(split[3].replace("\"", ""));
        address.setCity(split[4].replace("\"", ""));
        return address;
    }

    /**
     * Methode die voor Clients een variabel aantal Accounts inclusief IBAN en Saldo creëert en deze direct koppelt aan de Client.
     * Dit zodat er onderscheid is tusen Clients zonder particuliere Rekening, sommige met maar 1 en sommige met 2.
     * Tevens wordt er random een saldo gecreëerd op het gemaakte account tussen de 99.99 en 0.01
     */
    public void fillPrivateAccounts() {
        Iterable<Client> client = clientDao.findAll();
        for (Client c : client
        ) {
            int result = random.nextInt(3);
            for (int i = 0; i < result; i++) {
                PrivateAccount a = new PrivateAccount();
                a.setIban(ibanStack.pop());
                BigDecimal balance = new BigDecimal(random.nextInt(MAX_AMOUNT_BALANCE)+ MIN_AMOUNT_BALANCE);
                a.setBalance(balance);
                connectAccount(c, a);
            }
        }
    }

    /**
     * helper methode voor fillAccounts om op juiste wijze de Accounts en Owners te koppelen.
     * en in de DB weg te schrijven.
     *
     * @param client  nieuwe klant
     * @param account nieuwe account
     */
    private void connectAccount(Client client, Account account) {
        client.addAccount(account);
        account.addClient(client);
        clientDao.save(client);
        accountDao.save(account);
    }

    public void fillTransactions(){
        long maxId = accountDao.count();
        for (int i = 0; i < maxId ; i++) {
            int credit = random.nextInt((int) maxId)+1;
            int debit = random.nextInt((int) maxId)+1;
            while (debit == credit){
                debit = random.nextInt((int) maxId)+1;
            }
            Account cr = accountDao.findAccountById(credit);
            Account db = accountDao.findAccountById(debit);
            String[] arr={"ASKEBY", "ASARUM", "LANDSKRONA", "KLIPPAN", "HERNES", "FRIHETEN", "EKTORP"};
            int randomNumber=random.nextInt(arr.length);
            String date = createRandomDate(2016,2019);
            Transaction t = new Transaction(new BigDecimal(random.nextInt(10)+1),arr[randomNumber],date,cr,db);
            fundTransfer.storeTransaction(db,cr,t);
        }
    }
    public void makeDoubleAccounts(){
        List<Client> list = clientDao.findClientsByAccountsNull();
        List<Account> accounts = (List<Account>) accountDao.findAll();
        for (Client client :
                list) {
            Account account;
            do {
                int index = random.nextInt(accounts.size());
                account = accounts.get(index);
            }
            while(account.getOwners().size() == 2);
            connectAccount(client, account);
        }
    }
    private String createRandomDate(int startYear, int endYear) {
        int day;
        int month;
        int year;
        do {
            day = createRandomIntBetween(1, 28);
            month = createRandomIntBetween(1, 12);
            year = createRandomIntBetween(startYear, endYear);
        } while (LocalDate.now().isBefore(LocalDate.of(year,month,day)));
        return String.valueOf(LocalDate.of(year, month, day));
    }
    private int createRandomIntBetween(int low, int high){
        return random.nextInt((high - low) + 1) + low;
    }
}

