package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import applicationLogic.Branch;
import dataStorageAccess.DBConnection;

public class BranchController {
	//Get Branches starting with name --> Used for Autocomplete
	public static ArrayList<Branch> filterBranch(String name) throws SQLException {
		ArrayList<Branch> result = new ArrayList<Branch>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Branches WHERE branch_name LIKE '" + name + "%'");
		) {
			while (resultSet.next()) {
				result.add(new Branch(resultSet.getInt("branch_id"), resultSet.getString("branch_name")));
			}
		}
		return result;
	}
	
	
	/**
	 * @return A List off all Branches
	 * @throws SQLException
	 */
	public static ArrayList<Branch> getAllBranches() throws SQLException {
		ArrayList<Branch> result = new ArrayList<Branch>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Branches");
		) {
			while (resultSet.next()) {
				result.add(new Branch(resultSet.getInt("branch_id"), resultSet.getString("branch_name")));
			}
		}
		return result;
	}
	
	
	/**
	 * @return A List off all Branches which had defects
	 * @throws SQLException
	 */
	public static ArrayList<Branch> getAllBranchesWithDefect() throws SQLException {
		ArrayList<Branch> result = new ArrayList<Branch>();
		try (
			Connection connection = DBConnection.getInstance().initConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
					"SELECT * " +
					"FROM Branches " +
					"WHERE branch_id IN ( " +		
						"SELECT branch_id " +
						"FROM DefectElement )"
			);
		) {
			while (resultSet.next()) {
				result.add(new Branch(resultSet.getInt("branch_id"), resultSet.getString("branch_name")));
			}
		}
		return result;
	}

}
