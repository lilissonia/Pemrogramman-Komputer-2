
package produk;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
/**
 *
 * @author lilissonia
 */
public class Koneksi {

    /**
     * @param args the command line arguments
     */
    public static MongoDatabase sambungDB() {
        // TODO code application logic here
        try {
            MongoClient client = MongoClients.create();
            MongoDatabase database = client.getDatabase("product");
            System.out.println("Koneksi sukses!");
            return database;
        } catch (Exception e) {
            System.err.println("Koneksi Gagal !"+e.getMessage());
        }
        return null;
    }
    
}
