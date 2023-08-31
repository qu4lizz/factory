package qu4lizz.factoryapp.utils;

import java.io.File;

public class DbUtil {
    public static final String ROOT_FOLDER = System.getProperty("user.dir");
    public static final String DATABASE_FOLDER;

    static {
        DATABASE_FOLDER = new File(ROOT_FOLDER).getParentFile() + "/database/";
    }
}
