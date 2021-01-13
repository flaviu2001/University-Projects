package application;

public enum Gender {
	 
	   FEMALE("F", "Female"), MALE("M", "Male");
	 
	   private String code;
	   private String text;
	 
	   private Gender(String code, String text) {
	       this.code = code;
	       this.text = text;
	   }
	 
	   public String getCode() {
	       return code;
	   }
	 
	   public String getText() {
	       return text;
	   }
	 
	   public static Gender getByCode(String genderCode) {
	       for (Gender g : Gender.values()) {
	           if (g.code.equals(genderCode)) {
	               return g;
	           }
	       }
	       return null;
	   }
	 
	   @Override
	   public String toString() {
	       return this.text;
	   }
	 
	}