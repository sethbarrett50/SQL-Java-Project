/*
Project 2 for CSCI 3410 DBS created by Seth Barrett
The purpose of this code is to create an application for users to interact with the database created in project 1
I choose to mainly focus on the company table and specifically the url attribute for my interactions
There was a requirement to interact with the database through an update, a select-project-join query and an insertion of data
*/

import java.sql.*;
//Import all of sql
import java.util.Scanner;
//Import scanner for getting the user's input ie. C.RL()
import java.util.ArrayList;
//Import ArrayList for holding data



public class VaccineProg {
  public static void main (String[] args) {
    try (Connection conn = DriverManager.getConnection(
                                                       "jdbc:mysql://localhost:3306/HW_Vaccine?user=testuser&password=password&allowMultiQueries=true&createDatabaseIfNotExist=true&useSSL=true"); Statement stmt = conn.createStatement(); ) {
      
    	
      /*
       * Select-Project-Join-Query
       */
      ResultSet allVName = stmt.executeQuery("SELECT  Name FROM VACCINE;");
      //Sets new ResultSet object to be the equal to the SELECT operation of vaccine name
      System.out.println("The names of the vaccines are:");
      //Prints header for the response from the query to the user
      ArrayList<String> vaccL = new ArrayList<String>();
      //Declares new ArrayList variable to contain all the names of the variables.
      while (allVName.next()) {vaccL.add(allVName.getString("Name"));}
      //Loops through the values returned by the select query to be added to vaccL
      for (int i = 0; i < vaccL.size(); i++) System.out.print((i+1) + " {" + vaccL.get(i) + "} ");
      //Loops through list of vaccine names and prints them to console
      System.out.println("\nEnter the name of the vaccine to get all the websites of the companies making it:");
      //Prints question of interaction to insert data to the user.
      Scanner ui = new Scanner(System.in);
      //Sets up scanner object to read the users input to console
      String vaccName = ui.nextLine();
      //Sets String variable vaccName to be the users input by using the scanner object
      while (!vaccL.contains(vaccName))
    	  //Loops questioning the user and reading their response until they input a valid vaccine name
      {
    	  System.out.println("Please a valid name of a vaccine:");
          //Prints a response to an invalid data entry by asking for a specific value out of a set
    	  vaccName = ui.nextLine();
          //Sets String variable vaccName to be the users input by using the scanner object
      }
      String query = "SELECT COMPANY.Website FROM VACCINE, COMPANY WHERE VACCINE.Name = \"" + vaccName + "\" AND VACCINE.Manufacturer = COMPANY.Name;";
      //Declares String variable query to equal a SQL Select-Project-Join Query
      ResultSet rset1 = stmt.executeQuery(query);
      //Sets new ResultSet object to be the equal to the SELECT operation of the variable query
      System.out.print("The urls that companies that are manufacturing vaccine " + vaccName + " are: ");
      //Prints header for the response from the query to the user
      while (rset1.next()){System.out.print(rset1.getString("Website") + " ");}
      //Loops through the values returned by Select-Project-Join query to display them to console.
      
      
      /*
       * Insertion of Data
       */
      System.out.println("\nDo you want to add another company? (y/n)");
      //Prints question of interaction to insert data to the user.
      String addComp = ui.nextLine();
      //Sets String variable addComp to be the users input of y or n by using the scanner object
      while(!addComp.toLowerCase().matches("y") && !addComp.toLowerCase().matches("n"))
    	  //Loops a question and reading of user input until the user inputs a valid response
      {
    	  System.out.println("Please only enter y or n:");
          //Prints a response to an invalid data entry by asking for a specific value out of a set
    	  addComp = ui.nextLine();
          //Sets String variable addComp to be the users input of y or n by using the scanner object
      }
      ArrayList<String> compL = new ArrayList<String>();
      //Declares new ArrayList variable to contain all the names of the existing companies
      if(addComp.toLowerCase().matches("y")) 
    	  //Starts set of code if the user's input is equal to an allowed value
      {
          System.out.println("Please enter the name of the company:");
          //Prints question of interaction to insert data to the user.
          addComp = ui.nextLine();
          //Sets String variable addComp to be the user's input of the new company's name by using the scanner object
          ResultSet allCName = stmt.executeQuery("SELECT  Name FROM COMPANY;");
          //Sets new ResultSet object to be the equal to the SELECT operation of companies' names
          while (allCName.next()) {compL.add(allCName.getString("Name"));}
          //Loops through the values returned by the select query to be added to compL
          while (compL.contains(addComp))
        	  //Loops questioning the user and reading their response until they input any non valid company name
          {
        	  System.out.println("Please a name not already taken by a company:");
              //Prints a response to an invalid data entry by asking for a specific value out of a set
        	  addComp = ui.nextLine();
              //Sets String variable addComp to be the users input by using the scanner object
          }
          System.out.println("Please enter the name of url starting with https:// for company " + addComp + ": ");
          //Prints question of interaction to insert data to the user
          String addCompUrl = ui.nextLine();
          //Sets String variable addCompUrl to be the user's input of the company's website's URL by using the scanner object
          while (!addCompUrl.matches("https://(.*)")) 
        	  //Loops a question and readline until the user inputs a valid response
          {
        	  System.out.println("Please enter an url starting with https://");
              //Prints a response to an invalid data entry by specifying the format of user response.
        	  addCompUrl = ui.nextLine();
              //Sets String variable addCompUrl to be the users input by using the scanner object
          }
          String inValues = "INSERT INTO COMPANY VALUES ('" + addComp + "', '" + addCompUrl + "');";
          //Declares String variable inValues to equal a SQL insertion
          System.out.println("You added " + stmt.executeUpdate(inValues) + "company and its url.");
          //Inserts new data into the database and return to console that values were added.
      }
      
      
      /*
       * Update of data
       */
      System.out.println("\nDo you want to update a company's url? (y/n)");
      //Prints question of interaction to insert data to the user
      addComp = ui.nextLine();
      //Sets String variable addComp to be the users input of y or n by using the scanner object
      while(!addComp.toLowerCase().matches("y") && !addComp.toLowerCase().matches("n"))
    	  //Loops a question and reading of user input until the user inputs a valid response
      {
    	  System.out.println("Please only enter y or n:");
          //Prints a response to an invalid data entry by asking for a specific value out of a set
    	  addComp = ui.nextLine();
          //Sets String variable addComp to be the users input of y or n by using the scanner object
      }
      if(addComp.toLowerCase().matches("y")) 
    	  //Starts set of code if the user's input is equal to an allowed value
      {
          System.out.println("Please enter which company which has the url to change: ");
          //Prints header to prompt user response 
          ResultSet allCName = stmt.executeQuery("SELECT Name FROM COMPANY;");
          //Sets new ResultSet object to be the equal to the SELECT operation of companies' names
          while (allCName.next()) {compL.add(allCName.getString("Name"));}
          //Loops through the values returned by the select query to be added to vaccL
          for (int i = 0; i < compL.size(); i++) System.out.print(compL.get(i) + " ");
          //Lists all current company names
          addComp = ui.nextLine();
          //Sets String variable addComp to be the users input by using the scanner object
          while (!compL.contains(addComp)) 
          {
        	  System.out.println("\nPlease enter which a valid company name which has the url to change: ");
              //Prints a response to an invalid data entry
        	  addComp = ui.nextLine();
              //Sets String variable addComp to be the users input by using the scanner object
          }
          System.out.println("\nPlease enter the updated url value:");
          //Prints question of interaction to insert data to the user
          String addCompUrl = ui.nextLine();
          //Sets String variable addComp to be the users input by using the scanner object
          while (!addCompUrl.matches("https://(.*)")) 
        	  //Loops a question and readline until the user inputs a valid response
          {
        	  System.out.println("Please enter an url starting with https://");
              //Prints a response to an invalid data entry by specifying the format of user response.
        	  addCompUrl = ui.nextLine();
              //Sets String variable addCompUrl to be the users input by using the scanner object
          }
          query = "UPDATE COMPANY SET Website = \'" + addCompUrl + "\' WHERE Name = \'" + addComp + "\';";
          //Sets query to the value of a SQL UPDATE statement
          System.out.println("You updated " + stmt.executeUpdate(query) + " company\'s url.");
          //Inserts new data into the database and return to console that values were added.
      }
      System.out.println("Thank you for using my Project 2 for DBS!");
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}