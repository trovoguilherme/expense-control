package br.com.iug.entity;

import br.com.iug.entity.enums.RoleName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Entity
@Table(name = "ROLE")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_NAME", nullable = false, unique = true)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return this.roleName.toString();
    }

}
