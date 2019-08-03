package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericUtils {

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String dateFix(Date date) {
        Calendar newDate = toCalendar(date);
        int day = newDate.get(Calendar.DAY_OF_MONTH);
        int month = newDate.get(Calendar.MONTH) + 1;
        int year = newDate.get(Calendar.YEAR);
        String finalDate = day + "/" + month + "/" + year;
        return finalDate;
    }

    public static String giveInputFromUser(String message) {
        String input = null;
        System.out.print(message);
        Scanner sc = new Scanner(System.in);
        input = sc.nextLine();
        return input;
    }

    public static int giveIntFromUser(String message) {
        boolean check = false;
        int input = 0;
        System.out.print(message);
        Scanner sc = new Scanner(System.in);
        do {
            if (sc.hasNextInt()) {
                input = sc.nextInt();
                check = true;
            } else {
                System.out.print("Give numbers only: ");
                sc.next();
            }
        } while (check == false);
        return input;
    }

    public static int giveIntFromUser(String message, int start, int end) {
        int input;
        boolean range = false;
        do {
            System.out.print(message);
            Scanner sc = new Scanner(System.in);
            while (!sc.hasNextInt()) {
                System.out.println("Not valid input!");
                sc.next();
            }
            input = sc.nextInt();
            if (input >= start && input <= end) {
                range = true;
            } else {
                System.out.println("Not valid input!");
            }
        } while (range == false);
        return input;
    }

    public static Date getDate(String message) {
        int x = 0;
        String d = "";
        Date date = new Date();
        do {
            SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy");
            Date testDate = new Date();
            System.out.println(message);
            d = giveInputFromUser("Input date Day/Month/Year : ");
            try {
                testDate = df.parse(d);
            } catch (ParseException e) {
                System.out.println("Wrong date format");
            }
            if (!df.format(testDate).equals(d)) {
                System.out.println("Not valid date!");
            } else {
                try {
                    date = df.parse(d);
                } catch (ParseException ex) {
                    Logger.getLogger(GenericUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
                x = 1;
            }
        } while (x == 0);
        return date;
    }

    public static boolean validChoice(String answer) {
        boolean yn = false, check = false;
        do {
            switch (answer) {
                case "y":
                    check = true;
                    yn = true;
                    break;
                case "n":
                    yn = true;
                    break;
                default:
                    answer = giveInputFromUser("Give valid option (y/n): ");
            }
        } while (yn == false);
        return check;
    }

}
