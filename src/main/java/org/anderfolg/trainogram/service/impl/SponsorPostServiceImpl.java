package org.anderfolg.trainogram.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.anderfolg.trainogram.entities.ContentType;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.entities.SponsorPost;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.entities.dto.PostDto;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.repo.SponsorPostRepository;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.PostService;
import org.anderfolg.trainogram.service.SponsorPostService;
import org.anderfolg.trainogram.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public void addSponsorPost( String description, JwtUser jwtUser, MultipartFile file, Long sponsorID) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status436DoesntExistException, Status419UserException {
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
    public void updateSponsorPost( String description, JwtUser jwtUser, MultipartFile file, Long sponsorPostID ) throws Status435StorageException, Status436DoesntExistException, Status432InvalidFileNameException, Status430InvalidFileException, Status419UserException {
        if ( sponsorPostRepository.existsById(sponsorPostID) && sponsorPostRepository.findById(sponsorPostID).isPresent() ){
            SponsorPost sponsorPost = sponsorPostRepository.findById(sponsorPostID).get();
            postService.updatePost(sponsorPost.getSponsoredPost().getId(), description, jwtUser, file);
            log.info("Sponsored post has been updated");
        }
        else throw new Status436DoesntExistException("Sponsored post doesn't exist");

    }

    @Override
    @Transactional
    public void deleteSponsorPost( JwtUser jwtUser, Long sponsorPostID ) throws Status436DoesntExistException, Status419UserException {
        if ( sponsorPostRepository.existsById(sponsorPostID) && sponsorPostRepository.findById(sponsorPostID).isPresent() ){
            SponsorPost sponsorPost = sponsorPostRepository.findById(sponsorPostID).get();
            postService.deletePost(sponsorPost.getSponsoredPost().getId(), jwtUser);
            sponsorPostRepository.delete(sponsorPost);
            log.info("Sponsored post has been deleted");
        }
        else throw new Status436DoesntExistException("Sponsored post doesn't exist");
    }


    @Override
    public Page<Post> listSponsoredPosts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<SponsorPost> sponsorPosts = sponsorPostRepository.findAll(pageable);
        List<Post> posts = new ArrayList<>();
        for (SponsorPost sponsorPost : sponsorPosts) {
            posts.add(sponsorPost.getSponsoredPost());
        }
        return new PageImpl<>(posts, pageable, sponsorPosts.getTotalElements());
    }


    @Override
    public Page<PostDto> listSponsoredPostsByUser( JwtUser jwtUser , int page, int pageSize) throws Status419UserException {
        return postService.listPostsByUserAndType(jwtUser,ContentType.SPONSORED_POST, page, pageSize);
    }

    @Override
    public Page<Post> listSponsoredPostsBySponsor(Long sponsorID, int page, int size) throws Status419UserException {
        User sponsor = userService.findUserById(sponsorID);
        Pageable pageable = PageRequest.of(page, size);
        Page<SponsorPost> sponsorPostsPage = sponsorPostRepository.findAllBySponsor(sponsor, pageable);
        List<Post> posts = sponsorPostsPage.getContent().stream()
                .map(SponsorPost::getSponsoredPost)
                .toList();
        return new PageImpl<>(posts, pageable, sponsorPostsPage.getTotalElements());
    }

}
