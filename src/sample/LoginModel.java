package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginModel {
    Connection connection;

    public LoginModel(){
        connection = SQLiteConnection.Connector();
    }

    public boolean isDbConnected(){
        try{
            return !connection.isClosed();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void deleteRecord(Yakit yakit) throws SQLException {
        PreparedStatement ps= null ;

        String query = " DELETE FROM yakit_kayitlari WHERE id = ?";
        ps = connection.prepareStatement(query);
        Long id = yakit.getId();
        ps.setLong(1,yakit.getId());
        ps.executeUpdate();
        ps.close();
    }

//// BURADAYIZ
    public ArrayList<Yakit> loadData(String m) throws SQLException {

        PreparedStatement ps= null ;
        ResultSet rs=null;
        String merkez = m.toLowerCase();

        ArrayList<Yakit> kayitlar=new ArrayList<>();
        String query = "";
        if(merkez.equals("hepsi")) {
            query = "SELECT * FROM yakit_kayitlari ";
            ps = connection.prepareStatement(query);
        } else {
            query = "SELECT * FROM yakit_kayitlari WHERE LOWER(merkez) = ? ";
            ps = connection.prepareStatement(query);
            ps.setString(1, merkez);
        }

        rs = ps.executeQuery();

        while (rs.next()) {
            kayitlar.add(new Yakit(rs.getLong("id"), rs.getString("merkez"),
            rs.getString("plaka"),rs.getString("tarih"), rs.getString("yakittipi"),
                    rs.getDouble("yakitmiktari"),rs.getDouble("yakitTutari")));
        }
        ps.close();
        rs.close();
        return kayitlar;
    }

    public Yakit findById(Long id) throws SQLException {
        String query = "SELECT * FROM yakit_kayitlari WHERE id =?";
        PreparedStatement ps= connection.prepareStatement(query);
        ps.setLong(1,id);

        ResultSet rs=ps.executeQuery();
        if(rs.next()) {
            Yakit yakit = new Yakit(rs.getLong("id"), rs.getString("merkez"),
                    rs.getString("plaka"), rs.getString("tarih"), rs.getString("yakittipi"),
                    rs.getDouble("yakitmiktari"), rs.getDouble("yakitTutari"));
            ps.close();
            rs.close();
            return yakit;
        } else
        {
            ps.close();
            rs.close();
            return null;
        }
    }

    public void storeDB(ArrayList<Yakit> yakits) throws SQLException {
        PreparedStatement ps=null;

        System.out.println(yakits.size());
        String query = "INSERT INTO yakit_kayitlari (merkez, plaka, tarih, yakittipi, yakitmiktari, yakitTutari) VALUES (?, ?, ?, ?,?,?)";
        String query2 = "UPDATE yakit_kayitlari SET merkez = ?, plaka = ?,tarih=?,yakittipi=?,yakitmiktari=?, yakitTutari=? WHERE id = ?";

       for (int i=0; i<yakits.size(); i++){
           Yakit yakit = yakits.get(i);
           if(findById(yakit.getId()) != null){
                ps = connection.prepareStatement(query2);
                ps.setString(1,yakit.getMerkez());
                ps.setString(2,yakit.getPlaka());
                ps.setString(3, yakit.getTarih());
                ps.setString(4,yakit.getYakittipi());
                ps.setDouble(5,yakit.getYakitmiktari());
                ps.setDouble(6,yakit.getYakitTutari());
                ps.setLong(7, yakit.getId());
                ps.executeUpdate();
               ps.close();

              } else {
                ps = connection.prepareStatement(query);
                ps.setString(1,yakit.getMerkez());
                ps.setString(2,yakit.getPlaka());
                ps.setString(3, yakit.getTarih());
                ps.setString(4,yakit.getYakittipi());
                ps.setDouble(5,yakit.getYakitmiktari());
                ps.setDouble(6,yakit.getYakitTutari());
                ps.executeUpdate();
               ResultSet results = ps.getGeneratedKeys();
                yakit.setId(results.getLong(1));
               ps.close();
               results.close();
           }
        }
    }
}
