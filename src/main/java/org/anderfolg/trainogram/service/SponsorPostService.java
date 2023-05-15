package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.dto.PostDto;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface SponsorPostService {
    void addSponsorPost(String description, JwtUser jwtUser, MultipartFile file, Long sponsorID)
            throws Status435StorageException,
            Status432InvalidFileNameException,
            Status430InvalidFileException,
            Status436DoesntExistException,
            Status419UserException;
    void updateSponsorPost(String description, JwtUser jwtUser, MultipartFile file, Long sponsorPostID)
            throws Status435StorageException,
            Status436DoesntExistException,
            Status432InvalidFileNameException,
            Status430InvalidFileException,
            Status419UserException;
    void deleteSponsorPost( JwtUser jwtUser, Long sponsorPostID)
            throws Status436DoesntExistException, Status419UserException;

    Page<Post> listSponsoredPosts(int page, int pageSize);
    Page<PostDto> listSponsoredPostsByUser( JwtUser jwtUser , int page, int pageSize)
            throws Status419UserException;
    Page<Post> listSponsoredPostsBySponsor(Long sponsorID, int page, int size)
            throws Status419UserException;

}
