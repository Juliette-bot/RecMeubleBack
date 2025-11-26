package com.backend.recMeuble.controller;

import com.backend.recMeuble.DTO.MyFurnitureRequest;
import com.backend.recMeuble.DTO.MyFurnitureResponse;
import com.backend.recMeuble.DTO.PictureResponse;
import com.backend.recMeuble.entity.*;
import com.backend.recMeuble.mapper.MyFurnitureMapper;
import com.backend.recMeuble.repository.AddressRepository;
import com.backend.recMeuble.repository.FurnitureTypeRepository;
import com.backend.recMeuble.service.MyFurnitureService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/my/furniture")
@RequiredArgsConstructor
public class MyFurnitureController {

    private final MyFurnitureService myFurnitureService;
    private final FurnitureTypeRepository furnitureTypeRepository;
    private final AddressRepository addressRepository;

    /**
     * Liste des meubles de l'utilisatrice connectée
     */
    @GetMapping
    public List<MyFurnitureResponse> getMyFurniture() {
        return myFurnitureService.getMyFurniture()
                .stream()
                .map(MyFurnitureMapper::toResponse)
                .collect(toList());
    }

    /**
     * Création d'un meuble pour l'utilisatrice connectée
     */
    @PostMapping
    public ResponseEntity<MyFurnitureResponse> createMyFurniture(@RequestBody MyFurnitureRequest request) {
        FurnitureType type = furnitureTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Type de meuble invalide"));

        Address address = null;
        if (request.getAddressId() != null) {
            address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Adresse invalide"));
        }

        Furniture furniture = Furniture.builder()
                .name(request.getName())
                .description(request.getDescription())
                .height(request.getHeight())
                .width(request.getWidth())
                .price(request.getPrice())
                .type(type)
                .address(address)
                // 🔒 status fixé côté serveur à la création
                .status(FurnitureStatus.PENDING_REVIEW)
                .build();

        Furniture saved = myFurnitureService.createMyFurniture(furniture);
        MyFurnitureResponse response = MyFurnitureMapper.toResponse(saved);

        return ResponseEntity
                .created(URI.create("/api/my/furniture/" + saved.getId()))
                .body(response);
    }


    @PostMapping("/{id}/pictures")
    public List<PictureResponse> uploadPictures(@PathVariable Integer id,
                                                @RequestParam("files") List<MultipartFile> files) throws IOException, java.io.IOException {
        return myFurnitureService.addPictures(id, files).stream()
                .map(PictureResponse::fromEntity)
                .toList();
    }


    /**
     * Liste des photos d'un meuble appartenant à l'utilisatrice connectée
     */
    @GetMapping("/{id}/pictures")
    public List<PictureResponse> getPictures(@PathVariable Integer id) {
        return myFurnitureService.getPictures(id).stream()
                .map(PictureResponse::fromEntity)
                .toList();
    }


    /**
     * Mise à jour d'un meuble appartenant à l'utilisatrice connectée.
     * Le status n'est PAS modifiable ici.
     */
    @PutMapping("/{id}")
    public MyFurnitureResponse updateMyFurniture(@PathVariable Integer id,
                                                 @RequestBody MyFurnitureRequest request) {

        //User current = currentUserService.getCurrentUser(); furniture.getseller est different que getcurrentuser
            // recupere tout la furniture ici verif le user via le  mail, find by seller
        FurnitureType type = furnitureTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Type de meuble invalide"));

        Address address = null;
        if (request.getAddressId() != null) {
            address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Adresse invalide"));
        }

        Furniture updated = Furniture.builder()
                .name(request.getName())
                .description(request.getDescription())
                .height(request.getHeight())
                .width(request.getWidth())
                .price(request.getPrice())
                .type(type)
                .address(address)
                // ❌ pas de status ici : il sera conservé tel quel dans le service
                .build();

        Furniture saved = myFurnitureService.updateMyFurniture(id, updated);
        return MyFurnitureMapper.toResponse(saved);
    }

    /**
     * Suppression d'un meuble appartenant à l'utilisatrice connectée
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyFurniture(@PathVariable Integer id) {
        myFurnitureService.deleteMyFurniture(id);
    }
}
