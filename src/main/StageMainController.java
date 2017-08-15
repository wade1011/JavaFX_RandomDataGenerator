package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;

public class StageMainController
{

    @FXML TextField txtNumToGen;
    @FXML TableView<Person> tableView;
    @FXML TableColumn<Person, String> colFirstName;
    @FXML TableColumn<Person, String> colLastName;
    @FXML TableColumn<Person, String> colEmail;
    @FXML TableColumn<Person, Byte> colBirthDay;
    @FXML TableColumn<Person, Byte> colBirthMonth;


    private char[] vowelArray = {'a', 'e', 'i', 'o', 'u'};
    private char[] lowerCaseArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private String[] firstNamesArray;
    private String[] lastNamesArray;
    private String[] emails;
    private byte[] bDay;
    private String[] bMonth;

    ObservableList<Person> list;
    int inputToInt;

    public ObservableList<Person> generatePerson()
    {
        //region Parse user input as integer, if fails message user and return null
        String userInput = txtNumToGen.getText();
        tryParseInt(userInput);

        if (tryParseInt(userInput))
        {
            inputToInt = Integer.parseInt(userInput);  // We now know that it's safe to parse
            if (inputToInt <= 0)
            {
                errorAlert();
                return null;
            }
        }
        else if (!tryParseInt(userInput))
        {
            errorAlert();
            return null;
        }
        //endregion
        
        list = FXCollections.observableArrayList();

        firstNamesArray = genName();
        lastNamesArray = genName();
        emails = genEmails();
        bDay = genDay();
        bMonth = genMonth();

        for (int i = 0; i < inputToInt; i++)
        {
            Person person = new Person();

            person.setFirstName(firstNamesArray[i]);
            person.setLastName(lastNamesArray[i]);
            person.setEmail(emails[i]);
            person.setDayOfBirth(bDay[i]);
            person.setMonthOfBirth(bMonth[i]);

            list.add(person);
        }

        int randomPerson = (int)Math.random() * list.size();
        System.out.println("Created an object with " + list.size() + " entries.\ne.g.\nName: " + list.get(randomPerson).getFirstName() + " " + list.get(randomPerson).getLastName() + "\nEmail: " + list.get(randomPerson).getEmail() + "\nD.O.B: " + list.get(randomPerson).getDayOfBirth() + " " + list.get(randomPerson).getMonthOfBirth() + "\n");
        return list;
    }

    //region Check if parse throws exception
    public boolean tryParseInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
    //endregion

    //region Create message box
    public void errorAlert()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid number entered.");
        alert.setContentText("Try again!");
        alert.showAndWait();
    }

    public void finishedAlert()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Finished writing file.");
        alert.setContentText("Placed in project directory.");
        alert.showAndWait();
    }
    //endregion

    public void writePersonDetailsToFile()
    {
        try
        {
            OutputStream os = new FileOutputStream("person.txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            for (int i = 0; i < inputToInt; i++)
            {
                bw.write(firstNamesArray[i] + "," + lastNamesArray[i] + "," + emails[i] + "," + bDay[i] + "," + bMonth[i]);
                bw.newLine();
            }
            bw.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        finally
        {
            finishedAlert();
        }
    }


    public ObservableList<Person> readFileToList()
    {
        //Clear TableView
        if (tableView.getItems().size() > 0)
        {
            for ( int i = 0; i<tableView.getItems().size(); i++)
            {
                tableView.getItems().clear();
            }
        }

        list = FXCollections.observableArrayList();
        File file = new File("person.txt");

        try
        {
            FileInputStream fs = new FileInputStream(file);
            InputStreamReader in = new InputStreamReader(fs);
            BufferedReader br = new BufferedReader(in);

            while (true)
            {
                final String line = br.readLine();
                if (line == null) break;

                String[] items = line.split(",");

                Person person = new Person();

                person.setFirstName(items[0]);
                person.setLastName(items[1]);
                person.setEmail(items[2]);
                byte dOB = Byte.parseByte(items[3]);
                person.setDayOfBirth(dOB);
                person.setMonthOfBirth(items[4]);

                if (person.equals(null))
                {
                    break;
                }

                list.add(person);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        readData();

        return list;
    }


    public void readData()
    {
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colBirthDay.setCellValueFactory(new PropertyValueFactory<>("dayOfBirth"));
        colBirthMonth.setCellValueFactory(new PropertyValueFactory<>("monthOfBirth"));

        tableView.setItems(list);
    }


    private String[] genName()
    {
        String[] names = new String[inputToInt];

        for (int i = 0; i < inputToInt; i++)
        {
            byte rndNameLength = ((byte)(Math.random() * 3));

            if (rndNameLength == 0)
            {
                char randomCapital = (char) ((int) 'A' + Math.random() * ((int) 'Z' - (int) 'A' + 1));

                byte oneToFive = (byte) Math.floor(Math.random() * 5);
                char secondLetter = vowelArray[oneToFive];

                char[] twoLowerCase = {lowerCaseArray[randomOneToTwentyFive()], lowerCaseArray[randomOneToTwentyFive()]};

                StringBuilder sb = new StringBuilder();
                sb.append(randomCapital);
                sb.append(secondLetter);
                sb.append(twoLowerCase);
                String str = sb.toString();

                names[i] = str;
            }

            else if (rndNameLength == 1)
            {
                char randomCapital = (char) ((int) 'A' + Math.random() * ((int) 'Z' - (int) 'A' + 1));

                byte oneToFive = (byte) Math.floor(Math.random() * 5);
                char secondLetter = vowelArray[oneToFive];

                char[] twoLowerCase = {lowerCaseArray[randomOneToTwentyFive()], lowerCaseArray[randomOneToTwentyFive()]};

                char[] medName =
                        {
                                lowerCaseArray[randomOneToTwentyFive()],
                                lowerCaseArray[randomOneToTwentyFive()],
                                lowerCaseArray[randomOneToTwentyFive()]
                        };
                StringBuilder sb = new StringBuilder();
                sb.append(randomCapital);
                sb.append(secondLetter);
                sb.append(twoLowerCase);
                sb.append(medName);
                String str = sb.toString();

                names[i] = str;
            }

            else if (rndNameLength == 2)
            {
                char randomCapital = (char) ((int) 'A' + Math.random() * ((int) 'Z' - (int) 'A' + 1));

                byte oneToFive = (byte) Math.floor(Math.random() * 5);
                char secondLetter = vowelArray[oneToFive];

                char[] twoLowerCase = {lowerCaseArray[randomOneToTwentyFive()], lowerCaseArray[randomOneToTwentyFive()]};

                char[] larName =
                        {
                                lowerCaseArray[randomOneToTwentyFive()],
                                lowerCaseArray[randomOneToTwentyFive()],
                                lowerCaseArray[randomOneToTwentyFive()],
                                lowerCaseArray[randomOneToTwentyFive()],
                                lowerCaseArray[randomOneToTwentyFive()],
                                lowerCaseArray[randomOneToTwentyFive()],
                                lowerCaseArray[randomOneToTwentyFive()]
                        };
                StringBuilder sb = new StringBuilder();
                sb.append(randomCapital);
                sb.append(secondLetter);
                sb.append(twoLowerCase);
                sb.append(larName);
                String str = sb.toString();

                names[i] = str;
            }
        }
        return names;
    }


    private String[] genEmails()
    {
        emails = new String[inputToInt];
        for (int i = 0; i < inputToInt; i++)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(firstNamesArray[i]);
            sb.append(".");
            sb.append(lastNamesArray[i]);
            sb.append("@email.com");
            String newEmail = sb.toString();

            emails[i] = newEmail;
        }
        return emails;
    }


    private byte[] genDay()
    {
        byte[] bDay = new byte[inputToInt];

        for (int i = 0; i < inputToInt; i++)
        {
            byte day = (byte) (Math.random() * 31 + 1);
            bDay[i] = day;
        }
        return bDay;
    }


    private String[] genMonth()
    {
        String[] bMonth = new String[inputToInt];

        for (int i = 0; i < inputToInt; i++)
        {
            byte month = (byte) (Math.random() * 12 + 1);
            String monthString;

            if (month == 1) { monthString = "January"; }
            else if (month == 2) { monthString = "February"; }
            else if (month == 3) { monthString = "March"; }
            else if (month == 4) { monthString = "April"; }
            else if (month == 5) { monthString = "May"; }
            else if (month == 6) { monthString = "June"; }
            else if (month == 7) { monthString = "July"; }
            else if (month == 8) { monthString = "August"; }
            else if (month == 9) { monthString = "September"; }
            else if (month == 10) { monthString = "October"; }
            else if (month == 11) { monthString = "November"; }
            else { monthString = "December"; }

            bMonth[i] = monthString;
        }
        return bMonth;
    }


    private byte randomOneToTwentyFive()
    {
        byte n = (byte) Math.floor(Math.random() * 25);
        return n;
    }
}