package sirenko.mar.bank.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "moneyAmount")
    private double moneyAmount;

    public Account() {

    }

    public Account (User user, double moneyAmount) {
        this.user = user;
        this.moneyAmount = moneyAmount;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMoneyAmount(double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "Account {" +
                "id = " + id +
                ", userId = " + (user != null ? user.getId() : null) +
                ", moneyAmount = " + (int) moneyAmount +
                '}';
    }

}
