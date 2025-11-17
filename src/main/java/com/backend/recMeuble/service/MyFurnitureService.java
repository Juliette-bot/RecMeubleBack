package com.backend.recMeuble.service;

import com.backend.recMeuble.entity.Furniture;
import com.backend.recMeuble.entity.Picture;
import com.backend.recMeuble.entity.User;
import com.backend.recMeuble.repository.FurnitureRepository;
import com.backend.recMeuble.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyFurnitureService {
    private final FurnitureRepository furnitureRepository;
    private final CurrentUserService currentUserService;
    private final PictureRepository pictureRepository;

    /**
     * Liste des meubles de l'utilisatrice connectée
     */
    @Transactional(readOnly = true)
    public List<Furniture> getMyFurniture() {
        User current = currentUserService.getCurrentUser();
        return furnitureRepository.findBySeller(current);
    }

    /**
     * Création d'un meuble pour l'utilisatrice connectée
     * (le controller nous passera un Furniture construit à partir du body)
     */
    @Transactional
    public Furniture createMyFurniture(Furniture furniture) {
        User current = currentUserService.getCurrentUser();
        furniture.setSeller(current);
        // le status est déjà forcé à PENDING_REVIEW dans le contrôleur
        return furnitureRepository.save(furniture);
    }

    /**
     * Mise à jour d'un meuble appartenant à l'utilisatrice connectée
     */
    @Transactional
    public Furniture updateMyFurniture(Integer id, Furniture updated) {
        User current = currentUserService.getCurrentUser();

        Furniture existing = furnitureRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Meuble introuvable"));

        if (!existing.getSeller().getId().equals(current.getId())) {
            throw new AccessDeniedException("Tu ne peux modifier que tes propres meubles");
        }

        // 🔹 On met à jour uniquement les champs éditables par l'utilisatrice
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setHeight(updated.getHeight());
        existing.setWidth(updated.getWidth());
        existing.setPrice(updated.getPrice());
        existing.setType(updated.getType());
        existing.setAddress(updated.getAddress());
        // ❌ on NE TOUCHE PAS à existing.getStatus()

        return furnitureRepository.save(existing);
    }

    /**
     * Suppression d'un meuble appartenant à l'utilisatrice connectée
     */
    @Transactional
    public void deleteMyFurniture(Integer id) {
        User current = currentUserService.getCurrentUser();

        Furniture existing = furnitureRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Meuble introuvable"));

        if (!existing.getSeller().getId().equals(current.getId())) {
            throw new AccessDeniedException("Tu ne peux supprimer que tes propres meubles");
        }

        furnitureRepository.delete(existing);
    }

    @Transactional
    public List<Picture> addPictures(Integer furnitureId, List<MultipartFile> files) throws IOException, IOException {
        var current = currentUserService.getCurrentUser();

        Furniture furniture = furnitureRepository.findById(furnitureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meuble introuvable"));

        if (!furniture.getSeller().getId().equals(current.getId())) {
            throw new AccessDeniedException("Tu ne peux ajouter des photos que à tes propres meubles");
        }

        Path uploadDir = Paths.get("uploads/furniture");
        Files.createDirectories(uploadDir);

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target);

            String url = "/uploads/furniture/" + filename;

            Picture picture = Picture.builder()
                    .url(url)
                    .altText(file.getOriginalFilename() != null ? file.getOriginalFilename() : "Photo du meuble")
                    .furniture(furniture)
                    .build();

            pictureRepository.save(picture);
            furniture.getPictures().add(picture);
        }

        return furniture.getPictures();
    }

    @Transactional(readOnly = true)
    public List<Picture> getPictures(Integer furnitureId) {
        User current = currentUserService.getCurrentUser();

        Furniture furniture = furnitureRepository.findById(furnitureId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Meuble introuvable"));

        // sécurité : uniquement la vendeuse peut voir ses photos
        if (!furniture.getSeller().getId().equals(current.getId())) {
            throw new AccessDeniedException("Tu ne peux voir les photos que de tes propres meubles");
        }

        // si tu as furniture.getPictures() mappé :
        if (furniture.getPictures() != null) {
            return furniture.getPictures();
        }

        // sinon tu peux utiliser pictureRepository.findByFurniture(furniture)
        return pictureRepository.findByFurniture(furniture);
    }
}
