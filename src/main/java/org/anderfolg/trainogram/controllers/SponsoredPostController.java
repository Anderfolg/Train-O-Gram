package org.anderfolg.trainogram.controllers;

import org.anderfolg.trainogram.entities.ApiResponse;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.entities.dto.PostDto;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.SponsorPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/sponsor-posts")
public class SponsoredPostController {
    private final SponsorPostService sponsorPostService;

    @Autowired
    public SponsoredPostController( SponsorPostService sponsorPostService ) {
        this.sponsorPostService = sponsorPostService;
    }

    @PostMapping( "/")
    public ResponseEntity<ApiResponse> addSponsoredPost( @RequestParam("description") @Valid String description,
                                                         JwtUser jwtUser,
                                                @RequestParam("sponsorID") Long sponsorID,
                                                @RequestPart("image") MultipartFile image ) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status436DoesntExistException, Status419UserException {
        sponsorPostService.addSponsorPost(description,jwtUser,image,sponsorID);
        return new ResponseEntity<>(new ApiResponse(true, "Post has been added"), HttpStatus.CREATED);
    }

    @PutMapping("/{postID}")
    public ResponseEntity<ApiResponse> updateSponsoredPost(@RequestParam("description") @Valid String description,
                                                  @PathVariable("postID") Long postId,
                                                           JwtUser jwtUser,
                                                  @RequestPart("image") MultipartFile image) throws Status436DoesntExistException, Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status419UserException {
        sponsorPostService.updateSponsorPost(description, jwtUser, image, postId);
        return new ResponseEntity<>(new ApiResponse(true, "Post has been updated"), HttpStatus.OK);
    }
    @DeleteMapping(value = "/{postID}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable("postID") final Long postID,
                                                  JwtUser jwtUser) throws Status436DoesntExistException, Status419UserException {
        sponsorPostService.deleteSponsorPost( jwtUser, postID);
        return new ResponseEntity<>(new ApiResponse(true, "Post has been deleted"), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Page<Post>> getAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        Page<Post> sponsoredPosts= sponsorPostService.listSponsoredPosts(page, size);
        return new ResponseEntity<>(sponsoredPosts, HttpStatus.OK);
    }

    @GetMapping( "/all-by-user")
    public ResponseEntity<Page<PostDto>> getAllByUser( JwtUser jwtUser,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) throws Status419UserException {
        Page<PostDto> sponsoredPosts = sponsorPostService.listSponsoredPostsByUser(jwtUser, page, size);
        return new ResponseEntity<>(sponsoredPosts, HttpStatus.OK);
    }
    @GetMapping( "/all-by-sponsor/{sponsorID}")
    public ResponseEntity<Page<Post>> getAllBySponsor(@PathVariable("sponsorID") Long sponsorID,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) throws Status419UserException {
        Page<Post> sponsoredPosts = sponsorPostService.listSponsoredPostsBySponsor(sponsorID, page, size);
        return new ResponseEntity<>(sponsoredPosts, HttpStatus.OK);
    }
}
