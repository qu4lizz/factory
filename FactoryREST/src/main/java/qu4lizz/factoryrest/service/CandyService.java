package qu4lizz.factoryrest.service;

import qu4lizz.factoryrest.dao.CandyDAO;
import qu4lizz.factoryrest.model.Candy;

import java.util.ArrayList;

public class CandyService {
    private final CandyDAO candyDAO;

    public CandyService() {
        candyDAO = new CandyDAO();
    }
    public ArrayList<Candy> getCandies() {
        return (ArrayList<Candy>) candyDAO.getCandies();
    }

    public Candy getCandyById(int id) {
        return candyDAO.getCandyById(id);
    }

    public void addCandy(Candy candy) {
        candyDAO.addCandy(candy);
    }

    public void updateCandy(Candy candy) {
        candyDAO.updateCandy(candy);
    }

    public boolean deleteCandy(int id) {
        return candyDAO.deleteCandy(id);
    }
}
