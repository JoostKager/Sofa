package team2.sofa.sofa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team2.sofa.sofa.model.Client;
import team2.sofa.sofa.model.Employee;
import team2.sofa.sofa.model.dao.ClientDao;
import team2.sofa.sofa.model.dao.EmployeeDao;


@Service
public class PasswordValidator {


    @Autowired
    ClientDao clientDao;

    @Autowired
    EmployeeDao employeeDao;

    public PasswordValidator() {
        super();
    }

    public boolean validateClientPassword(Client client) {
        boolean loginOk;
        Client clients = clientDao.findClientByUsername(client.getUsername());
        if (!(clients == null)){
            loginOk = client.getPassword().equals(clients.getPassword());
            return loginOk;
        }
        return false;
    }

    public boolean validateEmployeePassword(Employee employee) {
        boolean loginOk;
        Employee employee1 = employeeDao.findEmployeeByUsername(employee.getUsername());
        loginOk = employee.getPassword().equals(employee1.getPassword());
        return loginOk;
    }
}

