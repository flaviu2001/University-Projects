package domain;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    public DBManager() {
        try{
            Class.forName("org.postgresql.Driver");
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public User getUserFromName(String username) {
        User user = null;
        String statement = "select * from users where username='" + username + "'";
        try (var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jsp", "postgres", "admin");
             var preparedStatement = connection.prepareStatement(statement);
             var rs = preparedStatement.executeQuery()) {
            if (rs.next())
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return user;
    }

    public User authenticate(String username, String password) {
        User user = getUserFromName(username);
        if (user != null && user.getPassword().equals(password))
            return user;
        return null;
    }

    public List<Url> UrlsOfUser(String username) {
        List<Url> urls = new ArrayList<>();
        String statement = "select urls.* from urls join users on urls.userid = users.id where users.username = '" + username + "'";
        try (var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jsp", "postgres", "admin");
             var preparedStatement = connection.prepareStatement(statement);
             var rs = preparedStatement.executeQuery()) {
            while (rs.next())
                urls.add(new Url(rs.getInt("id"), rs.getInt("userid"), rs.getString("url")));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return urls;
    }

    public void deleteUrl(int id) {
        String statement = "delete from urls where id = '" + id + "'";
        try (var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jsp", "postgres", "admin");
             var preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public void addUrl(int userId, String url) {
        String statement = "select max(urls.id)+1 as id from urls";
        int newId = 0;
        try (var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jsp", "postgres", "admin");
             var preparedStatement = connection.prepareStatement(statement);
             var rs = preparedStatement.executeQuery()) {
            if (rs.next())
                newId = rs.getInt("id");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        statement = "insert into urls values (" + newId + ", " + userId + ", '" + url + "')";
        try (var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jsp", "postgres", "admin");
             var preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public List<Pair<String, Integer>> getPopularUrls(int howMany) {
        String statement = "select urls.url as url, count(*) as cnt from urls group by urls.url order by count(*) desc limit " + howMany;
        List<Pair<String, Integer>> urls = new ArrayList<>();
        try (var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jsp", "postgres", "admin");
             var preparedStatement = connection.prepareStatement(statement);
             var rs = preparedStatement.executeQuery()) {
            while (rs.next())
                urls.add(new Pair<>(rs.getString("url"), rs.getInt("cnt")));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return urls;
    }
}
