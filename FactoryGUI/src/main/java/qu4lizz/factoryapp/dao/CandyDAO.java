package qu4lizz.factoryapp.dao;

import qu4lizz.factoryapp.model.Candy;
import qu4lizz.factoryapp.utils.ConfigUtil;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CandyDAO {
    private JedisPool jedisPool = new JedisPool(ConfigUtil.getProperties().getProperty("jedis_host"));
    private static final String KEY = "item:";

    public CandyDAO() { }

    public List<Candy> getCandies(){
        List<Candy> products = new ArrayList<>();
        try(Jedis jedis = jedisPool.getResource()){
            Set<String> keys = jedis.keys(KEY + "*");
            for(String key : keys) {
                Map<String, String> productMap = jedis.hgetAll(key);
                products.add(new Candy(productMap));
            }
        }
        return products;
    }

    public Candy getCandyById(int productId) {
        String key = "product:" + productId;
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> objectMap = jedis.hgetAll(key);

            if (objectMap != null && !objectMap.isEmpty()) {
                return new Candy(objectMap);
            }
        }
        return null;
    }

    public void addCandy(Candy item) {
        Jedis jedis = jedisPool.getResource();
        jedis.hmset(KEY + item.getId(), item.toMap());
    }

    public void updateCandy(Candy product) {
        String key = "product:" + product.getId();
        Jedis jedis = jedisPool.getResource();
        jedis.hmset(key, product.toMap());
    }

    public boolean deleteCandy(int productId) {
        String key = "product:" + productId;
        Jedis jedis = jedisPool.getResource();
        return jedis.del(key) == 1;
    }

    public int getMaxId() {
        try (Jedis jedis = jedisPool.getResource()) {
            var keys = jedis.keys(KEY + "*");

            int maxId = keys.stream()
                    .mapToInt(key -> Integer.parseInt(key.substring(KEY.length())))
                    .max()
                    .orElse(0);

            return maxId;
        }
    }
}
