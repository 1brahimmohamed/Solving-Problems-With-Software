import edu.duke.*;
import org.apache.commons.csv.*;

import java.io.*;

public class BabyNames {
    public static void main(String[] args) {
//        testGetRank("Owen","M");
        testGetName(430,"M");
//        testYearOfHighestRank();
//        testGetTotalBirthsRankedHigher();
//        testingTotalBirth();
//          testGetAverageRank();
//        testWhatIsNameInYear("Own","M");
    }

    /*
     * This Methods gets
     *  the number to total births,  the number of the boys births, the number of girls births
     *  the number of the total names, the number of the boys names, the number of the girls names
     * */
    public static int totalBirth(FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        int totalNumberOfNames = 0;
        int totalNumberOfBoysNames = 0;
        int totalNumberOfGirlsNames = 0;
        String currName = null;
        String pervName = null;

        for (CSVRecord rec : fr.getCSVParser(false)) {

            int numBorn = Integer.parseInt(rec.get(2));
            currName = rec.get(0);

            totalBirths += numBorn;

            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
            } else {
                totalGirls += numBorn;
            }
            if (!currName.equals(pervName)) {
                if (rec.get(1).equals("M")) {
                    totalNumberOfBoysNames++;
                } else {
                    totalNumberOfGirlsNames++;
                }
            }
            pervName = currName;
        }

        totalNumberOfNames = totalNumberOfBoysNames + totalNumberOfGirlsNames;
        System.out.println("Total births: " + totalBirths);
        System.out.println("Total boys: " + totalBoys);
        System.out.println("Total girls: " + totalGirls);
        System.out.println("Total names: " + totalNumberOfNames);
        System.out.println("Total boys names: " + totalNumberOfBoysNames);
        System.out.println("Total girls names: " + totalNumberOfGirlsNames);
        return totalGirls;
    }

    public static void testingTotalBirth() {
        FileResource fr = new FileResource();
        ;
        totalBirth(fr);
    }

    public static int getNumOfGirls(FileResource fr) {
        String currName = null;
        String pervName = null;
        int totalNumberOfGirlsNames = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            currName = rec.get(0);

            if (!currName.equals(pervName)) {
                if (rec.get(1).equals("F")) {
                    totalNumberOfGirlsNames++;
                }
            }
            pervName = currName;
        }
        return totalNumberOfGirlsNames;
    }

    /*
     * This Methods gets
     * The rank of certain name
     * */

    public static int getRank(String name, String gender) {
        int count = 0;
        int rank = 0;
        boolean found = false;
        FileResource fr = new FileResource();
        return getNums(fr, name, gender, count, rank, found);

    }

    public static int getRankCustom(FileResource fr, String name, String gender) {
        int count = 0;
        int rank = 0;
        boolean found = false;
        return getNums(fr, name, gender, count, rank, found);

    }

    private static int getNums(FileResource fr, String name, String gender, int count, int rank, boolean found) {
        for (CSVRecord rec : fr.getCSVParser(false)) {
            count++;
            String nameFromFile = rec.get(0);
            String genderFromFile = rec.get(1);

            if (gender.equals(genderFromFile)) {
                if (name.equals(nameFromFile)) {
                    if (gender.equals("F")) {
                        rank = count;
                        found = true;
                    } else {
                        rank = count - getNumOfGirls(fr);
                        found = true;
                    }
                }
            }
        }

        if (!found)
            return -1;
        else
            return rank;
    }

    public static void testGetRank(String name, String gender) {
        int rank = getRank(name, gender);
        System.out.println("The name rank in this year was " + rank);
    }


    /*
     * This Methods gets
     * The name of certain rank
     *
     * */

    public static String getName(int rank, String gender) {
        FileResource fr = new FileResource();
        int count = 0;
        boolean found = false;
        String nameNeeded = null;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            count++;
            String nameFromFile = rec.get(0);
            String genderFromFile = rec.get(1);
            if (genderFromFile.equals(gender)) {
                if (gender.equals("F")) {
                    if (rank == count) {
                        nameNeeded = nameFromFile;
                        found = true;
                    }
                } else {
                    if (rank == (count - getNumOfGirls(fr))) {
                        nameNeeded = nameFromFile;
                        found = true;
                    }
                }
            }
        }
        if (found)
            return nameNeeded;
        else
            return "NO NAME";
    }

    public static void testGetName(int rank, String gender) {
        String name = getName(rank, gender);
        System.out.println("The name in that rank is " + name);
    }

    public static String whatIsNameInYear(String yearReal, String expectedYear, String name, String gender) {
        int rankReal = getRank(name, gender);
        String expectedName = getName(rankReal, gender);

        return name + " born in " + yearReal + " would be " + expectedName + " if he\\she was born in " + expectedYear;
    }

    public static void testWhatIsNameInYear(String name, String gender) {
        String newName = whatIsNameInYear("1974", "2014 ", name, gender);
        System.out.println(newName);
    }

    public static String yearOfHighestRank(String name, String gender) {

        DirectoryResource dr = new DirectoryResource();
        int highestRank = 0;
        String fileName = null;
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            int rank = getRankCustom(fr, name, gender);

            if (highestRank == 0) {
                if (rank > 0){
                    highestRank = rank;
                    fileName = f.getName();
                };
            }

            if (rank < highestRank) {
                if(rank > 0){
                    highestRank = rank;
                    fileName = f.getName();
                }
            }
        }

        return highestRank + " in " + fileName;
    }

    public static void testYearOfHighestRank(String name, String gender) {
        System.out.println("Highest Rank was " + yearOfHighestRank(name, gender));
    }

    public static double getAverageRank(String name, String gender) {
        DirectoryResource dr = new DirectoryResource();
        double avgRank = 0;
        int totalRank = 0;
        double count = 0;
        int rank = 0;

        for (File f : dr.selectedFiles()) {
            count++;
            FileResource fr = new FileResource(f);
            rank = getRankCustom(fr, name, gender);
            totalRank += rank;
        }
        avgRank = totalRank / count;
        return avgRank;
    }

    public static void testGetAverageRank(String name, String gender) {
        System.out.println("Average Rank is " + getAverageRank(name, gender));
    }

    public static int getTotalBirthsRankedHigher(String name, String gender) {
        int totalBirthsBefore = 0;
        int numBorn = 0;
        String currName = null;
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {

            numBorn = Integer.parseInt(rec.get(2));
            currName = rec.get(0);

            if (!currName.equals(name)){
                totalBirthsBefore += numBorn;
            }
            else
                break;
        }
        if (gender.equals("F"))
            totalBirthsBefore += numBorn;
        else
            totalBirthsBefore = totalBirthsBefore - totalBirth(fr);

        System.out.println("NUMBORN IS "+ numBorn);
        return totalBirthsBefore;
    }

    public static void testGetTotalBirthsRankedHigher(String name, String gender) {
        System.out.println("Total Births before " + getTotalBirthsRankedHigher(name, gender));
    }
}
