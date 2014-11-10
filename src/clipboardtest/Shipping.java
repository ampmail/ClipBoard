import java.text.SimpleDateFormat;
import java.util.Date;

public class Shipping {
    
    Integer brakID;
    Date createDate;
    String marka;
    String SuplierName;
    String tipVozvrata;

    Shipping( int bID, Date crDate, String marka, String supl, String tipVozvrata ){
        this.brakID = bID;
        this.createDate = crDate;
        this.marka = marka;
        this.SuplierName = supl;
        this.tipVozvrata = tipVozvrata;
    }
    public String[] getData(){
        String[] data = {this.SuplierName,
                         this.tipVozvrata,
                         new SimpleDateFormat("dd.MM.yyyy").format( this.createDate ), 
                         this.brakID.toString(), 
                         this.marka};
        return data;
    }
}