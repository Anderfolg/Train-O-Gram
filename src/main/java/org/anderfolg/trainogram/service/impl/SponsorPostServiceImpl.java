package org.anderfolg.trainogram.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.ContentType;
import org.anderfolg.trainogram.entities.DTO.PostDto;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.entities.SponsorPost;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.repo.SponsorPostRepository;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SponsorPostServiceImpl implements SponsorPostService {

    private final PostService postService;
    private final SponsorPostRepository sponsorPostRepository;
    private final UserService userService;


    @Transactional
    @Override
    public void addSponsorPost( String description, JwtUser jwtUser, MultipartFile file, Long sponsorID) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status436PostDoesntExistException, Status419UserException {
        postService.addPost(description, jwtUser, file);

        SponsorPost sponsorPost = SponsorPost.builder()
                .sponsoredPost(postService.findPostById(postService.findLatestPostId()))
                .sponsor(userService.findUserById(sponsorID))
                .type(ContentType.SPONSORED_POST)
                .build();
        sponsorPostRepository.save(sponsorPost);
        log.info("Sponsored post has been added");

    }

    @Override
    public void updateSponsorPost( String description, JwtUser jwtUser, MultipartFile file, Long sponsorPostID ) throws Status435StorageException, Status436PostDoesntExistException, Status432InvalidFileNameException, Status430InvalidFileException, Status419UserException {
        if ( sponsorPostRepository.existsById(sponsorPostID) && sponsorPostRepository.findById(sponsorPostID).isPresent() ){
            SponsorPost sponsorPost = sponsorPostRepository.findById(sponsorPostID).get();
            postService.updatePost(sponsorPost.getSponsoredPost().getId(), description, jwtUser, file);
            log.info("Sponsored post has been updated");
        }
        else throw new Status436PostDoesntExistException("Sponsored post doesn't exist");

    }

    @Override
    @Transactional
    public void deleteSponsorPost( JwtUser jwtUser, Long sponsorPostID ) throws Status436PostDoesntExistException, Status419UserException {
        if ( sponsorPostRepository.existsById(sponsorPostID) && sponsorPostRepository.findById(sponsorPostID).isPresent() ){
            SponsorPost sponsorPost = sponsorPostRepository.findById(sponsorPostID).get();
            postService.deletePost(sponsorPost.getSponsoredPost().getId(), jwtUser);
            sponsorPostRepository.delete(sponsorPost);
            log.info("Sponsored post has been deleted");
        }
        else throw new Status436PostDoesntExistException("Sponsored post doesn't exist");
    }


    @Override
    public List<Post> listSponsoredPosts(){
        List<SponsorPost> sponsorPosts = sponsorPostRepository.findAll();
        List<Post> posts = new ArrayList<>();
        for (SponsorPost sponsorPost:sponsorPosts) {
            posts.add(sponsorPost.getSponsoredPost());
        }
        return posts;
    }

    @Override
    public List<PostDto> listSponsoredPostsByUser( JwtUser jwtUser ) throws Status419UserException {
        return postService.listPostsByUserAndType(jwtUser,ContentType.SPONSORED_POST);
    }

    @Override
    public List<Post> listSponsoredPostsBySponsor( Long sponsorID ) throws Status419UserException {
        List<SponsorPost> sponsorPosts = sponsorPostRepository.findAllBySponsor(userService.findUserById(sponsorID));
        List<Post> posts = new ArrayList<>();
        for (SponsorPost sponsorPost:sponsorPosts) {
            posts.add(sponsorPost.getSponsoredPost());
        }
        return posts;
    }
}
