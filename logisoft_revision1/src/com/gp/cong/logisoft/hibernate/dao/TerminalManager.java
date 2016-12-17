
package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author NambuRajasekar
 */
@Entity
@Table(name = "terminal_manager")
@DynamicInsert(true)
@DynamicUpdate(true)

public class TerminalManager extends Domain implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User user;
    @JoinColumn(name = "trmnum", referencedColumnName = "trmnum")
    @ManyToOne(optional = false)
    private RefTerminal refTerminal;
    
    

    public TerminalManager() {
    }

    public TerminalManager(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RefTerminal getRefTerminal() {
        return refTerminal;
    }

    public void setRefTerminal(RefTerminal refTerminal) {
        this.refTerminal = refTerminal;
    }

}
