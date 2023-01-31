package nextmainfocus.account;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Account {
	@GeneratedValue
	@UuidGenerator(style = Style.TIME)
	@Column(name = "id", updatable = false, nullable = false)
	private @Id @Getter UUID id;
	private @Getter @Setter String username;
	private @Getter @Setter String password;
	private @Getter @Setter String role;

	// methods below are from Spring Security and not used for now

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + "]";
	}
}
