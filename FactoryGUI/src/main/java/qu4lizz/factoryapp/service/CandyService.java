package qu4lizz.factoryapp.service;

import qu4lizz.factoryapp.dao.CandyDAO;
import qu4lizz.factoryapp.model.Candy;

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

    public int getMaxId() { return candyDAO.getMaxId(); }
}
