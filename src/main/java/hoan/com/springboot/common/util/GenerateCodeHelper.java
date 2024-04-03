package hoan.com.springboot.common.util;

import java.util.Random;

public class GenerateCodeHelper {

    private static Random random = new Random();
    public static String generateEmployeeCode() {

        int randomNumber = random.nextInt(9000) + 1000;

        return "EHUST" + randomNumber;
    }

    public static String generateRoleCode() {

        int randomNumber = random.nextInt(9000) + 1000;

        return "RHUST" + randomNumber;
    }

    public static String generateDepartmentCode() {

        int randomNumber = random.nextInt(9000) + 1000;
        
        return "DHUST" + randomNumber;
    }
}
