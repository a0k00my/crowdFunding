package com.intuit.funding.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PROJECT")
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Column(length = 1000)
    private String description;
    private double requestedAmount;
    private double currentAmount;
    private String status;
    private Date endDate;
    private Date createdDate;
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "project")
    private List<Contribution> contributions = new ArrayList<>();

    public double getTotalContribution() {
        return contributions.stream().mapToDouble(Contribution::getAmount).sum();
    }
}
