package com.luftborn.luftborn_backend.security.evaluator;

import com.luftborn.luftborn_backend.repository.AlbumRepository;
import com.luftborn.luftborn_backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("albumSecurity")
@RequiredArgsConstructor
public class AlbumSecurity {
    private final AlbumRepository albumRepository;

    @Transactional(readOnly = true)
    public boolean isArtistOfAlbum(Long albumId) {
        //Check if SecurityContext is populated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails))
            return false;

        return albumRepository.findById(albumId)
                .map(album -> album.getArtist().getId().equals(userDetails.id()))
                .orElse(false); //Album doesn't exist
    }
}
