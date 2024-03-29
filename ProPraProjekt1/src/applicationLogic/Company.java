package applicationLogic;

/**
 * @author Niklas Schnettler
 *
 */
public class Company extends AutocompleteSuggestion{

	private String street;
	private int zipCode;
	private String city;
	private boolean isDummy;
	
	/*
	 * Id Guide:
	 *  0: Company complete, not saved in Database yet
	 * -1: Dummy Company, Company just created, missing Data
	 *  else: Company from Database
	 */
	
	public Company(int internalId, String name, String street, int zipCode, String city) {
		super(internalId, internalId, name);
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
	}
	
	//Dummy Company
	public Company(int internalId, String name) {
		super(internalId, internalId, name);
	}
	
	//Dummy Company
	public Company(String name) {
		super(0, 0, name);
		this.isDummy = true;
	}
	
	public boolean isDummy() {
		return isDummy;
	}

	public String getStreet() {
		return street;
	}

	public int getZipCode() {
		return zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
