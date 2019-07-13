package Utilities;


public class Authentication {


    private static Authentication instance = null;
    private Authentication() {
    }
    public static Authentication shared() {
        if(instance == null) {
            instance = new Authentication();
        }
        return instance;
    }
    public String getUserNameFor(String userType){
        return PropertyReader.shared().getProperty(userType.toUpperCase()) ;
    }
    public String getPasswordFor(String userType){
        return PropertyReader.shared().getProperty(userType.toUpperCase()) ;
    }
    public String getEmailFor(String userType){
        return PropertyReader.shared().getProperty(userType.toUpperCase()) ;
    }
}
