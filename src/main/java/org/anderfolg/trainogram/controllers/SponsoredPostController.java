package org.anderfolg.trainogram.controllers;

import org.anderfolg.trainogram.entities.ApiResponse;
import org.anderfolg.trainogram.entities.dto.PostDto;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.SponsorPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sponsor-posts")
//  TODO (Bogdan O.) 7/4/23: use pagination for "getAll" method types
public class SponsoredPostController {
    private final SponsorPostService sponsorPostService;

    @Autowired
    public SponsoredPostController( SponsorPostService sponsorPostService ) {
        this.sponsorPostService = sponsorPostService;
    }

    @PostMapping(value = "/")
    public ResponseEntity<ApiResponse> addSponsoredPost( @RequestParam("description") @Valid String description,
                                                         JwtUser jwtUser,
                                                @RequestParam("sponsorID") Long sponsorID,
                                                @RequestPart("image") MultipartFile image ) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status436DoesntExistException, Status419UserException {
        sponsorPostService.addSponsorPost(description,jwtUser,image,sponsorID);
        return new ResponseEntity<>(new ApiResponse(true, "Post has been added"), HttpStatus.CREATED);
    }

    @PutMapping(value="/{postID}")
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

    //  TODO (Bogdan O.) 24/4/23: refactor this
    @GetMapping(value = "/all")
    public ResponseEntity<List<Post>> getAll() {
        List<Post> sponsoredPosts= sponsorPostService.listSponsoredPosts();
        return new ResponseEntity<>(sponsoredPosts, HttpStatus.OK);
    }
    @GetMapping(value = "/all-by-user")
    public ResponseEntity<List<PostDto>> getAllByUser( JwtUser jwtUser) throws Status419UserException {
        List<PostDto> sponsoredPosts = sponsorPostService.listSponsoredPostsByUser(jwtUser);
        return new ResponseEntity<>(sponsoredPosts, HttpStatus.OK);
    }
    @GetMapping(value = "/all-by-sponsor/{sponsorID}")
    public ResponseEntity<List<Post>> getAllBySponsor(@PathVariable("sponsorID") Long sponsorID) throws Status419UserException {
        List<Post> sponsoredPosts = sponsorPostService.listSponsoredPostsBySponsor(sponsorID);
        return new ResponseEntity<>(sponsoredPosts, HttpStatus.OK);
    }
}
