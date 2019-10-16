package team2.sofa.sofa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team2.sofa.sofa.model.Address;
import team2.sofa.sofa.model.Client;
import team2.sofa.sofa.model.dao.AccountDao;
import team2.sofa.sofa.model.dao.AddressDao;
import team2.sofa.sofa.model.dao.ClientDao;

import java.util.Map;

@Service
public class UpdateClient {

    @Autowired
    ClientDao clientDao;
    @Autowired
    AddressDao addressDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    NewAccountChecker checker;

    public UpdateClient() {
        super();
    }

    public Address findAddress(int id) {
        return addressDao.findById(id).get();
    }

    public Client findClient(int id) {
        return clientDao.findClientById(id);
    }

    public boolean usernameExists(String newUsername) {
        return clientDao.existsClientByUsername(newUsername);
    }

    public Client processChanges(Client client, Map<String, String> input) {
        client.setFirstName(input.get("first"));
        client.setPrefix(input.get("prefix"));
        client.setLastName(input.get("last"));
        client.setEmail(input.get("email"));
        client.setTelephoneNr(input.get("telephone"));
        client.setPassword(input.get("pass"));
        clientDao.save(client);
        return client;
    }

    public Address checkAddress(Map<String,String> input){
        Address address = new Address();
        address.setStreet(input.get("street"));
        address.setHouseNumber(Integer.valueOf(input.get("houseNumber")));
        address.setZipCode(input.get("zipCode"));
        address.setCity(input.get("city"));
        if (checker.AddressExistsChecker(address)) {
            Address exists = checker.AddressExists(address);
            return exists;
        }
        return address;
    }

    public Client changeAddress(Client client, Address newAddress) {
        int oldAddressId = client.getAddress().getId();
        int count = clientDao.countClientsByAddressId(oldAddressId);
        if (count > 1) {
            addressDao.save(newAddress);
            client.setAddress(newAddress);
            clientDao.save(client);
            return client;
        }
        addressDao.save(newAddress);
        client.setAddress(newAddress);
        clientDao.save(client);
        addressDao.deleteById(oldAddressId);
        return client;
    }
}
