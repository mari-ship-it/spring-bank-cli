package sirenko.mar.bank.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

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
    private BigDecimal moneyAmount;

    public Account() {

    }

    public Account (User user, BigDecimal moneyAmount) {
        this.user = user;
        this.moneyAmount = moneyAmount;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "Account {" +
                "id = " + id +
                ", userId = " + (user != null ? user.getId() : null) +
                ", moneyAmount = " + moneyAmount +
                '}';
    }

}
