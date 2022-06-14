package uz.pdp.class_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.class_manager.entity.enums.RoleEnum;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "users")
@Transactional
public class User implements UserDetails {

    public User(String username, String password, String email, RoleEnum role, List<String> subject_name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.subject_names = subject_name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;


    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @CreationTimestamp
    private Timestamp created_at;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @ElementCollection
    private List<String> subject_names;

    @UpdateTimestamp
    private Timestamp last_updated_at;


    @OneToOne
    private Attachment attachment;

    @OneToMany(mappedBy = "user")
    private List<Message> messages;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;


    //=====================METHODS FOR WEB USER==========================================================================================================================================================================================================================================================================================================================================================================================================================================================================================


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.name());
        authorities.add(grantedAuthority);
        return authorities;

    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
