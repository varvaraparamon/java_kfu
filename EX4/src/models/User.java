package EX4.src.models;



public class User {
    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private Integer age;


    public static class UserBuilder {
        private String name;
        private String surname;
        private String phone;
        private String email;
        private Integer age;

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }
        public UserBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }
        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UserBuilder age(Integer age) {
            this.age = age;
            return this;
        }
        public User build() {
            return new User(name, surname, phone, email, age);
        }
    }

    public User(){
    }

    public User(String name, String surname, String phone, String email, Integer age){
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.age = age;

    }

    public User(Long id, String name, String surname, String phone, String email, Integer age){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.age = age;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String userToString(){
        return (this.name + " " + this.surname + " " + this.phone + " " + this.email + " " + this.age);
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setAge(Integer age){
        this.age = age;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

    public String getPhone(){
        return this.phone;
    }

    public String getEmail(){
        return this.email;
    }

    public Integer getAge(){
        return this.age;
    }
}
