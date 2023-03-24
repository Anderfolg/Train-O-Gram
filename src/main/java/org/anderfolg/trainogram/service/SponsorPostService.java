package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.DTO.PostDto;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SponsorPostService {
    void addSponsorPost(String description, JwtUser jwtUser, MultipartFile file, Long sponsorID) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status436PostDoesntExistException, Status419UserException;
    void updateSponsorPost(String description, JwtUser jwtUser, MultipartFile file, Long sponsorPostID) throws Status435StorageException, Status436PostDoesntExistException, Status432InvalidFileNameException, Status430InvalidFileException, Status419UserException;
    void deleteSponsorPost( JwtUser jwtUser, Long sponsorPostID) throws Status436PostDoesntExistException, Status419UserException;

    List<Post> listSponsoredPosts();
    List<PostDto> listSponsoredPostsByUser(JwtUser jwtUser) throws Status419UserException;
    List<Post> listSponsoredPostsBySponsor(Long sponsorID) throws Status419UserException;

}
