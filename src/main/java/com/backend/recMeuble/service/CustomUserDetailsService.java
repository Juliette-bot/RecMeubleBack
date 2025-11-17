package com.backend.recMeuble.service;

import com.backend.recMeuble.DTO.UserUpdateDto;
import com.backend.recMeuble.entity.User;
import com.backend.recMeuble.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository userRepository;

    // ---------- Authentification (utilisé par Spring Security) ----------
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ici username = email si tu as configuré comme ça
        return userRepository.findByMail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));
    }

    // ---------- Récupérer l'utilisatrice connectée ----------
    @Transactional(readOnly = true)
    public User getCurrentUserPublic() {
        return getCurrentUser();
    }

    @Transactional(readOnly = true)
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new EntityNotFoundException("Aucune utilisatrice connectée");
        }

        String email = auth.getName(); // par défaut, c'est le username ⇒ ton mail
        return userRepository.findByMail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateurice non trouvé·e"));
    }

    // ---------- Mise à jour du compte courant ----------
    @Transactional
    public User updateCurrentUser(UserUpdateDto dto) {
        User user = getCurrentUser();

        if (dto.getFirstName() != null) user.setFirstname(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastname(dto.getLastName());
        if (dto.getMail() != null) user.setMail((dto.getMail());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());

        return userRepository.save(user);
    }

    // ---------- Suppression du compte courant ---------,
    @Transactional
    public void deleteCurrentUser() {
        User user = getCurrentUser();

        // Suppression réelle :
        userRepository.delete(user);

        // OU soft delete :
        // user.setActive(false);
        // userRepository.save(user);
    }
}
