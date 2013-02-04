package li.demo.model;

import li.annotation.Field;
import li.annotation.Table;

@Table("t_account")
public class User {
    @Field
    private Integer id;
    @Field
    private String username;
    @Field
    private String password;
    @Field
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}