package com.luftborn.luftborn_backend.security.evaluator;

import com.luftborn.luftborn_backend.repository.SongRepository;
import com.luftborn.luftborn_backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("songSecurity")
@RequiredArgsConstructor
public class SongSecurity {
    private final SongRepository songRepository;

    @Transactional(readOnly = true)
    public boolean isArtistOfSong(Long songId) {
        //Check if SecurityContext is populated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails))
            return false;

        return songRepository.findById(songId)
                .map(song -> song.getArtist().getId().equals(userDetails.id()))
                .orElse(false); //Song doesn't exist
    }
}
